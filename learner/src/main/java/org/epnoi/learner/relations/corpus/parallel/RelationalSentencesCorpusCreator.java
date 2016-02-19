package org.epnoi.learner.relations.corpus.parallel;

import gate.Document;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.learner.helper.SparkHelper;
import org.epnoi.model.RelationalSentence;
import org.epnoi.model.RelationalSentencesCorpus;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.nlp.wikipedia.WikipediaPagesRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class RelationalSentencesCorpusCreator {
    private static final Logger LOG = LoggerFactory.getLogger(RelationalSentencesCorpusCreator.class);

    @Autowired
    private SparkHelper sparkHelper;

    @Autowired
    LearningHelper helper;

    @Value("${learner.corpus.sentences.maxlength}")
    int sentencesMaxLength;

    @Value("${learner.corpus.patterns.lexical.store}")
    boolean lexicalStore;

    @Value("${learner.corpus.patterns.lexical.verbose}")
    boolean lexicalVerbose;

    private RelationalSentencesCorpus corpus;

    @PostConstruct
    public void init() throws EpnoiInitializationException {
        LOG.info("Initializing the RelationalSentencesCorpusCreator with the following parameters " + this);
        this.corpus = new RelationalSentencesCorpus();
    }


    public void createCorpus() {

        LOG.info("Creating a relational sencences corpus with the following parameters: " + helper);

        // This should be done in parallel!!
        List<String> URIs = _collectCorpusURIs();

        corpus.setUri(helper.getSentencesUri());
        corpus.setDescription(helper.getSentencesDescription());
        corpus.setType(helper.getSentencesType());

        corpus.setSentences(_findRelationalSentences(URIs));

        if (this.lexicalVerbose) {
            RelationalSentencesCorpusViewer.showRelationalSentenceCorpusInfo(corpus);
        }

        if (this.lexicalStore) {
            _storeCorpus();
        }
    }


    private List<RelationalSentence> _findRelationalSentences(List<String> URIs) {

        //Broadcast<RelationalSentencesCorpusCreationParameters> parametersBroadcast = sparkContext.broadcast((RelationalSentencesCorpusCreationParameters) this.parameters);

        // First we must create the RDD with the URIs of the resources to be
        // included in the creation of the corpus
        JavaRDD<String> corpusURIs = sparkHelper.getSc().parallelize(URIs);

        LOG.info("init!!!!!");
        // THen we obtain the uris of the annotated content documents that are
        // stored at the UIA. The uris are those of the sections of the documents

        JavaRDD<String> annotatedContentURIs = corpusURIs.flatMap(uri -> {
            UriToSectionsAnnotatedContentURIsFlatMapper mapper = new UriToSectionsAnnotatedContentURIsFlatMapper(helper);
            return mapper.call(uri);
        });

        //From

        JavaRDD<Document> annotatedDocuments = annotatedContentURIs.flatMap(uri -> {
            UriToAnnotatedDocumentFlatMapper flatMapper = new UriToAnnotatedDocumentFlatMapper(helper);
            return flatMapper.call(uri);
        });


        JavaRDD<Sentence> annotatedDocumentsSentences = annotatedDocuments
                .flatMap(new DocumentToSentencesFlatMapper());


        JavaRDD<RelationalSentenceCandidate> relationalSentencesCandidates =
                annotatedDocumentsSentences.flatMap(relationalSentence -> {
                    SentenceToRelationalSentenceCandidateFlatMapper sentenceMapper = new
                            SentenceToRelationalSentenceCandidateFlatMapper(helper);
                    return sentenceMapper.call(relationalSentence);
                });

        JavaRDD<RelationalSentence> relationalSentences =
                relationalSentencesCandidates.flatMap(new
                        RelationalSentenceCandidateToRelationalSentenceFlatMapper());


        return relationalSentences.collect();
    }

    private void _storeCorpus() {
        // TODO
        LOG.error("Pending to implement by using UDM");
//        core.getInformationHandler().remove(this.corpus.getUri(), RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);
//        core.getInformationHandler().put(this.corpus, Context.getEmptyContext());
    }

    // ----------------------------------------------------------------------------------------------------------------------

    private List<String> _collectCorpusURIs() {
//        Selector selector = new Selector();
//        selector.setProperty(SelectorHelper.TYPE, RDFHelper.WIKIPEDIA_PAGE_CLASS);



        // String uri = "http://en.wikipedia.org/wiki/AccessibleComputing";

        // logger.info("Retrieving the URIs of the Wikipedia articles ");

        List<String> wikipediaPages = WikipediaPagesRetriever.getWikipediaArticles();

        return wikipediaPages.subList(0, helper.getSentencesMaxLength());
    }

    // ----------------------------------------------------------------------------------------------------------------------

/*
    public static void main(String[] args) {
        logger.info("Starting the Relation Sentences Corpus Creator");

        RelationalSentencesCorpusCreator relationSentencesCorpusCreator = new RelationalSentencesCorpusCreator();

        Core core = CoreUtility.getUIACore();

        RelationalSentencesCorpusCreationParameters parameters = new RelationalSentencesCorpusCreationParameters();

        String relationalCorpusURI = "http://epnoi.org/relationalSentencesCorpus";

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI,
                relationalCorpusURI);

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_TYPE,
                RelationHelper.HYPERNYMY);

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.UIA_PATH, "http://localhost:8080/epnoi/rest");

        parameters.setParameter(
                RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_DESCRIPTION,
                "DrInventor first review relational sentences corpus");

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI,
                relationalCorpusURI);

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.MAX_SENTENCE_LENGTH, 80);

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.STORE, false);

        parameters.setParameter(RelationalSentencesCorpusCreationParameters.VERBOSE, true);

        try {
            relationSentencesCorpusCreator.init(core, parameters);
        } catch (EpnoiInitializationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(-1);
        }
        */
        /*
         * RelationalSentencesCorpus testRelationalSentenceCorpus =
		 * relationSentencesCorpusCreator .createTestCorpus();
		 * 
		 * System.out.println("testCorpus>" + testRelationalSentenceCorpus);
		 * 
		 * core.getInformationHandler().put(testRelationalSentenceCorpus,
		 * Context.getEmptyContext());
		 * 
		 * System.out.println(core.getInformationHandler().get(
		 * testRelationalSentenceCorpus.getURI()));
		 * 
		 * System.exit(0);
		 */

//        relationSentencesCorpusCreator.createCorpus();
/*
        System.out.println("Checking if the Relational Sentence Corpus can be retrieved");

		RelationalSentencesCorpus relationalSentenceCorpus = (RelationalSentencesCorpus) core.getInformationHandler()
				.get(relationalCorpusURI, RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);
		System.out.println("The readed relational sentences corpus " + relationalSentenceCorpus);
		logger.info("Stopping the Relation Sentences Corpus Creator");
	*/
    //   }

}
