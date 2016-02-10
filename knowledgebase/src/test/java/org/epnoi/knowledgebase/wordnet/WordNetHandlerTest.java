package org.epnoi.knowledgebase.wordnet;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.knowledgebase.Config;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Set;

/**
 * Created by cbadenes on 10/02/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {
        "epnoi.knowledgeBase.wordnet.dictionaryPath = /epnoi/epnoideployment/wordnet/dictWN40K/dict"})
public class WordNetHandlerTest {

    private static final Logger LOG = LoggerFactory.getLogger(WordNetHandlerTest.class);

    @Autowired
    WordNetHandler wordNetHandler;

    @Test
    public void basic(){
        LOG.info("Starting WordNetHandlerTest");

        LOG.info("Testing for dog--------------------------------------------------------");
        LOG.info(""+wordNetHandler.getNounFirstMeaningHypernyms("dog"));

        LOG.info("Testing for lion--------------------------------------------------------");
        LOG.info(""+wordNetHandler.getNounFirstMeaningHypernyms("lion"));

        LOG.info("Testing for lions--------------------------------------------------------");
        LOG.info(""+wordNetHandler.getNounFirstMeaningHypernyms("lions"));

        LOG.info("Testing the get nouns function");
        long t = System.currentTimeMillis();

        LOG.info("These are the nouns ");
        int count = 0;
        int hypernymsTotal = 0;
        List<String> nouns = wordNetHandler.getNouns();
        for (String noun : nouns) {
            Set<String> hypernyms = wordNetHandler.getNounFirstMeaningHypernyms(noun);
            LOG.info(":::> " + noun + " -> " + hypernyms);
            if (hypernyms.size() > 0) {
                count = count + 1;
                hypernymsTotal += hypernyms.size();
            }
        }
        LOG.info("There are " + nouns.size() + " nouns");
        LOG.info("About " + ((double) count)
                / ((double) nouns.size()) + " have hypernyms defined");
        LOG.info("With an average of " + ((double) hypernymsTotal)
                / ((double) nouns.size()) + "hypernyms each");
        long time = System.currentTimeMillis() - t;
        System.out.printf(" done in " + String.valueOf(time));
        LOG.info("Ending WordNetHandlerTest");
    }

}
