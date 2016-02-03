package org.epnoi.modeler.models.topic;

import es.upm.oeg.epnoi.matching.metrics.domain.entity.RegularResource;
import org.epnoi.model.domain.Resource;
import org.epnoi.model.domain.*;
import org.epnoi.modeler.scheduler.ModelingTask;
import org.epnoi.modeler.helper.ModelingHelper;
import org.epnoi.modeler.models.WordDistribution;
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
        helper.getUdm().findTopicsByDomain(domain.getUri()).stream().forEach(topic -> helper.getUdm().deleteTopic(topic));

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
                case DOCUMENT: regularResources = helper.getUdm().
                        findDocumentsByDomain(
                                domain.getUri()).stream().
                        map(uri -> helper.getUdm().readDocument(uri)).
                        filter(res -> res.isPresent()).map(res -> res.get()).
                        map(document -> helper.getRegularResourceBuilder().from(document.getUri(), document.getTitle(), document.getAuthoredOn(), helper.getAuthorBuilder().composeFromMetadata(document.getAuthoredBy()), document.getTokens())).
                        collect(Collectors.toList());
                    break;
                //TODO Optimize using Spark.parallel
                case ITEM: regularResources = helper.getUdm().
                        findItemsByDomain(domain.getUri()).stream().
                        map(uri -> helper.getUdm().readItem(uri)).
                        filter(res -> res.isPresent()).map(res -> res.get()).
                        map(item -> helper.getRegularResourceBuilder().from(item.getUri(), item.getTitle(), item.getAuthoredOn(), helper.getAuthorBuilder().composeFromMetadata(item.getAuthoredBy()), item.getTokens())).
                        collect(Collectors.toList());
                    break;
                //TODO Optimize using Spark.parallel
                case PART: regularResources = helper.getUdm().
                        findPartsByDomain(domain.getUri()).stream().
                        map(uri -> helper.getUdm().readPart(uri)).
                        filter(res -> res.isPresent()).map(res -> res.get()).
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
            helper.getUdm().saveAnalysis(analysis);
        } catch (RuntimeException e){
            LOG.warn(e.getMessage(),e);
        } catch (Exception e){
            LOG.error(e.getMessage(),e);
        }
    }

    private void persistModel(Analysis analysis, TopicModel model, Resource.Type resourceType){

        String creationTime = helper.getTimeGenerator().getNowAsISO();
        Map<String,String> topicTable = new HashMap<>();
        for (TopicData topicData : model.getTopics()){

            // Save Topic
            Topic topic = new Topic();
            topic.setUri(helper.getUriGenerator().newTopic());
            topic.setAnalysis(analysis.getUri());
            topic.setCreationTime(creationTime);
            topic.setContent(String.join(",",topicData.getWords().stream().map(wd -> wd.getWord()).collect(Collectors.toList())));
            helper.getUdm().saveTopic(topic, domain.getUri(), analysis.getUri()); // Implicit relation to Domain (and Analysis)

            topicTable.put(topicData.getId(),topic.getUri());

            // Relate it to Words
            for (WordDistribution wordDistribution : topicData.getWords()){

                Optional<String> wordOptional = helper.getUdm().findWordByLemma(wordDistribution.getWord());
                String wordURI;
                if (wordOptional.isPresent()){
                    wordURI = wordOptional.get();
                }else {
                    wordURI = helper.getUriGenerator().newWord();

                    // Create Word
                    Word word = new Word();
                    word.setUri(wordURI);
                    word.setCreationTime(helper.getTimeGenerator().getNowAsISO());
                    word.setContent(wordDistribution.getWord());
                    word.setLemma(wordDistribution.getWord());
                    word.setType("term");
                    helper.getUdm().saveWord(word);

                }

                // Relate Topic to Word (mentions)
                helper.getUdm().relateWordToTopic(wordURI,topic.getUri(),wordDistribution.getWeight());
            }
        }


        Set<String> resourceURIs = model.getResources().keySet();
        for (String resourceURI: resourceURIs){

            for (TopicDistribution topicDistribution: model.getResources().get(resourceURI)){
                // Relate resource  to Topic
                String topicURI = topicTable.get(topicDistribution.getTopic());
                switch(resourceType){
                    case DOCUMENT: helper.getUdm().relateTopicToDocument(topicURI,resourceURI,topicDistribution.getWeight());
                        break;
                    case ITEM: helper.getUdm().relateTopicToItem(topicURI,resourceURI,topicDistribution.getWeight());
                        break;
                    case PART: helper.getUdm().relateTopicToPart(topicURI,resourceURI,topicDistribution.getWeight());
                        break;
                }
            }
        }
        LOG.info("Topic Model saved in ddbb: " + model);
    }
}
