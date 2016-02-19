package org.epnoi.learner.relations.patterns;

import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.learner.relations.corpus.MockUpRelationalSentencesCorpusCreator;
import org.epnoi.model.RelationalSentencesCorpus;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;


public class RelationalPatternsModelCreator {
    private static final Logger LOG = LoggerFactory.getLogger(RelationalPatternsModelCreator.class);

    @Autowired
    private LearningHelper helper;

    private final String type;

    @Value("${learner.corpus.sentences.uri}")
    String relationalSentencesCorpusURI;

    @Value("${learner.corpus.patterns.lexical.path}")
    String path;

    @Value("${learner.corpus.patterns.lexical.store}")
    boolean store;

    @Value("${learner.corpus.patterns.lexical.test}")
    boolean test;

    @Value("${learner.corpus.patterns.lexical.verbose}")
    boolean verbose;

    private RelationalSentencesCorpus relationalSentencesCorpus;

    private RelationalPatternsCorpusCreator patternsCorpusCreator;

    private RelationalPatternsCorpus patternsCorpus;

    private RelationalPatternsModelBuilder modelBuilder;

    private RelationalPatternsModel model;

    MockUpRelationalSentencesCorpusCreator relationSentencesCorpusCreator;

    public RelationalPatternsModelCreator(String type){
        this.type = type;
    }

    @PostConstruct
    public void init() throws EpnoiInitializationException, EpnoiResourceAccessException {
        LOG.info("Initializing the RelationalPatternsModelCreator with the following parameters: " + helper);

        this.patternsCorpusCreator = new RelationalPatternsCorpusCreator();

        RelationalPatternGenerator relationalPatternsGenerator = null;
        try {
            relationalPatternsGenerator = RelationalPatternsGeneratorFactory.build(type);
        } catch (EpnoiResourceAccessException exception) {
            throw new EpnoiInitializationException(exception.getMessage());
        }
        this.patternsCorpusCreator.init(relationalPatternsGenerator);

        this.relationSentencesCorpusCreator = new MockUpRelationalSentencesCorpusCreator();

        this.relationSentencesCorpusCreator.init(helper);

        this.modelBuilder = RelationalPatternsModelBuilderFactory.build(helper,type);

    }

    public void create() {
        LOG.info("Creating a relational patterns model with the following runtime parameters: " + this);
        _obtainPatternsCorpus();

        this.model = _createModel();
        if (this.verbose) {
            this.model.show();
        }
        if (this.store) {
            _storeModel();
        }
    }


    // ------------------------------------------------------------------------------------------------------------------------

    private void _storeModel() {

        LOG.info("Storing the model at " + this.path);

        try {
            RelationalPatternsModelSerializer.serialize(this.path, model);

        } catch (EpnoiResourceAccessException e) {
            LOG.error("There was a problem trying to serialize the patterns model at " + this.path,e);
        }

    }

    // ------------------------------------------------------------------------------------------------------------------------

    private void _obtainPatternsCorpus() {
        LOG.info("Obtaining the RelationalPatternsCorspus");

        _obtainRealtionalSentencesCorpus();
        if (relationalSentencesCorpus == null) {
            LOG.error("The RelationalSentecesCorpus was null, the model cannot be created!");
        } else {

            LOG.info("The RelationalSencentcesCorpus has "
                    + relationalSentencesCorpus.getSentences().size()
                    + " sentences");
            patternsCorpus = patternsCorpusCreator
                    .buildCorpus(relationalSentencesCorpus);

            LOG.info("The RelationalPatternsCorpus has "
                    + patternsCorpus.getPatterns().size() + " patterns");
        }
    }

    private void _obtainRealtionalSentencesCorpus() {
        if (this.test) {
            this.relationalSentencesCorpus = relationSentencesCorpusCreator.createTestCorpus();
        } else {
            this.relationalSentencesCorpus = _retrieveRelationalSentencesCorpus();
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------

    private RelationalSentencesCorpus _retrieveRelationalSentencesCorpus() {
        LOG.info("Retrieving the relational sentences corpus with the following uri " + relationalSentencesCorpusURI);

        //TODO
        LOG.error("Pending to implement by using UDM");
//        RelationalSentencesCorpus relationalSentencesCorpus = (RelationalSentencesCorpus) this.core
//                .getInformationHandler().get(relationalSentencesCorpusURI,
//                        RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);
        RelationalSentencesCorpus relationalSentencesCorpus = null;

        if (relationalSentencesCorpus == null) {
            LOG.info("The Relational Sentences Corpus "
                    + relationalSentencesCorpusURI + "could not be found");

        } else {

            LOG.info("The RelationalSencentcesCorpus has "
                    + relationalSentencesCorpus.getSentences().size()
                    + " sentences");
            patternsCorpus = patternsCorpusCreator
                    .buildCorpus(relationalSentencesCorpus);

            LOG.info("The RelationalPatternsCorpus has "
                    + patternsCorpus.getPatterns().size() + " patterns");
        }
        return relationalSentencesCorpus;
    }

    private RelationalPatternsModel _createModel() {
        long startingTime = System.currentTimeMillis();
        LOG.info("Adding all the patterns to the model");
        for (RelationalPattern pattern : patternsCorpus.getPatterns()) {
            this.modelBuilder.addPattern(pattern);
        }
        LOG.info("Building the model");
        RelationalPatternsModel model = this.modelBuilder.build();
        long totalTime = startingTime - System.currentTimeMillis();
        LOG.info("It took " + Math.abs(totalTime) + " ms to build the model");
        return model;
    }



}
