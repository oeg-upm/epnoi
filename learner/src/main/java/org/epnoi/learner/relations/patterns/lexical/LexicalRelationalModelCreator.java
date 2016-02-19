package org.epnoi.learner.relations.patterns.lexical;

import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.learner.relations.patterns.*;
import org.epnoi.model.RelationalSentencesCorpus;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class LexicalRelationalModelCreator {

    private static final Logger logger = LoggerFactory.getLogger(LexicalRelationalModelCreator.class);

    @Autowired
    LearningHelper helper;


    private RelationalPatternsCorpusCreator patternsCorpusCreator;
    private RelationalPatternsCorpus patternsCorpus;
    private RelaxedBigramSoftPatternModelBuilder modelBuilder;
    private RelaxedBigramSoftPatternModel model;
    private boolean store;
    private boolean verbose;
    private boolean test;
    private double interpolationConstant;
    private String path;


    // ----------------------------------------------------------------------------------------------------------------
    @PostConstruct
    public void init() throws EpnoiInitializationException {
        logger.info("Initializing the LexicalRealationalModelCreator with the following parameters");

        String relationalSentencesCorpusURI = helper.getSentencesUri();
        this.patternsCorpusCreator = new RelationalPatternsCorpusCreator();
        this.patternsCorpusCreator.init(new LexicalRelationalPatternGenerator());

        // TODO
        logger.error("pending to implement by using UDM");
//        RelationalSentencesCorpus relationalSentencesCorpus = (RelationalSentencesCorpus) this.core.getInformationHandler().get(relationalSentencesCorpusURI, RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);
        RelationalSentencesCorpus relationalSentencesCorpus = null;


        if (relationalSentencesCorpus == null) {
            throw new EpnoiInitializationException(
                    "The Relational Sentences Corpus "
                            + relationalSentencesCorpusURI
                            + "could not be found");

        } else {

            _buildPatternsCorpus(relationalSentencesCorpus);
        }
        modelBuilder = new RelaxedBigramSoftPatternModelBuilder(helper);

        _readParameters();

    }

    private void _buildPatternsCorpus(RelationalSentencesCorpus relationalSentencesCorpus) {
        logger.info("The RelationalSencentcesCorpus has "
                + relationalSentencesCorpus.getSentences().size()
                + " sentences");
        patternsCorpus = patternsCorpusCreator
                .buildCorpus(relationalSentencesCorpus);

        logger.info("The RelationalPatternsCorpus has "
                + patternsCorpus.getPatterns().size() + " patterns");
    }

    private void _readParameters() {
        this.path = helper.getLexicalPath();

        this.store = helper.isLexicalStore();

        this.verbose = helper.isLexicalVerbose();

        this.test = helper.isLexicalTest();

        this.interpolationConstant = helper.getLexicalInterpolation();
    }

    // ----------------------------------------------------------------------------------------------------------------

    public RelaxedBigramSoftPatternModel buildModel() {
        long startingTime = System.currentTimeMillis();
        logger.info("Adding all the patterns to the model");
        for (RelationalPattern pattern : patternsCorpus.getPatterns()) {
            this.modelBuilder.addPattern(((LexicalRelationalPattern) pattern));
        }

        logger.info("Building the model " + this.modelBuilder);
        RelaxedBigramSoftPatternModel model = this.modelBuilder.build();
        long totalTime = startingTime - System.currentTimeMillis();
        logger.info("It took " + Math.abs(totalTime) + " ms to build the model");
        return model;
    }

    // ----------------------------------------------------------------------------------------------------------------

    public void create() {
        logger.info("Starting the creation of a lexical BigramSoftPatternModel with the following parameters: " + this);
        this.model = buildModel();

        if (this.verbose) {
            this.model.show();
        }
        if (this.store) {
            logger.info("Storing the model at " + path);
            try {
                RelationalPatternsModelSerializer.serialize(path, model);

            } catch (EpnoiResourceAccessException e) {
                logger.error("There was a problem trying to serialize the BigramSoftPatternModel at "
                        + path);
                logger.error(e.getMessage());
            }

        }
    }

    // ----------------------------------------------------------------------------------------------------------------
/*
    public static void main(String[] args) {
        logger.info("Starting the Lexical Relational Model creation");
        RelationalPatternsModelCreationParameters parameters = new RelationalPatternsModelCreationParameters();
        parameters
                .setParameter(
                        RelationalPatternsModelCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI,
                        "http://drInventorFirstReview/relationalSentencesCorpus");
        parameters
                .setParameter(
                        RelationalPatternsModelCreationParameters.MAX_PATTERN_LENGTH,
                        20);

        parameters.setParameter(
                RelationalPatternsModelCreationParameters.MODEL_PATH,
                "/home/rgonza/Escritorio/model.bin");

        parameters.setParameter(
                RelationalSentencesCorpusCreationParameters.STORE, true);

        parameters.setParameter(
                RelationalSentencesCorpusCreationParameters.VERBOSE, true);

        parameters
                .setParameter(
                        RelationalPatternsModelCreationParameters.INTERPOLATION_CONSTANT,
                        0.0);

        Core core = CoreUtility.getUIACore();

        LexicalRelationalModelCreator modelCreator = new LexicalRelationalModelCreator();
        try {
            modelCreator.init(core, parameters);
        } catch (EpnoiInitializationException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        modelCreator.create();

        logger.info("Ending the Lexical Relational Model creation");
    }
*/
    // ----------------------------------------------------------------------------------------------------------------

}
