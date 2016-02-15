package org.epnoi.knowledgebase.wikidata.ddbb;

import org.epnoi.knowledgebase.wikidata.WikidataHandler;
import org.epnoi.knowledgebase.wikidata.WikidataStemmer;
import org.epnoi.knowledgebase.wikidata.view.WikidataViewCreator;
import org.epnoi.model.WikidataView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@Conditional(InDatabaseCondition.class)
public class InDatabaseWikidataHandler implements WikidataHandler {

    private static final Logger LOG = LoggerFactory.getLogger(InDatabaseWikidataHandler.class);


    @Value("${epnoi.knowledgeBase.wikidata.uri}")
    String wikidataUri;

    @Autowired
    WikidataStemmer stemmer;

    @Autowired
    WikidataViewCreator wikidataViewCreator;

    @Autowired
    WikidataViewStorer wikidataViewStorer;

    @PostConstruct
    public void setup(){
//        this.wikidataView = new CassandraWikidataView(wikidataUri);

        if (wikidataViewCreator.hasToBeCreated()){
            LOG.info("Save in DDBB a new wikidataview ");
            WikidataView wikidataView = wikidataViewCreator.create();
            wikidataViewStorer.save(wikidataView);
        } else{
            LOG.info("Wikidataview will be read from DDBB");
        }
    }

    @Override
    public synchronized String  stem(String term) {
        return this.stemmer.stem(term);
    }


    @Override
    public Set<String> getRelated(String sourceLabel, String type) {
        Set<String> targetLabels = new HashSet<String>();

        Map<String, Set<String>> consideredRelations = wikidataViewStorer.readRelation(wikidataUri,type);

        // Firstly we retrieve the IRIs

        Set<String> sourceIRIs = wikidataViewStorer.readLabel(wikidataUri,sourceLabel);
        //System.out.println("Inital sourceIRIs obtained from the label" + sourceIRIs);
        if (sourceIRIs != null) {

            for (String sourceIRI : sourceIRIs) {
                // System.out.println("sourceIRI " + sourceIRI);
                Set<String> targetIRIs = consideredRelations.get(sourceIRI);
                // System.out.println(" ("+sourceIRI+") targetIRIs " +
                // targetIRIs);
                if (targetIRIs != null) {
                    for (String targetIRI : targetIRIs) {
                        //	System.out.println(" trying > " + targetIRI);
                        // System.out.println("->
                        // "+this.getLabelsReverseDictionary().get(targetIRI).size());
                        if (targetIRI != null) {
                            Set<String> reverse = wikidataViewStorer.readReverseLabel(wikidataUri, targetIRI);
                            if (reverse != null) {
                                //	System.out.println("reverseDict " + this.labelsReverseDictionary);
                                for (String destinationTarget : reverse) {
                                    //	System.out.println("Destination target " + destinationTarget);
                                    targetLabels.add(destinationTarget);
                                }
                            }

                        }
                    }
                }
            }
        }


        return targetLabels;
    }

    @Override
    public String toString() {
        return "WikidataHandlerImpl [wikidataView=" + wikidataUri + "]";
    }

}
