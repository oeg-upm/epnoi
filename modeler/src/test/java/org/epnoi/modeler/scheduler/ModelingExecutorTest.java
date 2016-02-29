package org.epnoi.modeler.scheduler;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.domain.resources.Document;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.modeler.Config;
import org.epnoi.modeler.helper.ModelingHelper;
import org.epnoi.model.domain.resources.Domain;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by cbadenes on 11/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class ModelingExecutorTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModelingExecutorTest.class);

    @Autowired
    ModelingHelper helper;

    @Test
    public void onlyOneTime() throws InterruptedException {

        Document document = Resource.newDocument();
        document.setUri("http://drinventor.eu/documents/c369c917fecf3b4828688bdb6677dd6e");

        ModelingPoolExecutor modelingExecutor = new ModelingPoolExecutor(document,helper,5000);

        modelingExecutor.buildModel();
        Thread.sleep(1000);
        modelingExecutor.buildModel();
        Thread.sleep(1000);
        modelingExecutor.buildModel();
        Thread.sleep(1000);

        LOG.info("Waiting for delayed executions...");
        Thread.sleep(5000);

    }

}
