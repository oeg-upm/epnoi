package org.epnoi.learner.relations;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.learner.Config;
import org.epnoi.learner.LearningHelper;
import org.epnoi.model.Domain;
import org.epnoi.model.RelationHelper;
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

/**
 * Created by cbadenes on 10/02/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {"epnoi.learner.delay = 2000"})
public class RelationsHandlerTest {

    private static final Logger LOG = LoggerFactory.getLogger(RelationsHandlerTest.class);

    @Autowired
    org.epnoi.learner.helper.LearningHelper helper;

    @Test
    public void basic(){
        System.out.println("Starting the RelationsHandler test!");


        String domainURI = "http://CGTestCorpus";

        Domain domain = null;


        //TODO
        LOG.error("pending to implement by using UDM");
//        if (core.getInformationHandler().contains(domainURI,
//                RDFHelper.DOMAIN_CLASS)) {
//            domain = (Domain) core.getInformationHandler().get(domainURI,
//                    RDFHelper.DOMAIN_CLASS);
//        } else {
//            domain = new Domain();
//            domain.setLabel("CGTestCorpus");
//            domain.setUri(domainURI);
//            domain.setType(RDFHelper.PAPER_CLASS);
//        }

        // List<Domain> consideredDomains = Arrays.asList(domain);

        ArrayList<Domain> consideredDomains = new ArrayList<Domain>();
        String targetDomain = domainURI;

        Double hyperymExpansionMinimumThreshold = 0.7;
        Double hypernymExtractionMinimumThresohold = 0.1;
        boolean extractTerms = true;
        Integer numberInitialTerms = 10;
        String hypernymsModelPath = "/opt/epnoi/epnoideployment/firstReviewResources/lexicalModel/model.bin";

        // First of all we initialize the KnowledgeBase


        RelationsHandlerParameters relationsHandlerParameters = new RelationsHandlerParameters();


        relationsHandlerParameters.setParameter(
                RelationsHandlerParameters.CONSIDERED_DOMAINS,
                consideredDomains);

        LearningHelper learningHelper = new LearningHelper();
        learningHelper.setParameter(
                LearningHelper.CONSIDERED_DOMAINS,
                consideredDomains);

        learningHelper.setParameter(
                LearningHelper.TARGET_DOMAIN_URI, targetDomain);
        learningHelper
                .setParameter(
                        LearningHelper.HYPERNYM_RELATION_EXPANSION_THRESHOLD,
                        hyperymExpansionMinimumThreshold);

        learningHelper
                .setParameter(
                        LearningHelper.HYPERNYM_RELATION_EXTRACTION_THRESHOLD,
                        hyperymExpansionMinimumThreshold);
        learningHelper.setParameter(
                LearningHelper.EXTRACT_TERMS, extractTerms);
        learningHelper.setParameter(
                LearningHelper.NUMBER_INITIAL_TERMS,
                numberInitialTerms);

        learningHelper.setParameter(
                LearningHelper.HYPERNYM_MODEL_PATH,
                hypernymsModelPath);

        RelationsHandler relationsHandler = new RelationsHandler();
        try {

            relationsHandler.init(helper, relationsHandlerParameters);

        } catch (EpnoiInitializationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Are related? "
                + relationsHandler.areRelated("dog", "canine",
                RelationHelper.HYPERNYMY, "http://whatever"));
        System.out.println("Are related? "
                + relationsHandler.areRelated("cats", "canine",
                RelationHelper.HYPERNYMY, "http://whatever"));
        System.out.println("The strange EEUU case");
        System.out.println("Are related? "
                + relationsHandler.areRelated("EEUU", "country",
                RelationHelper.HYPERNYMY, "http://whatever"));
        System.out.println("The strange Spain case");
        System.out.println("Are related? "
                + relationsHandler.areRelated("Spain", "country",
                RelationHelper.HYPERNYMY, "http://whatever"));
        System.out.println("Finally the dog and cat problem");
        System.out.println("Are related? "
                + relationsHandler.areRelated("dog", "animal",
                RelationHelper.HYPERNYMY, "http://whatever"));

        System.out.println("Ending the RelationsHandler Process!");
    }
}
