package org.epnoi.modeler.builder;

import es.cbadenes.lab.test.IntegrationTest;
import es.upm.oeg.epnoi.matching.metrics.domain.entity.RegularResource;
import org.epnoi.model.domain.*;
import org.epnoi.modeler.Config;
import org.epnoi.modeler.models.WordDistribution;
import org.epnoi.modeler.models.topic.TopicData;
import org.epnoi.modeler.models.topic.TopicDistribution;
import org.epnoi.modeler.models.topic.TopicModel;
import org.epnoi.storage.UDM;
import org.epnoi.storage.generator.TimeGenerator;
import org.epnoi.storage.generator.URIGenerator;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * Created by cbadenes on 11/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class TopicModelBuilderTest {

    private static final Logger LOG = LoggerFactory.getLogger(TopicModelBuilderTest.class);

    @Autowired
    TopicModelBuilder topicModelBuilder;

    @Autowired
    URIGenerator uriGenerator;

    @Autowired
    TimeGenerator timeGenerator;

    @Autowired
    UDM udm;

    @Autowired
    RegularResourceBuilder regularResourceBuilder;

    @Test
    public void basic(){

        // Source
        Source source = new Source();
        source.setUri(uriGenerator.newFor(Resource.Type.SOURCE));
        source.setCreationTime(timeGenerator.asISO());
        udm.save(Resource.Type.SOURCE).with(source);

        // Domain
        Domain domain = new Domain();
        domain.setUri(uriGenerator.newFor(Resource.Type.DOMAIN));
        domain.setName("test-domain");
        udm.save(Resource.Type.DOMAIN).with(domain);

        // Analysis
        Analysis analysis = new Analysis();
        analysis.setUri(uriGenerator.newFor(Resource.Type.ANALYSIS));
        analysis.setCreationTime(timeGenerator.asISO());
        analysis.setType("topic-model");
        analysis.setDomain(domain.getUri());
        analysis.setDescription("Topic Modeling using LDA");
        analysis.setConfiguration("");
        udm.save(Resource.Type.ANALYSIS).with(analysis);

        // Document 1
        Document document1 = new Document();
        document1.setUri(uriGenerator.newFor(Resource.Type.DOCUMENT));
        document1.setCreationTime(timeGenerator.asISO());
        document1.setTitle("title-1");
        document1.setPublishedOn("20160112T12:07");
        udm.save(Resource.Type.DOCUMENT).with(document1);
        udm.attachFrom(source.getUri()).to(document1.getUri()).by(Relation.Type.SOURCE_PROVIDES_DOCUMENT, RelationProperties.builder().date(timeGenerator.asISO()).build());
        udm.attachFrom(domain.getUri()).to(document1.getUri()).by(Relation.Type.DOMAIN_CONTAINS_DOCUMENT, RelationProperties.builder().date(timeGenerator.asISO()).build());

        // -> Item 1 from 1
        Item item11 = new Item();
        item11.setUri(uriGenerator.newFor(Resource.Type.ITEM));
        item11.setCreationTime(timeGenerator.asISO());
        udm.save(Resource.Type.ITEM).with(item11);
        udm.attachFrom(document1.getUri()).to(item11.getUri()).by(Relation.Type.DOCUMENT_BUNDLES_ITEM,RelationProperties.builder().date(timeGenerator.asISO()).build());

        // -> Item 2 from 1
        Item item12 = new Item();
        item12.setUri(uriGenerator.newFor(Resource.Type.ITEM));
        item12.setCreationTime(timeGenerator.asISO());
        udm.save(Resource.Type.ITEM).with(item12);
        udm.attachFrom(document1.getUri()).to(item12.getUri()).by(Relation.Type.DOCUMENT_BUNDLES_ITEM,RelationProperties.builder().date(timeGenerator.asISO()).build());

        // Document 2
        Document document2 = new Document();
        document2.setUri(uriGenerator.newFor(Resource.Type.DOCUMENT));
        document2.setCreationTime(timeGenerator.asISO());
        document2.setTitle("title-2");
        document2.setPublishedOn("20160112T12:07");
        udm.save(Resource.Type.DOCUMENT).with(document2);
        udm.attachFrom(source.getUri()).to(document2.getUri()).by(Relation.Type.SOURCE_PROVIDES_DOCUMENT, RelationProperties.builder().date(timeGenerator.asISO()).build());
        udm.attachFrom(domain.getUri()).to(document2.getUri()).by(Relation.Type.DOMAIN_CONTAINS_DOCUMENT, RelationProperties.builder().date(timeGenerator.asISO()).build());

        // -> Item 1 from 2
        Item item21 = new Item();
        item21.setUri(uriGenerator.newFor(Resource.Type.ITEM));
        item21.setCreationTime(timeGenerator.asISO());
        udm.save(Resource.Type.ITEM).with(item21);
        udm.attachFrom(document2.getUri()).to(item21.getUri()).by(Relation.Type.DOCUMENT_BUNDLES_ITEM,RelationProperties.builder().date(timeGenerator.asISO()).build());



        // -> Item 2 from 2
        Item item22 = new Item();
        item22.setUri(uriGenerator.newFor(Resource.Type.ITEM));
        item22.setCreationTime(timeGenerator.asISO());
        udm.save(Resource.Type.ITEM).with(item22);
        udm.attachFrom(document2.getUri()).to(item22.getUri()).by(Relation.Type.DOCUMENT_BUNDLES_ITEM,RelationProperties.builder().date(timeGenerator.asISO()).build());


        // Creators
        User user = new User();
        user.setUri("http://epnoi.org/users/833a8399-29b5-4e75-9118-d434d9f0273e");
        List<User> creators = Arrays.asList(new User[]{user});


        List<RegularResource> rrs = new ArrayList<>();
        rrs.add(regularResourceBuilder.from(item11.getUri(),document1.getTitle(), document1.getPublishedOn(), creators, "house place forest"));
        rrs.add(regularResourceBuilder.from(item12.getUri(),document1.getTitle(), document1.getPublishedOn(), creators, "home joy forest"));
        rrs.add(regularResourceBuilder.from(item21.getUri(),document2.getTitle(), document2.getPublishedOn(), creators, "house person forest"));
        rrs.add(regularResourceBuilder.from(item22.getUri(),document2.getTitle(), document2.getPublishedOn(), creators, "house play home"));

        TopicModel model = topicModelBuilder.build(domain.getUri(), rrs);
        String creationTime = timeGenerator.asISO();


        LOG.info("Configuration: " + model.getConfiguration());


        Map<String,String> topicTable = new HashMap<>();
        for (TopicData topicData : model.getTopics()){

            // Save Topic
            Topic topic = new Topic();
            topic.setUri(uriGenerator.newFor(Resource.Type.TOPIC));
            topic.setAnalysis(analysis.getUri());
            topic.setCreationTime(creationTime);
            topic.setContent("content");
            udm.save(Resource.Type.TOPIC).with(topic);
            udm.attachFrom(topic.getUri()).to(domain.getUri()).by(Relation.Type.TOPIC_EMERGES_IN_DOMAIN, RelationProperties.builder().date(timeGenerator.asISO()).description(analysis.getUri()).build());

            topicTable.put(topicData.getId(),topic.getUri());

            // Relate it to Words
            for (WordDistribution wordDistribution : topicData.getWords()){

                List<String> result = udm.find(Resource.Type.WORD).by(Word.LEMMA, wordDistribution.getWord());
                String wordURI;
                if (result != null && !result.isEmpty()){
                    wordURI = result.get(0);
                }else {
                    wordURI = uriGenerator.newFor(Resource.Type.WORD);

                    // Create Word
                    Word word = new Word();
                    word.setUri(wordURI);
                    word.setLemma(wordDistribution.getWord());
                    udm.save(Resource.Type.WORD).with(word);

                }

                // Relate Topic to Word (mentions)
                udm.attachFrom(topic.getUri()).to(wordURI).by(Relation.Type.TOPIC_MENTIONS_WORD,RelationProperties.builder().weight(wordDistribution.getWeight()).build());

            }
        }


        Set<String> resourceURIs = model.getResources().keySet();
        for (String resourceURI: resourceURIs){

            for (TopicDistribution topicDistribution: model.getResources().get(resourceURI)){
                // Relate resource (Item) to Topic
                String topicURI = topicTable.get(topicDistribution.getTopic());
                udm.attachFrom(resourceURI).to(topicURI).by(Relation.Type.ITEM_DEALS_WITH_TOPIC,RelationProperties.builder().weight(topicDistribution.getWeight()).build());

            }
        }
        LOG.info("Model built and saved: " + model);

    }

}
