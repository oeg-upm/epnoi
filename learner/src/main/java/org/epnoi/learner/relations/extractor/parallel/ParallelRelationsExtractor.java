package org.epnoi.learner.relations.extractor.parallel;

import gate.Document;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.epnoi.learner.DomainsTable;
import org.epnoi.learner.LearningHelper;
import org.epnoi.learner.relations.corpus.parallel.DocumentToSentencesFlatMapper;
import org.epnoi.learner.relations.corpus.parallel.RelationalSentenceCandidate;
import org.epnoi.learner.relations.corpus.parallel.Sentence;
import org.epnoi.learner.relations.corpus.parallel.UriToAnnotatedDocumentFlatMapper;
import org.epnoi.model.DeserializedRelationalSentence;
import org.epnoi.model.Relation;
import org.epnoi.model.RelationsTable;
import org.epnoi.model.commons.Parameters;

import java.util.List;
import java.util.logging.Logger;


public class ParallelRelationsExtractor {
    private static final Logger logger = Logger
            .getLogger(ParallelRelationsExtractor.class.getName());
    private static final long MAX_DISTANCE = 20;

    LearningHelper parameters;

    JavaSparkContext sparkContext;

    private DomainsTable domainsTable;


    private static final String JOB_NAME = "RELATIONS_EXTRACTION";
    // --------------------------------------------------------------------------------------

    public void init(LearningHelper parameters, DomainsTable domainsTable, JavaSparkContext sparkContext) {
        logger.info("Initializing the Relations Extractor with the following parameters");
        logger.info(parameters.toString());
        this.parameters = parameters;
        this.sparkContext = sparkContext;
        this.domainsTable = domainsTable;


        //  this.patternsGenerator = new LexicalRelationalPatternGenerator();


        // We retrieve the knowledge base just in case that it must be
        // considered when searching for relations
        /*
        if (considerKnowledgeBase) {
            try {
                this.knowledgeBase = core.getKnowledgeBaseHandler()
                        .getKnowledgeBase();
            } catch (EpnoiResourceAccessException e) {
                // TODO Auto-generated catch block
                throw new EpnoiInitializationException(e.getMessage());
            }
        }
*/

    }

    // ---------------------------------------------------------------------------------------

    public RelationsTable extract(DomainsTable domainsTable) {
        logger.info("Extracting the Relations Table");
        RelationsTable relationsTable = new RelationsTable();
        String relationsTableUri = domainsTable.getTargetDomain().getUri()+"/relations";
        relationsTable.setUri(relationsTableUri);

        // The relations finding task is only performed in the target domain,
        // these are the resources that we should consider

        String targetDomainUri = domainsTable.getTargetDomain().getUri();
        List<String> domainResourceUris = domainsTable.getDomainResources().get(
                targetDomainUri);


        Broadcast<LearningHelper> parametersBroadcast = sparkContext.broadcast((LearningHelper) this.parameters);

        // First we must create the RDD with the URIs of the resources to be
        // included in the creation of the corpus
        JavaRDD<String> corpusURIs = sparkContext.parallelize(domainResourceUris);

        //We retrieve for each document its annotated document
        JavaRDD<Document> corpusAnnotatedDocuments;
        corpusAnnotatedDocuments = corpusURIs.flatMap(uri -> {
                    Parameters parameters = (Parameters) parametersBroadcast.value();
                    UriToAnnotatedDocumentFlatMapper flatMapper = new UriToAnnotatedDocumentFlatMapper(parameters);
                    return flatMapper.call(uri);
                }
        );


        //Each annotated document is split in sentences
        JavaRDD<Sentence> corpusSentences = corpusAnnotatedDocuments.flatMap(new DocumentToSentencesFlatMapper());

        //For each sentence we create all the possible RelationalSentenceCandidates
        JavaRDD<RelationalSentenceCandidate> relationsCandidates = corpusSentences.flatMap(relationalSentenceCandidate -> {
            SentenceToRelationCandidateFunction mapper = new SentenceToRelationCandidateFunction(parametersBroadcast.getValue());
            return mapper.call(relationalSentenceCandidate);
        });

        JavaRDD<DeserializedRelationalSentence> relationalSentences = relationsCandidates.flatMap(new RelationalSentenceCandidateToRelationalSentenceFlatMapper());

        JavaRDD<Relation> probableRelations = relationalSentences.flatMap(relationalSentence -> {
            RelationalSentenceToRelationMapper mapper = new RelationalSentenceToRelationMapper(parametersBroadcast.getValue());
            return mapper.call(relationalSentence);
        });
        probableRelations.collect();
/*

        //Now we aggregate those relations that are repeated in to a single relation that aggregates them

        //First, we create pairs <relationURI, relation>
        JavaPairRDD<String, Relation> probableRelationsByUri = probableRelations.mapToPair(new ResourceKeyValueMapper());

        //We reduce them by key
        JavaPairRDD<String, Relation> aggregatedProbableRelationsByUri = probableRelationsByUri.reduceByKey(new RelationsReduceByKeyFunction());

        //Finally each aggregated relation is added to the relations table
        for (Tuple2<String, Relation> tuple : aggregatedProbableRelationsByUri.collect()) {
            relationsTable.addRelation(tuple._2());
        }
        */

        return relationsTable;
    }


}
