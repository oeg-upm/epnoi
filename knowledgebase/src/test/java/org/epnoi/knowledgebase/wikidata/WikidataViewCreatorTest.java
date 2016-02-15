package org.epnoi.knowledgebase.wikidata;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.knowledgebase.Config;
import org.epnoi.knowledgebase.wikidata.view.WikidataViewCreator;
import org.epnoi.model.WikidataView;
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
@TestPropertySource(properties = {
        "epnoi.knowledgeBase.wordnet.considered = true",
        "epnoi.knowledgeBase.wordnet.dictionaryPath = /Users/cbadenes/Tools/drinventor/wordnet/dictWN3.1",
        "epnoi.knowledgeBase.wikidata.timeout = 100",
        "epnoi.knowledgeBase.wikidata.offline = true",
        "epnoi.knowledgeBase.wikidata.mode = create",
        "epnoi.knowledgeBase.wikidata.inMemory = false",
        "epnoi.knowledgeBase.wikidata.dump.mode = json",
        "epnoi.knowledgeBase.wikidata.dump.path = /Users/cbadenes/Tools/drinventor/wikidata",
        "epnoi.knowledgeBase.wikidata.uri = http://www.epnoi.org/wikidataView"})
public class WikidataViewCreatorTest {

    private static final Logger LOG = LoggerFactory.getLogger(WikidataViewCreatorTest.class);

    @Autowired
    WikidataViewCreator wikidataViewCreator;

    @Test
    public void basic(){
        LOG.info("Starting the WikidataViewCreator");

        WikidataView wikidataView = wikidataViewCreator.create();

        LOG.info("Ending the WikidataViewCreator: " + wikidataView);
    }
}
