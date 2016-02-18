package org.epnoi.learner;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.Domain;
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
 * Created by cbadenes on 10/02/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {"epnoi.learner.delay = 2000"})
public class OntologyLearningWorkflowTest {

    private static final Logger LOG = LoggerFactory.getLogger(OntologyLearningWorkflowTest.class);

    @Autowired
    OntologyLearningWorkflow ontologyLearningWorkflow;

    @Test
    public void basic(){
        System.out.println("Starting the Ontology Learning Process!");

        Domain domain = null;
        ontologyLearningWorkflow.execute();
        System.out.println("Ending the Ontology Learning Process!");
    }
}
