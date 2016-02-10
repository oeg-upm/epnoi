package org.epnoi.knowledgebase.wikidata;

import org.epnoi.model.RelationHelper;
import org.epnoi.model.WikidataView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by cbadenes on 10/02/16.
 */
public class WikidataHandlerBuilderTest {

    private static final Logger LOG = LoggerFactory.getLogger(WikidataHandlerBuilderTest.class);


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
