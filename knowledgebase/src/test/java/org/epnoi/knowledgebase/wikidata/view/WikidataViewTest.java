package org.epnoi.knowledgebase.wikidata.view;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.knowledgebase.Config;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.WikidataView;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

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
public class WikidataViewTest {

    private static final Logger LOG = LoggerFactory.getLogger(WikidataViewTest.class);


    @Test
    public void basic(){
        //System.out.println("Creating the dictionary ");
        Map<String, Set<String>> labelsDictionary = new HashMap<>();
        Map<String, Set<String>> labelsReverseDictionary = new HashMap<>();


        Map<String, Map<String, Set<String>>> relations = new HashMap<>();
        Map<String, Set<String>> hypernymRelations = new HashMap<>();
        Set<String> destionationSet = new HashSet<String>();
        destionationSet.add("http://testTargetA");
        destionationSet.add("http://testTargetB");
        hypernymRelations.put("http://testSource", destionationSet);
        relations.put(RelationHelper.HYPERNYMY, hypernymRelations);



        Set<String> labelDictionary = new HashSet<String>();
        labelDictionary.add("http://testTargetA");
        //labelDictionary.add("http://testTargetB");
        labelsDictionary.put("target label", labelDictionary);
        labelsDictionary.put("source label", new HashSet<String>(Arrays.asList("http://testSource")));


        labelsReverseDictionary.put("http://testSource", new HashSet<String>(Arrays.asList("source label")));

        labelsReverseDictionary.put("http://testTargetA", new HashSet<String>(Arrays.asList("target label")));

        // WikidataHandlerParameters.DEFAULT_URI
        String defaultUri = "http:/sample";

        WikidataView wikidataView = new WikidataView(
                defaultUri, labelsDictionary,
                labelsReverseDictionary, relations);

        LOG.info(""+wikidataView);
    }
}
