package org.epnoi.comparator;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.comparator.helper.ComparatorHelper;
import org.epnoi.comparator.tasks.DocumentSimilarityTask;
import org.epnoi.model.domain.relations.DealsWithFromDocument;
import org.epnoi.model.domain.relations.EmergesIn;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        "epnoi.eventbus.host = drinventor.dia.fi.upm.es"})
public class FixComparatorTest {

    private static final Logger LOG = LoggerFactory.getLogger(FixComparatorTest.class);

    @Autowired
    ComparatorHelper helper;

    @Autowired
    UDM udm;

    @Test
    public void docSimilarity() throws InterruptedException {



        Analysis analysis = new Analysis();
        analysis.setType("topic-model");
        analysis.setDomain("http://epnoi.org/domains/ce123683-512a-4f2a-a539-c77b666a8b79");
        analysis.setUri("http://epnoi.org/analyses/88130588-1dfc-48d8-8f3c-181e031b9ebf");


//        List<Relation> deals = udm.find(Relation.Type.DEALS_WITH_FROM_DOCUMENT).in(Resource.Type.DOMAIN, analysis.getDomain());
//
//        System.out.println(deals);

        DocumentSimilarityTask task = new DocumentSimilarityTask(analysis, helper);
        task.run();

    }

    @Test
    public void fixDocAndWords(){

        String domainUri = "http://epnoi.org/domains/ce123683-512a-4f2a-a539-c77b666a8b79";


        for (String uri: udm.find(Resource.Type.TOPIC).all()){
            Optional<Resource> res = udm.read(Resource.Type.TOPIC).byUri(uri);

            if (!res.isPresent()) throw new RuntimeException("No topic found with uri: " + uri);

            Topic topic = res.get().asTopic();

            EmergesIn emerges = Relation.newEmergesIn(uri, domainUri);
            emerges.setAnalysis(topic.getAnalysis());
            udm.save(emerges);
        }

        for (String uri: udm.find(Resource.Type.WORD).all()){
            udm.save(Relation.newEmbeddedIn(uri,domainUri));
        }
    }

}
