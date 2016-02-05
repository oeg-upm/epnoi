package org.epnoi.modeler.models.topic;

import es.upm.oeg.epnoi.matching.metrics.domain.entity.RegularResource;
import org.epnoi.model.domain.*;
import org.epnoi.modeler.helper.ModelingHelper;
import org.epnoi.modeler.models.WordDistribution;
import org.epnoi.modeler.scheduler.ModelingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by cbadenes on 11/01/16.
 */
public class TopicModeler extends ModelingTask {

    private static final Logger LOG = LoggerFactory.getLogger(TopicModeler.class);

    public TopicModeler(Domain domain, ModelingHelper modelingHelper) {
        super(domain, modelingHelper);
    }


    @Override
    public void run() {
        //TODO Use of factory to avoid this explicit flow!
        LOG.info("ready to create a new topic model for domain: " + domain);

        // Delete previous Topics
        helper.getUdm().find(Resource.Type.TOPIC).in(Resource.Type.DOMAIN,domain.getUri()).stream().forEach(topic -> helper.getUdm().delete(Resource.Type.TOPIC).byUri(topic));

        // Documents
        buildModelfor(Resource.Type.DOCUMENT);

        // Items
        buildModelfor(Resource.Type.ITEM);

        // Parts
        buildModelfor(Resource.Type.PART);

    }


    private void buildModelfor(Resource.Type resourceType){
        try{
            LOG.info("Building a topic model for " + resourceType.name() + "s in domain: " + domain.getUri());

            List<RegularResource> regularResources = new ArrayList<>();

            switch(resourceType){
                //TODO Optimize using Spark.parallel
                case DOCUMENT: regularResources = helper.getUdm().find(Resource.Type.DOCUMENT).in(Resource.Type.DOMAIN, domain.getUri())
                        .stream().
                        map(uri -> helper.getUdm().read(Resource.Type.DOCUMENT).byUri(uri)).
                        filter(res -> res.isPresent()).map(res -> (Document) res.get()).
                        map(document -> helper.getRegularResourceBuilder().from(document.getUri(), document.getTitle(), document.getAuthoredOn(), helper.getAuthorBuilder().composeFromMetadata(document.getAuthoredBy()), document.getTokens())).
                        collect(Collectors.toList());
                    break;
                //TODO Optimize using Spark.parallel
                case ITEM: regularResources = helper.getUdm().find(Resource.Type.ITEM).in(Resource.Type.DOMAIN,domain.getUri())
                        .stream().
                        map(uri -> helper.getUdm().read(Resource.Type.ITEM).byUri(uri)).
                        filter(res -> res.isPresent()).map(res -> (Item) res.get()).
                        map(item -> helper.getRegularResourceBuilder().from(item.getUri(), item.getTitle(), item.getAuthoredOn(), helper.getAuthorBuilder().composeFromMetadata(item.getAuthoredBy()), item.getTokens())).
                        collect(Collectors.toList());
                    break;
                //TODO Optimize using Spark.parallel
                case PART: regularResources = helper.getUdm().find(Resource.Type.PART).in(Resource.Type.DOMAIN,domain.getUri())
                        .stream().
                        map(uri -> helper.getUdm().read(Resource.Type.PART).byUri(uri)).
                        filter(res -> res.isPresent()).map(res -> (Part) res.get()).
                        // TODO Improve metainformation of Part
                                map(part -> helper.getRegularResourceBuilder().from(part.getUri(), part.getSense(), part.getCreationTime(), new ArrayList<User>(), part.getTokens())).
                                collect(Collectors.toList());
                    break;
            }

            if ((regularResources == null) || (regularResources.isEmpty()))
                throw new RuntimeException("No " + resourceType.name() + "s found in domain: " + domain.getUri());

            // Create the analysis
            Analysis analysis = newAnalysis("Topic-Model","LDA with Evolutionary Algorithm parameterization",resourceType.name());

            // Persist Topic and Relations
            TopicModel model = helper.getTopicModelBuilder().build(analysis.getUri(), regularResources);
            persistModel(analysis,model,resourceType);

            // Save the analysis
            analysis.setConfiguration(model.getConfiguration().toString());
            helper.getUdm().save(Resource.Type.ANALYSIS).with(analysis);
        } catch (RuntimeException e){
            LOG.warn(e.getMessage(),e);
        } catch (Exception e){
            LOG.error(e.getMessage(),e);
        }
    }

    private void persistModel(Analysis analysis, TopicModel model, Resource.Type resourceType){

        String creationTime = helper.getTimeGenerator().asISO();
        Map<String,String> topicTable = new HashMap<>();
        for (TopicData topicData : model.getTopics()){

            // Save Topic
            Topic topic = new Topic();
            topic.setUri(helper.getUriGenerator().newFor(Resource.Type.TOPIC));
            topic.setAnalysis(analysis.getUri());
            topic.setCreationTime(creationTime);
            topic.setContent(String.join(",",topicData.getWords().stream().map(wd -> wd.getWord()).collect(Collectors.toList())));
            helper.getUdm().save(Resource.Type.TOPIC).with(topic);
            helper.getUdm().attachFrom(topic.getUri()).to(domain.getUri()).by(Relation.Type.TOPIC_EMERGES_IN_DOMAIN,RelationProperties.builder().description(analysis.getUri()).build());


            topicTable.put(topicData.getId(),topic.getUri());

            // Relate it to Words
            for (WordDistribution wordDistribution : topicData.getWords()){

                List<String> result = helper.getUdm().find(Resource.Type.WORD).by(Word.LEMMA, wordDistribution.getWord());
                String wordURI;
                if (result != null && !result.isEmpty()){
                    wordURI = result.get(0);
                }else {
                    wordURI = helper.getUriGenerator().newFor(Resource.Type.WORD);

                    // Create Word
                    Word word = new Word();
                    word.setUri(wordURI);
                    word.setCreationTime(helper.getTimeGenerator().asISO());
                    word.setContent(wordDistribution.getWord());
                    word.setLemma(wordDistribution.getWord());
                    word.setType("term");
                    helper.getUdm().save(Resource.Type.WORD).with(word);

                }

                // Relate Topic to Word (mentions)
                helper.getUdm().attachFrom(topic.getUri()).to(wordURI).by(Relation.Type.TOPIC_MENTIONS_WORD,RelationProperties.builder().weight(wordDistribution.getWeight()).build());
            }
        }


        Set<String> resourceURIs = model.getResources().keySet();
        for (String resourceURI: resourceURIs){

            for (TopicDistribution topicDistribution: model.getResources().get(resourceURI)){
                // Relate resource  to Topic
                String topicURI = topicTable.get(topicDistribution.getTopic());
                Relation.Type relation = null;
                switch(resourceType){
                    case DOCUMENT:
                        relation = Relation.Type.DOCUMENT_DEALS_WITH_TOPIC;
                        break;
                    case ITEM:
                        relation = Relation.Type.ITEM_DEALS_WITH_TOPIC;
                        break;
                    case PART:
                        relation = Relation.Type.PART_DEALS_WITH_TOPIC;
                        break;
                }
                helper.getUdm().attachFrom(resourceURI).to(topicURI).by(relation,RelationProperties.builder().weight(topicDistribution.getWeight()).build());
            }
        }
        LOG.info("Topic Model saved in ddbb: " + model);
    }
}
