package org.epnoi.knowledgebase;

import es.cbadenes.lab.test.IntegrationTest;
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

/**
 * Created by cbadenes on 10/02/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {
        "epnoi.knowledgeBase.wordnet.considered = true",
        "epnoi.knowledgeBase.wordnet.dictionaryPath = /Users/cbadenes/Tools/drinventor/wordnet/dictWN3.1",
        "epnoi.knowledgeBase.wikidata.considered = true",
        "epnoi.knowledgeBase.wikidata.timeout = 100",
        "epnoi.knowledgeBase.wikidata.offline = true",
        "epnoi.knowledgeBase.wikidata.mode = load",
        "epnoi.knowledgeBase.wikidata.inMemory = false",
        "epnoi.knowledgeBase.wikidata.dump.mode = json",
        "epnoi.knowledgeBase.wikidata.dump.path = /Users/cbadenes/Tools/drinventor/wikidata",
        "epnoi.knowledgeBase.wikidata.uri = http://www.epnoi.org/wikidataView"})
public class KnowledgebaseFactoryTest {

    private static final Logger LOG = LoggerFactory.getLogger(KnowledgebaseFactoryTest.class);

    @Autowired
    KnowledgeBaseFactory knowledgeBaseFactory;

    @Test
    public void basic() throws EpnoiInitializationException {
        LOG.info("Starting the Knowledge Base test!!");

        KnowledgeBase curatedRelationsTable = knowledgeBaseFactory.build();
        LOG.info("Testing for dog-canine-------------------------------------------------------");
        LOG.info(""+curatedRelationsTable.areRelated("dog", "canrine", RelationHelper.HYPERNYMY));

        LOG.info("Testing for dogs-canine-------------------------------------------------------");
        LOG.info(""+curatedRelationsTable.areRelated("dogs", "canine",RelationHelper.HYPERNYMY));

        LOG.info("Testing for dog-canines-------------------------------------------------------");
        LOG.info(""+curatedRelationsTable.areRelated("dog", "canines ",RelationHelper.HYPERNYMY));

        LOG.info("Starting the CuratedRelationsTableCreator test!!");
    }
}
