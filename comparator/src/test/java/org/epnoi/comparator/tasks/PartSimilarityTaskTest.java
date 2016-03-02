package org.epnoi.comparator.tasks;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.comparator.Config;
import org.epnoi.comparator.helper.ComparatorHelper;
import org.epnoi.model.domain.resources.Analysis;
import org.epnoi.model.domain.resources.Resource;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by cbadenes on 02/03/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {
        "epnoi.comparator.delay = 1000",
        "epnoi.cassandra.contactpoints = drinventor.dia.fi.upm.es",
        "epnoi.cassandra.port = 5011",
        "epnoi.cassandra.keyspace = research",
        "epnoi.elasticsearch.contactpoints = drinventor.dia.fi.upm.es",
        "epnoi.elasticsearch.port = 5021",
        "epnoi.neo4j.contactpoints = drinventor.dia.fi.upm.es",
        "epnoi.neo4j.port = 5030",
        "epnoi.eventbus.host = drinventor.dia.fi.upm.es"})
public class PartSimilarityTaskTest {

    @Autowired
    ComparatorHelper helper;

    @Test
    public void simulate(){

        Analysis analysis = Resource.newAnalysis();
        analysis.setDomain("http://drinventor.eu/domains/7df34748-7fad-486e-a799-3bcd86a03499");


        PartSimilarityTask task = new PartSimilarityTask(analysis,helper);
        task.run();

    }
}
