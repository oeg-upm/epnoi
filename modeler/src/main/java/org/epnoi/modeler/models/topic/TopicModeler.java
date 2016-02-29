package org.epnoi.modeler.models.topic;

import es.upm.oeg.epnoi.matching.metrics.domain.entity.RegularResource;
import org.epnoi.model.domain.relations.EmergesIn;
import org.epnoi.model.domain.relations.MentionsFromTopic;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.*;
import org.epnoi.model.utils.TimeUtils;
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

    private final Document document;

    private final ModelingHelper helper;

    private Domain domain;

    public TopicModeler(Document document, ModelingHelper modelingHelper) {
        this.document = document;
        this.helper = modelingHelper;
    }


    @Override
    public void run() {
        //TODO Use of factory to avoid this explicit flow!
        LOG.info("ready to create a new topic model from document: " + document);

        List<String> domainUris = helper.getUdm().find(Resource.Type.DOMAIN).in(Resource.Type.DOCUMENT, document.getUri());

        if ((domainUris == null) || (domainUris.isEmpty())){
            LOG.warn("Unknown domain from document: " + document);
            return;
        }

        Optional<Resource> result = helper.getUdm().read(Resource.Type.DOMAIN).byUri(domainUris.get(0));//TODO Handle more than one domain

        if (!result.isPresent()){
            LOG.warn("Unknown domain from uri: " + domainUris);
            return;
        }

        domain = result.get().asDomain();

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
            Analysis analysis = newAnalysis("Topic-Model","LDA with Evolutionary Algorithm parameterization",resourceType.name(),domain.getUri());

            // Persist Topic and Relations
            TopicModel model = helper.getTopicModelBuilder().build(analysis.getUri(), regularResources);
            persistModel(analysis,model,resourceType);

            // Save the analysis
            analysis.setConfiguration(model.getConfiguration().toString());
            helper.getUdm().save(analysis);
        } catch (RuntimeException e){
            LOG.warn(e.getMessage(),e);
        } catch (Exception e){
            LOG.error(e.getMessage(),e);
        }
    }

    private void persistModel(Analysis analysis, TopicModel model, Resource.Type resourceType){
        Map<String,String> topicTable = new HashMap<>();
        for (TopicData topicData : model.getTopics()){

            // Save Topic
            Topic topic = Resource.newTopic();
            topic.setAnalysis(analysis.getUri());
            topic.setContent(String.join(",",topicData.getWords().stream().map(wd -> wd.getWord()).collect(Collectors.toList())));
            topic.setUri(helper.getUriGenerator().basedOnContent(Resource.Type.TOPIC,topic.getContent()));
            helper.getUdm().save(topic);

            EmergesIn emerges = Relation.newEmergesIn(topic.getUri(), domain.getUri());
            emerges.setAnalysis(analysis.getUri());
            helper.getUdm().save(emerges);


            topicTable.put(topicData.getId(),topic.getUri());

            // Relate it to Words
            for (WordDistribution wordDistribution : topicData.getWords()){

                List<String> result = helper.getUdm().find(Resource.Type.WORD).by(Word.CONTENT, wordDistribution.getWord());
                String wordURI;
                if (result != null && !result.isEmpty()){
                    wordURI = result.get(0);
                }else {
                    wordURI = helper.getUriGenerator().basedOnContent(Resource.Type.WORD,wordDistribution.getWord());

                    // Create Word
                    Word word = Resource.newWord();
                    word.setUri(wordURI);
                    word.setCreationTime(TimeUtils.asISO());
                    word.setContent(wordDistribution.getWord());
                    helper.getUdm().save(word);

                }

                // Relate Topic to Word (mentions)
                MentionsFromTopic mentions = Relation.newMentionsFromTopic(topic.getUri(), wordURI);
                mentions.setWeight(wordDistribution.getWeight());
                helper.getUdm().save(mentions);
            }
        }


        Set<String> resourceURIs = model.getResources().keySet();
        for (String resourceURI: resourceURIs){

            for (TopicDistribution topicDistribution: model.getResources().get(resourceURI)){
                // Relate resource  to Topic
                String topicURI = topicTable.get(topicDistribution.getTopic());
                Relation relation = null;
                switch(resourceType){
                    case DOCUMENT:
                        relation = Relation.newDealsWithFromDocument(resourceURI,topicURI);
                        break;
                    case ITEM:
                        relation = Relation.newDealsWithFromItem(resourceURI,topicURI);
                        break;
                    case PART:
                        relation = Relation.newDealsWithFromPart(resourceURI,topicURI);
                        break;
                }
                relation.setWeight(topicDistribution.getWeight());
                helper.getUdm().save(relation);
            }
        }
        LOG.info("Topic Model saved in ddbb: " + model);
    }
}
