package org.epnoi.knowledgebase;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
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
@TestPropertySource(properties = {
        "epnoi.knowledgeBase.wordnet.considered = true",
        "epnoi.knowledgeBase.wordnet.dictionaryPath = /opt/epnoi/epnoideployment/wordnet/dictWN3.1",
        "epnoi.knowledgeBase.wikidata.considered = true",
        "epnoi.knowledgeBase.wikidata.timeout = 100",
        "epnoi.knowledgeBase.wikidata.offline = true",
        "epnoi.knowledgeBase.wikidata.mode = create",
        "epnoi.knowledgeBase.wikidata.inMemory = false",
        "epnoi.knowledgeBase.wikidata.dump.mode = json",
        "epnoi.knowledgeBase.wikidata.dump.path = /opt/epnoi/epnoideployment/wikidata",
        "epnoi.knowledgeBase.wikidata.uri = http://www.epnoi.org/wikidataView"})
public class KnowledgeBaseHandlerTest {

    @Autowired
    KnowledgeBaseHandler knowledgeBaseHandler;

    @Test
    public void basic(){
        try {
            System.out.println("-> "+knowledgeBaseHandler.getKnowledgeBase().areRelated("depeche mode", "band", RelationHelper.HYPERNYMY));
        } catch (EpnoiInitializationException e) {
            e.printStackTrace();
        } catch (EpnoiResourceAccessException e) {
            e.printStackTrace();
        }
    }
}
