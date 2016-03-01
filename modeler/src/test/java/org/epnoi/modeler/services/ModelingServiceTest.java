package org.epnoi.modeler.services;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.domain.relations.Contains;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Document;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.modeler.Config;
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
 * Created by cbadenes on 11/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {"epnoi.modeler.delay = 5000"})
public class ModelingServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModelingServiceTest.class);

    @Autowired
    TopicModelingService service;

    @Test
    public void scheduleModelingTasks() throws InterruptedException {

        Contains contains = Relation.newContains("http://drinventor.eu/domains/7df34748-7fad-486e-a799-3bcd86a03499","http://drinventor.eu/documents/c369c917fecf3b4828688bdb6677dd6e");

        service.buildModels(contains);
        Thread.sleep(1000);
        service.buildModels(contains);
        service.buildModels(contains);
        Thread.sleep(1000);
        service.buildModels(contains);
        service.buildModels(contains);
        Thread.sleep(1000);

        LOG.info("waiting for execution....");
        Thread.sleep(10000);


    }
}
