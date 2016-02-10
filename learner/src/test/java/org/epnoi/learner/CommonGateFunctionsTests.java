package org.epnoi.learner;

import es.cbadenes.lab.test.IntegrationTest;
import gate.Document;
import gate.corpora.DocumentContentImpl;
import gate.util.InvalidOffsetException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.nlp.NLPHandler;
import org.epnoi.nlp.gate.NLPAnnotationsConstants;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {"epnoi.learner.delay = 2000"})
public class CommonGateFunctionsTests {
    private static final Logger logger = Logger.getLogger(CommonGateFunctionsTests.class.getName());

    private int MAX_SENTENCE_LENGTH = 4;

    @Autowired
    NLPHandler nlpHandler;

    private static final String JOB_NAME = "GATE_FUNCTIONS_TESTS";

    @Test
    public void testSentence(){
        Document document=null;
        try {

            document = nlpHandler.process("Autism is a neurodevelopmental disorder characterized by impaired social interaction, verbal and non-verbal communication, and restricted and repetitive behavior. Parents usually notice signs in the first two years of their child's life.");
        } catch (EpnoiResourceAccessException e) {
            e.printStackTrace();
        }


        gate.Annotation sentence = document.getAnnotations().get(NLPAnnotationsConstants.SENTENCE).iterator().next();
            System.out.println(sentence);
        //System.out.println(document);
        System.out.println("dc"+document.getContent());
        try {
            document.edit(sentence.getStartNode().getOffset(), sentence.getEndNode().getOffset(), new DocumentContentImpl(""));

        } catch (InvalidOffsetException e) {
            e.printStackTrace();
        }
        System.out.println("dc"+document.getContent());

    /*
        try {
            document.edit(0L, startOffset, new DocumentContentImpl(""));

            document.edit(endOffset + 1, document.getAnnotations().lastNode().getOffset(), new DocumentContentImpl(""));
        } catch (InvalidOffsetException e) {
            e.printStackTrace();
        }
        */
    }


    @Test
    public void test() {

        List<String> uris = Arrays.asList("http://en.wikipedia.org/wiki/Autism");
/*

        SparkConf sparkConf = new SparkConf().setMaster("local[8]").setAppName(JOB_NAME);

        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        // First we must create the RDD with the URIs of the resources to be
        // included in the creation of the corpus
        JavaRDD<String> corpusURIs = sparkContext.parallelize(uris);

        System.out.println("init!!!!!");
        // THen we obtain the URIs of the annotated content documents that are
        // stored at the UIA
        JavaRDD<String> annotatedContentURIs = corpusURIs.flatMap(new UriToSectionsAnnotatedContentURIsFlatMapper());
        JavaRDD<Document> annotatedDocuments = annotatedContentURIs.flatMap(new UriToAnnotatedDocumentFlatMapper());


        JavaRDD<Sentence> annotatedDocumentsSentences = annotatedDocuments
                .flatMap(new DocumentToSentencesFlatMapper());


        Sentence sentence = annotatedDocumentsSentences.collect().get(0);
        Document document = sentence.getContainedAnnotations().getDocument();

        Long startOffset = sentence.getAnnotation().getStartNode().getOffset();
        Long endOffset = sentence.getAnnotation().getEndNode().getOffset();
        System.out.println("A______>>  "+document.getContent());
        System.out.println("S length > "+(endOffset-startOffset));
        System.out.println("lengafter"+document.getContent().size());
        Document newDocument = null;
        try {
            document.edit(0L, startOffset, new DocumentContentImpl(""));
            System.out.println("lengbefore>" + document.getContent().size());
            document.edit(endOffset -startOffset, document.getAnnotations().lastNode().getOffset(), new DocumentContentImpl(""));
        } catch (InvalidOffsetException e) {
            e.printStackTrace();
        }

        System.out.println("A______>>  "+document);



        for (Sentence sentence : annotatedDocumentsSentences.collect()) {
			System.out.println("-------> " + sentence);
		}


        JavaRDD<RelationalSentenceCandidate> relationalSentencesCandidates =
                annotatedDocumentsSentences .flatMap(new
                        SentenceToRelationalSentenceCandidateFlatMapper());
*/
        //relationalSentencesCandidates.collect();

        /*
        JavaRDD<RelationalSentence> relationalSentences =
                relationalSentencesCandidates.map(new
                        RelationalSentenceMapFunction());
*/
        //System.out.println("------>"+relationalSentences.collect());

        //   return relationalSentences.collect();
    }
    // ----------------------------------------------------------------------------------------------------------------------
}

