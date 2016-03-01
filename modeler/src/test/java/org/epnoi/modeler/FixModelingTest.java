package org.epnoi.modeler;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.Event;
import org.epnoi.model.domain.relations.EmergesIn;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Domain;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.domain.resources.Topic;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.RoutingKey;
import org.epnoi.modeler.helper.ModelingHelper;
import org.epnoi.modeler.models.topic.TopicModeler;
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

import java.util.List;

/**
 * Created by cbadenes on 13/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {
        "epnoi.comparator.delay = 1000",
        "epnoi.cassandra.contactpoints = zavijava.dia.fi.upm.es",
        "epnoi.cassandra.port = 5011",
        "epnoi.cassandra.keyspace = research",
        "epnoi.elasticsearch.contactpoints = zavijava.dia.fi.upm.es",
        "epnoi.elasticsearch.port = 5021",
        "epnoi.neo4j.contactpoints = zavijava.dia.fi.upm.es",
        "epnoi.neo4j.port = 5032",
        "epnoi.eventbus.uri = amqp://epnoi:drinventor@drinventor.dia.fi.upm.es:5041/drinventor"})
public class FixModelingTest {

    private static final Logger LOG = LoggerFactory.getLogger(FixModelingTest.class);

    @Autowired
    ModelingHelper helper;

    @Autowired
    UDM udm;


    @Test
    public void topicModel() throws InterruptedException {

        String domainUri = "http://drinventor.eu/domains/7df34748-7fad-486e-a799-3bcd86a03499";
        new TopicModeler(domainUri,helper).run();

    }



}
