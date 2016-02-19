package org.epnoi.learner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan({"org.epnoi.learner","org.epnoi.storage","org.epnoi.eventbus","org.epnoi.knowledgebase","org.epnoi.nlp"})
@PropertySource({"classpath:learner.properties","classpath:eventbus.properties","classpath:storage.properties","classpath:knowledgebase.properties","classpath:nlp.properties"})
public class Config {

    private static final Logger LOG = LoggerFactory.getLogger(Config.class);

    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }

//
//    @Autowired
//    LearningHelper helper;


//    @Autowired
//    @Qualifier("lexicalPatternsModelCreationParameters")
//    private RelationalPatternsModelCreationParameters lexicalPatternsModelCreationParameters;
//
//    @Autowired
//    @Qualifier("syntacticPatternsModelCreationParameters")
//    private RelationalPatternsModelCreationParameters syntacticPatternsModelCreationParameters;

//    @Bean
//    @Profile(Profiles.DEVELOP)
//    public RelationalPatternsModelCreationParameters syntacticPatternsModelCreationParameters() {
//
//        RelationalPatternsModelCreationParameters parameters = new RelationalPatternsModelCreationParameters();
//        parameters
//                .setParameter(
//                        RelationalPatternsModelCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI,
//                        "http://drInventor.eu/reviews/second/relationalSentencesCorpus");
//        parameters
//                .setParameter(
//                        RelationalPatternsModelCreationParameters.MAX_PATTERN_LENGTH,
//                        20);
//
//        parameters.setParameter(
//                RelationalPatternsModelCreationParameters.MODEL_PATH,
//                "/opt/epnoi/epnoideployment/secondReviewResources/syntacticModel/model.bin");
//        parameters.setParameter(RelationalPatternsModelCreationParameters.TYPE,
//                PatternsConstants.SYNTACTIC);
//
//        parameters.setParameter(
//                RelationalPatternsModelCreationParameters.STORE, false);
//
//        parameters.setParameter(
//                RelationalPatternsModelCreationParameters.VERBOSE, true);
//
//        parameters.setParameter(RelationalPatternsModelCreationParameters.TEST,
//                true);
//        System.out.println("parameters> " + parameters);
//        return parameters;
//    }

//    @Bean
//    @Profile(Profiles.DEVELOP)
//    public RelationalPatternsModelCreationParameters lexicalPatternsModelCreationParameters(
//            @Value("${learner.corpus.patterns.lexical.path}") String path,
//            @Value("${learner.corpus.patterns.lexical.uri}") String uri,
//            @Value("${learner.corpus.patterns.lexical.maxlength}") Integer maxLength,
//            @Value("${learner.corpus.patterns.lexical.interpolation}") Double interpolationConstant,
//            @Value("${learner.corpus.patterns.lexical.store}") Boolean store,
//            @Value("${learner.corpus.patterns.lexical.verbose}") Boolean verbose,
//            @Value("${learner.corpus.patterns.lexical.test}") Boolean test
//
//    ) {
//        RelationalPatternsModelCreationParameters parameters = new RelationalPatternsModelCreationParameters();
//        parameters
//                .setParameter(
//                        RelationalPatternsModelCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI,
//                        uri);
//        parameters
//                .setParameter(
//                        RelationalPatternsModelCreationParameters.MAX_PATTERN_LENGTH,
//                        maxLength);
//
//
//        parameters.setParameter(RelationalPatternsModelCreationParameters.INTERPOLATION_CONSTANT, interpolationConstant);
//
//        parameters.setParameter(
//                RelationalPatternsModelCreationParameters.MODEL_PATH, path);
//        parameters.setParameter(RelationalPatternsModelCreationParameters.TYPE,
//                PatternsConstants.LEXICAL);
//
//        parameters.setParameter(
//                RelationalPatternsModelCreationParameters.STORE, store);
//
//        parameters.setParameter(
//                RelationalPatternsModelCreationParameters.VERBOSE, verbose);
//
//        parameters.setParameter(RelationalPatternsModelCreationParameters.TEST,
//                test);
//        return parameters;
//    }

