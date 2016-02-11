package org.epnoi.modeler;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.Event;
import org.epnoi.model.domain.*;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.RoutingKey;
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

/**
 * Created by cbadenes on 13/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {"epnoi.modeler.delay = 2000"})
public class ModelingTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModelingTest.class);

    @Autowired
    EventBus eventBus;

    @Autowired
    UDM udm;


    @Test
    public void run() throws InterruptedException {
        LOG.info("Sleepping...");
        Thread.sleep(120000);
        LOG.info("Wake Up!");
    }


    @Test
    public void simulate() throws InterruptedException {
        Domain domain = new Domain();
        domain.setUri("http://epnoi.org/domains/1f02ae0b-7d96-42c6-a944-25a3050bf1e2");
        domain.setName("test-domain");

        eventBus.post(Event.from(domain), RoutingKey.of(Resource.Type.DOMAIN, Resource.State.UPDATED));

        LOG.info("Sleeping..");
        Thread.sleep(300000);
        LOG.info("Wake Up..");

    }


    @Test
    public void saveTopic(){

        String domainURI    = "http://epnoi.org/domains/9d3cda8b-06ed-4ca8-bb10-9f1775a6077b";
        String analysisURI  = "http://epnoi.org/analyses/55269e40-e839-43b9-a535-43f94faec08d";

        Topic topic = new Topic();
        topic.setUri("http://epnoi.org/topics/2217f111-50c7-40da-8a0f-64a1a9db14af");
        topic.setContent("content");
        topic.setCreationTime("2016-01-14T09:37+0100");
        topic.setAnalysis(analysisURI);

        udm.save(Resource.Type.TOPIC).with(topic);
        udm.attachFrom(topic.getUri()).to(domainURI).by(Relation.Type.TOPIC_EMERGES_IN_DOMAIN, RelationProperties.builder().description(analysisURI).build());



    }
}
