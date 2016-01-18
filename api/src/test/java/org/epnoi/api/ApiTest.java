package org.epnoi.api;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.api.services.SourceService;
import org.epnoi.storage.model.Source;
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
 * Created by cbadenes on 18/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class ApiTest {

    private static final Logger LOG = LoggerFactory.getLogger(ApiTest.class);


    @Autowired
    SourceService sourceService;

    @Test
    public void rest() throws InterruptedException {
        Source source = sourceService.get("http://epnoi.org/sources/f0a820bd-1335-48a1-afe6-f73a6c3baf54");
        System.out.println(source);


    }
}
