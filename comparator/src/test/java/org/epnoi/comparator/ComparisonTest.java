package org.epnoi.comparator;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.domain.relations.DealsWithFromDocument;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.*;
import org.epnoi.model.modules.EventBus;
import org.epnoi.storage.UDM;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cbadenes on 13/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {"epnoi.comparator.delay = 1000"})
public class ComparisonTest {

    private static final Logger LOG = LoggerFactory.getLogger(ComparisonTest.class);

    @Autowired
    EventBus eventBus;

    @Autowired
    UDM udm;

    @Test
    public void simulate() throws InterruptedException {

        udm.delete(Resource.Type.ANY).all();

        // Domain
        Domain domain = Resource.newDomain();
        domain.setUri("domain/1");
        udm.save(domain);

        // Analysis
        Analysis analysis = Resource.newAnalysis();
        analysis.setUri("analysis/1");
        analysis.setType("Topic-Model");
        analysis.setDomain(domain.getUri());
        analysis.setDescription("document");

        // Topic at Document Level
        List<Topic> topics = new ArrayList<>();
        for (int i = 0; i < 2 ; i ++){
            topics.add(createTopic("topic/"+i,domain.getUri(),analysis.getUri()));
        }

        // Documents
        for (int i = 0; i < 5 ; i ++){
            Document document = createDocument("document/"+i,domain.getUri());
            for (Topic topic: topics){
                DealsWithFromDocument deals = Relation.newDealsWithFromDocument(document.getUri(), topic.getUri());
                deals.setWeight(Double.valueOf(1.0/topics.size()));
                udm.save(deals);
            }
        }

        udm.save(analysis);
        //eventBus.post(Event.from(analysis), RoutingKey.of(Resource.Type.ANALYSIS, Resource.State.CREATED));

        LOG.info("Sleeping..");
        Thread.sleep(60000);
        LOG.info("Wake Up..");


    }

    private Topic createTopic(String uri,String domain,String analysis){
        Topic topic = new Topic();
        topic.setUri(uri);
        udm.save(topic);

        udm.save(Relation.newEmergesIn(topic.getUri(),domain));
        return topic;
    }

    private Document createDocument(String uri, String domain){
        Document document = new Document();
        document.setUri(uri);
        udm.save(document);
        udm.save(Relation.newContains(domain,document.getUri()));
        return document;
    }

}
