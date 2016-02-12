package org.epnoi.knowledgebase;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
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
        "epnoi.knowledgeBase.wikidata.mode = create",
        "epnoi.knowledgeBase.wikidata.inMemory = false",
        "epnoi.knowledgeBase.wikidata.dump.mode = json",
        "epnoi.knowledgeBase.wikidata.dump.path = /Users/cbadenes/Tools/drinventor/wikidata",
        "epnoi.knowledgeBase.wikidata.uri = http://www.epnoi.org/wikidataView"})
public class KnowledgeBaseTest {

    private static final Logger LOG = LoggerFactory.getLogger(KnowledgeBaseTest.class);

    @Autowired
    KnowledgeBaseHandler knowledgeBaseHandler;

    @Test
    public void basic() {

//        try {
//            KnowledgeBase knowledgeBase = null;
//            try {
//                knowledgeBase =knowledgeBaseHandler.getKnowledgeBase();
//            } catch (EpnoiResourceAccessException e) { // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (EpnoiInitializationException e) {
//                e.printStackTrace();
//            }
//
//            LOG.info("100> " + knowledgeBase.getWikidataHandler().getRelated(knowledgeBase.getWikidataHandler().stem("madrid"),
//                     RelationHelper.HYPERNYMY));
//
//        } catch (EpnoiInitializationException e) {
//
//            e.printStackTrace();
//        }
//
//
//        CassandraInformationStore cis = ((CassandraInformationStore) core
//                .getInformationStoresByType(InformationStoreHelper.
//                        CASSANDRA_INFORMATION_STORE).get(0));
//        cis.getQueryResolver().getWith(
//                "http://www.epnoi.org/wikidataView/relations/" + RelationHelper.HYPERNYMY,
//                "WikidataViewCorpus", "Q2807");
//
//
//        CassandraInformationStore cis = ((CassandraInformationStore) core
//                .getInformationStoresByType(InformationStoreHelper.
//                        CASSANDRA_INFORMATION_STORE).get(0));
//
//        LOG.info(cis.getQueryResolver().getValues(
//                "http://www.epnoi.org/wikidataView/relations/" + RelationHelper.HYPERNYMY,
//                "Q2807", WikidataViewCassandraHelper.COLUMN_FAMILY));
//        LOG.info(cis.getQueryResolver().getValues(
//                "http://www.epnoi.org/wikidataView/reverseDictionary", "Q2807",
//                WikidataViewCassandraHelper.COLUMN_FAMILY));
    }

}
