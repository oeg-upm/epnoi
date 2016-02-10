package org.epnoi.learner.relations.patterns.syntactic;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.learner.Config;
import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.learner.relations.corpus.RelationalSentencesCorpusCreationParameters;
import org.epnoi.learner.relations.patterns.RelationalPatternsModelCreationParameters;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
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
public class SyntacticRelationalModelCreatorTest {

    @Autowired
    LearningHelper helper;

    @Test
    public void basic(){
        System.out.println("Starting the Syntactic Relational Model creation");
        RelationalPatternsModelCreationParameters parameters = new RelationalPatternsModelCreationParameters();
        parameters
                .setParameter(
                        SyntacticRelationalModelCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER,
                        "http://drInventorFirstReview/relationalSentencesCorpus");
        parameters
                .setParameter(
                        SyntacticRelationalModelCreationParameters.MAX_PATTERN_LENGTH_PARAMETER,
                        20);

        parameters
                .setParameter(
                        SyntacticRelationalModelCreationParameters.MODEL_PATH_PARAMETERS,
                        "/JUNK/model.bin");

        parameters
                .setParameter(
                        RelationalSentencesCorpusCreationParameters.STORE,
                        true);

        parameters.setParameter(
                RelationalSentencesCorpusCreationParameters.VERBOSE,
                false);

        SyntacticRelationalModelCreator modelCreator = new SyntacticRelationalModelCreator();
        try {

            modelCreator.init(helper,parameters);
        } catch (EpnoiInitializationException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        modelCreator.create();

        System.out.println("Ending the Syntantic Relational Model creation");
    }
}
