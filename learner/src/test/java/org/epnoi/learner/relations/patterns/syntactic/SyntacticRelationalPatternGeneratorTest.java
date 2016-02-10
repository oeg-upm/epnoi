package org.epnoi.learner.relations.patterns.syntactic;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.learner.Config;
import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.learner.relations.corpus.MockUpRelationalSentencesCorpusCreator;
import org.epnoi.learner.relations.patterns.RelationalPattern;
import org.epnoi.model.RelationalSentence;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by cbadenes on 10/02/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {"epnoi.learner.delay = 2000"})
public class SyntacticRelationalPatternGeneratorTest {

    @Autowired
    LearningHelper helper;

    @Test
    public void basic() throws EpnoiInitializationException {
        SyntacticRelationalPatternGenerator patternGenerator = new SyntacticRelationalPatternGenerator();

        MockUpRelationalSentencesCorpusCreator corpusCreator = new MockUpRelationalSentencesCorpusCreator();
        corpusCreator.init(helper);

        for (RelationalSentence sentence : corpusCreator.createTestCorpus()
                .getSentences()) {
            List<RelationalPattern> patterns = patternGenerator
                    .generate(sentence);
            for (RelationalPattern pattern : patterns) {
                System.out.println("Patter:> " + pattern);
            }
        }
    }
}
