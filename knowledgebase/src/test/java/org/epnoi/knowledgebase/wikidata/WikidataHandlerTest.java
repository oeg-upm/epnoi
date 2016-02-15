package org.epnoi.knowledgebase.wikidata;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.knowledgebase.Config;
import org.epnoi.knowledgebase.wikidata.ddbb.InDatabaseWikidataHandler;
import org.epnoi.knowledgebase.wikidata.memory.InMemoryWikidataHandler;
import org.epnoi.model.RelationHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

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
public class WikidataHandlerTest {

    private static final Logger LOG = LoggerFactory.getLogger(WikidataHandlerTest.class);

    @Autowired
    WikidataHandler wikidataHandler;

    @Test
    public void inMemory(){
        LOG.info("Starting the test..");

        Assert.assertTrue(wikidataHandler instanceof InMemoryWikidataHandler);

        Set<String> related = wikidataHandler.getRelated("dog", RelationHelper.HYPERNYMY);

        LOG.info("Related terms: " + related);
    }


    @Test
    public void inDDBB(){
        LOG.info("Starting the test..");

        Assert.assertTrue(wikidataHandler instanceof InDatabaseWikidataHandler);

        Set<String> related = wikidataHandler.getRelated("dog", RelationHelper.HYPERNYMY);

        LOG.info("Related terms: " + related);
    }
}
