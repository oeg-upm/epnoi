package org.epnoi.learner.relations.patterns.lexical;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.learner.Config;
import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.model.exceptions.EpnoiInitializationException;
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
 * Created by cbadenes on 19/02/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {"epnoi.learner.delay = 2000"})
public class LexicalRelationalModelTesterTest {

    private static final Logger LOG = LoggerFactory.getLogger(LexicalRelationalModelTesterTest.class);

    @Autowired
    LearningHelper helper;


    @Test
    public void createModel(){
        LexicalRelationalModelTester modelTester = new LexicalRelationalModelTester();
        try {
            modelTester.init(helper);
        } catch (EpnoiInitializationException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        modelTester.test();
        LOG.info("Ending the Lexical Relational Model creation");
    }

}