//    @Bean
//    @DependsOn("learningHelper")
////    @Profile(Profiles.DEVELOP)
//    public RelationalPatternsModelCreator lexicalPatternsModelCreator() throws EpnoiInitializationException, EpnoiResourceAccessException {
//        RelationalPatternsModelCreator relationalPatternsModelCreator = new RelationalPatternsModelCreator();
//        relationalPatternsModelCreator.init(helper, PatternsConstants.LEXICAL);
//        return relationalPatternsModelCreator;
//    }
//
//    @Bean
//    @DependsOn("learningHelper")
////    @Profile(Profiles.DEVELOP)
//    public RelationalPatternsModelCreator syntacticPatternsModelCreator() throws EpnoiInitializationException, EpnoiResourceAccessException {
//        RelationalPatternsModelCreator relationalPatternsModelCreator = new RelationalPatternsModelCreator();
//        relationalPatternsModelCreator.init(helper, PatternsConstants.SYNTACTIC);
//        return relationalPatternsModelCreator;
//    }


//    @Bean
//    @Profile(Profiles.DEVELOP)
//    public RelationalSentencesCorpusCreationParameters relationalSentencesCorpusParameters(
//            @Value("${learner.corpus.sentences.uri}") String uri,
//            @Value("${learner.corpus.sentences.description}") String description,
//            @Value("${learner.corpus.sentences.type}") String type,
//            @Value("${learner.corpus.sentences.maxlength}") Integer maxLength,
//            @Value("${learner.corpus.sentences.store}") Boolean store,
//            @Value("${learner.corpus.sentences.verbose}") Boolean verbose,
//            @Value("${learner.corpus.sentences.thrift.port}") Integer thriftPort) {
//
//
//        //logger.info("Starting the Relation Sentences Corpus Creator");
//
//
//        RelationalSentencesCorpusCreationParameters parameters = new RelationalSentencesCorpusCreationParameters();
//
//        // String relationalCorpusURI = "http://drInventor.eu/reviews/second/relationalSentencesCorpus";
//
//        parameters.setParameter(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI,
//                uri);
//
//        parameters.setParameter(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_TYPE,
//                type);
//
//        parameters.setParameter(
//                RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_DESCRIPTION,
//                description);
//
//
//        parameters.setParameter(RelationalSentencesCorpusCreationParameters.MAX_SENTENCE_LENGTH, maxLength);
//
//        parameters.setParameter(RelationalSentencesCorpusCreationParameters.STORE, store);
//
//        parameters.setParameter(RelationalSentencesCorpusCreationParameters.VERBOSE, verbose);
//
//        parameters.setParameter(RelationalSentencesCorpusCreationParameters.THRIFT_PORT, thriftPort);
//        //parameters.setParameter(RelationalSentencesCorpusCreationParameters.REST_PORT, 8082);
//        return parameters;
//    }


//    @Bean
//    @Scope(BeanDefinition.SCOPE_SINGLETON)
//    public ApiListingResource apiListingResource() {
//        return new ApiListingResource();
//    }
//
//
//    @Bean
//    @Scope(BeanDefinition.SCOPE_SINGLETON)
//    public SwaggerSerializers swaggerSerializer() {
//        return new SwaggerSerializers();
//    }


//    @Bean()
//    @Scope(BeanDefinition.SCOPE_SINGLETON)
//    public BeanConfig beanConfig() {
//
//
//        BeanConfig beanConfig = new BeanConfig();
//        beanConfig.setVersion("1.0.2");
//        beanConfig.setSchemes(new String[]{"http"});
//        beanConfig.setHost("localhost:8082/learner/rest");
//        beanConfig.setBasePath("/");
//        beanConfig.setResourcePackage("org.epnoi.learner.service.rest");
//        beanConfig.setScan(true);
//        return beanConfig;
//    }

//    @Bean()
//    public SparkConf sparkConfig(@Value("${epnoi.learner.spark.master}") String master,
//                                 @Value("${epnoi.learner.spark.app}") String appName) {
//
//        SparkConf sparkConf = new SparkConf().setMaster(master).setAppName(appName);
//        logger.info("Creating the following spark configuration " + sparkConf.getAll());
//
//        return sparkConf;
//    }





}