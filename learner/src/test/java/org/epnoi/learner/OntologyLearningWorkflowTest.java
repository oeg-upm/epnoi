package org.epnoi.learner;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.model.Domain;
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

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by cbadenes on 10/02/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {"epnoi.learner.delay = 2000"})
public class OntologyLearningWorkflowTest {

    private static final Logger LOG = LoggerFactory.getLogger(OntologyLearningWorkflowTest.class);

    @Test
    public void basic(){
        System.out.println("Starting the Ontology Learning Process!");

        Domain domain = null;

        //TODO
        LOG.error("pending to implement by using UDM");
//        if (core.getInformationHandler().contains(OntologyLearningWorkflow.DOMAIN_URI,
//                RDFHelper.DOMAIN_CLASS)) {
//            domain = (Domain) core.getInformationHandler().get(OntologyLearningWorkflow.DOMAIN_URI,
//                    RDFHelper.DOMAIN_CLASS);
//        } else {
//            System.out.println("The target domian "+OntologyLearningWorkflow.DOMAIN_URI+ "couldn't be found in the UIA");
//            System.exit(0);
//        }

        ArrayList<Domain> consideredDomains = new ArrayList(Arrays.asList(domain));
        String targetDomain = OntologyLearningWorkflow.DOMAIN_URI;

        Double hyperymExpansionMinimumThreshold = 0.7;
        Double hypernymExtractionMinimumThresohold = 0.091;
        boolean extractTerms = true;
        Integer numberInitialTerms = 10;
        String hypernymsModelPath = "/opt/epnoi/epnoideployment/firstReviewResources/lexicalModel/model.bin";

        LearningParameters learningParameters = new LearningParameters();
        learningParameters.setParameter(
                LearningParameters.CONSIDERED_DOMAINS,
                consideredDomains);

        learningParameters.setParameter(
                LearningParameters.TARGET_DOMAIN_URI, targetDomain);
        learningParameters
                .setParameter(
                        LearningParameters.HYPERNYM_RELATION_EXPANSION_THRESHOLD,
                        hyperymExpansionMinimumThreshold);

        learningParameters
                .setParameter(
                        LearningParameters.HYPERNYM_RELATION_EXTRACTION_THRESHOLD,
                        hypernymExtractionMinimumThresohold);
        learningParameters.setParameter(
                LearningParameters.EXTRACT_TERMS, extractTerms);
        learningParameters.setParameter(
                LearningParameters.NUMBER_INITIAL_TERMS,
                numberInitialTerms);

        learningParameters.setParameter(
                LearningParameters.HYPERNYM_MODEL_PATH,
                hypernymsModelPath);
        learningParameters.setParameter(LearningParameters.CONSIDER_KNOWLEDGE_BASE, false);

        OntologyLearningWorkflow ontologyLearningProcess = new OntologyLearningWorkflow();

        try {
            ontologyLearningProcess.init(learningParameters);
        } catch (EpnoiInitializationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ontologyLearningProcess.execute();
        System.out.println("Ending the Ontology Learning Process!");
    }
}
