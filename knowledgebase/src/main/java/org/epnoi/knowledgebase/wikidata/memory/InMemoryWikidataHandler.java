package org.epnoi.knowledgebase.wikidata.memory;

import org.epnoi.knowledgebase.wikidata.WikidataHandler;
import org.epnoi.knowledgebase.wikidata.WikidataStemmer;
import org.epnoi.knowledgebase.wikidata.ddbb.WikidataViewStorer;
import org.epnoi.knowledgebase.wikidata.view.WikidataViewCreator;
import org.epnoi.model.WikidataView;
import org.epnoi.model.domain.Resource;
import org.epnoi.model.domain.SerializedObject;
import org.epnoi.storage.UDM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.Set;

/**
 * Created by cbadenes on 15/02/16.
 */
@Component
@Conditional(InMemoryCondition.class)
public class InMemoryWikidataHandler implements WikidataHandler {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryWikidataHandler.class);

    @Value("${epnoi.knowledgeBase.wikidata.uri}")
    String wikidataUri;

    @Autowired
    WikidataViewCreator wikidataViewCreator;

    @Autowired
    UDM udm;

    @Autowired
    WikidataStemmer wikidataStemmer;

    @Autowired
    WikidataViewStorer wikidataViewStorer;

    private WikidataView wikidataView;

    @PostConstruct
    public void setup(){

        if (wikidataViewCreator.hasToBeCreated()){
            LOG.info("Loading wikidataview from a new creation");
            wikidataView = wikidataViewCreator.create();
        } else{
            LOG.info("Loaded wikidataview from ddbb");
            wikidataView = wikidataViewStorer.load(wikidataUri);
        }
    }


    @Override
    public Set<String> getRelated(String source, String type) {
        return wikidataView.getRelated(source,type);
    }

    @Override
    public String stem(String word) {
        return wikidataStemmer.stem(word);
    }
}
