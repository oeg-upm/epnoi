package org.epnoi.learner.modules;

import org.epnoi.learner.Config;
import org.epnoi.learner.relations.corpus.parallel.RelationalSentencesCorpusCreator;
import org.epnoi.learner.relations.patterns.RelationalPatternsModelCreator;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by rgonza on 14/11/15.
 */
@Component
public class Trainer {
    private static final Logger LOG = LoggerFactory.getLogger(Config.class);

    @Autowired
    @Qualifier("lexicalPatternsModelCreator")
    RelationalPatternsModelCreator lexicalRelationalPatternsModelCreator;

    @Autowired
    RelationalSentencesCorpusCreator relationalSentencesCorpusCreator;


    @PostConstruct
    public void init() throws EpnoiInitializationException {
        LOG.info("Initializing the Trainer");

    }

    public void createRelationalSentencesCorpus() {
        LOG.info("Creating the relational sentences corpus");
        try {
            this.relationalSentencesCorpusCreator.createCorpus();
        } catch (Exception e) {
            LOG.error("There was a problem creating the relational sentences corpus",e);
        }
    }


    public void createRelationalPatternsModel() {
        try {
            this.lexicalRelationalPatternsModelCreator.create();
        } catch (Exception e) {
            LOG.error("There was a problem in the creation of the relational pattern model",e);
        }
    }
}
