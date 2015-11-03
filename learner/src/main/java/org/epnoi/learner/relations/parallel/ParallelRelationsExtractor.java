package org.epnoi.learner.relations.parallel;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.DocumentContent;
import gate.util.InvalidOffsetException;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.epnoi.learner.DomainsTable;
import org.epnoi.learner.OntologyLearningWorkflowParameters;
import org.epnoi.learner.relations.corpus.ProbableRelationalSentencesFilter;
import org.epnoi.learner.relations.corpus.parallel.*;
import org.epnoi.learner.relations.patterns.RelationalPatternsModel;
import org.epnoi.learner.relations.patterns.RelationalPatternsModelSerializer;
import org.epnoi.learner.relations.patterns.lexical.LexicalRelationalPattern;
import org.epnoi.learner.relations.patterns.lexical.LexicalRelationalPatternGenerator;
import org.epnoi.learner.terms.TermCandidateBuilder;
import org.epnoi.learner.terms.TermsTable;
import org.epnoi.model.*;
import org.epnoi.model.commons.Parameters;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.nlp.gate.NLPAnnotationsConstants;
import org.epnoi.uia.informationstore.SelectorHelper;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class ParallelRelationsExtractor {
    private static final Logger logger = Logger
            .getLogger(ParallelRelationsExtractor.class.getName());
    private static final long MAX_DISTANCE = 20;
    private Core core;
    private RelationalPatternsModel softPatternModel;
    private Parameters parameters;
    private DomainsTable domainsTable;
    private TermsTable termsTable;
    private LexicalRelationalPatternGenerator patternsGenerator;
    private RelationsTable relationsTable;
    private double hypernymExtractionThreshold;
    private String targetDomain;
    private boolean considerKnowledgeBase = false;
    private KnowledgeBase knowledgeBase;

    private static final String JOB_NAME = "RELATIONS_EXTRACTION";
    // --------------------------------------------------------------------------------------

    public void init(Core core, DomainsTable domainsTable, Parameters parameters)
            throws EpnoiInitializationException {
        logger.info("Initializing the Relations Extractor with the following parameters");
        logger.info(parameters.toString());

        this.core = core;
        this.parameters = parameters;
        String hypernymModelPath = (String) parameters
                .getParameterValue(OntologyLearningWorkflowParameters.HYPERNYM_MODEL_PATH);
        this.hypernymExtractionThreshold = (double) parameters
                .getParameterValue(OntologyLearningWorkflowParameters.HYPERNYM_RELATION_EXTRACTION_THRESHOLD);
        this.targetDomain = (String) parameters
                .getParameterValue(OntologyLearningWorkflowParameters.TARGET_DOMAIN);

        this.considerKnowledgeBase = (boolean) parameters
                .getParameterValue(OntologyLearningWorkflowParameters.CONSIDER_KNOWLEDGE_BASE);
        this.patternsGenerator = new LexicalRelationalPatternGenerator();
        this.domainsTable = domainsTable;
        this.relationsTable = new RelationsTable();
        // We retrieve the knowledge base just in case that it must be
        // considered when searching for relations
        if (considerKnowledgeBase) {
            try {
                this.knowledgeBase = core.getKnowledgeBaseHandler()
                        .getKnowledgeBase();
            } catch (EpnoiResourceAccessException e) {
                // TODO Auto-generated catch block
                throw new EpnoiInitializationException(e.getMessage());
            }
        }

        try {

            this.softPatternModel = RelationalPatternsModelSerializer
                    .deserialize(hypernymModelPath);
            parameters.setParameter(OntologyLearningWorkflowParameters.HYPERNYM_MODEL, softPatternModel);
        } catch (EpnoiResourceAccessException e) {
            throw new EpnoiInitializationException(e.getMessage());
        }
    }

    // ---------------------------------------------------------------------------------------

    public RelationsTable extract(TermsTable termsTable) {
        logger.info("Extracting the Relations Table");
        this.termsTable = termsTable;
        this.relationsTable = new RelationsTable();
        // The relations finding task is only performed in the target domain,
        // these are the resources that we should consider

        List<String> domainResourceUris = domainsTable.getDomainResources().get(
                domainsTable.getTargetDomain().getUri());
        SparkConf sparkConf = new SparkConf().setMaster("local[8]").setAppName(JOB_NAME);

        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        Broadcast<OntologyLearningWorkflowParameters> parametersBroadcast = sparkContext.broadcast((OntologyLearningWorkflowParameters) this.parameters);

        // First we must create the RDD with the URIs of the resources to be
        // included in the creation of the corpus
        JavaRDD<String> corpusURIs = sparkContext.parallelize(domainResourceUris);

        //We retrieve for each document its annotated document
        JavaRDD<Document> corpusAnnotatedDocuments = corpusURIs.flatMap(new DocumentRetrievalFlatMapFunction());


        //Each annotated document is split in sentences
        JavaRDD<Sentence> corpusSentences = corpusAnnotatedDocuments.flatMap(new DocumentToSentencesFlatMapFunction());

        //For each sentence we create all the possible RelationalSentenceCandidates
        JavaRDD<RelationalSentenceCandidate> relationsCandidates = corpusSentences.flatMap(relationalSentenceCandidate -> {
            SentenceToRelationCandidateFunction mapper = new SentenceToRelationCandidateFunction(parametersBroadcast.getValue());
            return mapper.call(relationalSentenceCandidate);
        });

        JavaRDD<RelationalSentence> relationalSentences = relationsCandidates.map(new RelationalSentenceMapFunction());

        JavaRDD<Relation> probableRelations = relationalSentences.flatMap(relationalSentence -> {
            RelationalSentenceToRelationMapper mapper = new RelationalSentenceToRelationMapper(parametersBroadcast.getValue());
            return mapper.call(relationalSentence);
        });


        //Now we aggregate those relations that are repeated in to a single relation that aggregates them

        //First, we create pairs <relationURI, relation>
        JavaPairRDD<String, Relation> probableRelationsByUri = probableRelations.mapToPair(new ResourceKeyValueMapper());

        //We reduce them by key
        JavaPairRDD<String, Relation> aggregatedProbableRelationsByUri = probableRelationsByUri.reduceByKey(new RelationsReduceByKeyFunction());

        //Finally each aggregated relation is added to the relations table
        for (Tuple2<String, Relation> tuple: aggregatedProbableRelationsByUri.collect()) {
            relationsTable.addRelation(tuple._2());
        }

        return relationsTable;
    }





    public static void main(String[] args) {
        System.out.println("starting");

        SparkConf sparkConf = new SparkConf().setMaster("local[8]").setAppName(JOB_NAME);

        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        final Broadcast<String> separator = sparkContext.broadcast("/");

        JavaRDD<String> corpusURIs = sparkContext.parallelize(Arrays.asList("world", "boadilla", "madrid"));

        JavaRDD<String> greetingsRDD = corpusURIs.map(s -> {
            SimpleTestFunction simpleTestFunction = new SimpleTestFunction(separator.getValue());
            return simpleTestFunction.test(s);
        });
        System.out.println(greetingsRDD.collect());

        System.out.println("stoping");
    }
}