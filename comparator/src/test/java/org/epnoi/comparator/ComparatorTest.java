package org.epnoi.comparator;

import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by cbadenes on 11/02/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {"epnoi.comparator.delay = 2000"})
public class ComparatorTest {

    private static final Logger LOG = LoggerFactory.getLogger(ComparatorTest.class);


    @Test
    public void run() throws InterruptedException {
        LOG.info("Sleepping...");
        Thread.sleep(120000);
        LOG.info("Wake Up!");
    }
}
