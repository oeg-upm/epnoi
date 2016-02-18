package org.epnoi.learner.helper;

import lombok.Data;
import lombok.Getter;
import org.epnoi.knowledgebase.KnowledgeBase;
import org.epnoi.learner.modules.Learner;
import org.epnoi.learner.relations.RelationsRetriever;
import org.epnoi.learner.relations.patterns.RelationalPatternsModel;
import org.epnoi.learner.terms.TermsRetriever;
import org.epnoi.nlp.NLPHandler;
import org.epnoi.storage.UDM;
import org.epnoi.storage.generator.URIGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 10/02/16.
 */
@Data
@Component
public class LearningHelper {

    @Value("${epnoi.learner.delay}")
    long delay;

    @Value("${epnoi.learner.rest.port}")
    int restPort;

    @Value("${epnoi.learner.spark.app}")
    String sparkApp;

    @Value("${epnoi.learner.spark.master}")
    String sparkMaster;

    @Value("${epnoi.learner.thrift.port}")
    int thriftPort;

    @Value("${learner.corpus.patterns.lexical.interpolation }")
    double lexicalInterpolation;

    @Value("${learner.corpus.patterns.lexical.maxlength }")
    int lexicalMaxLength;

    @Value("${learner.corpus.patterns.lexical.path}")
    String lexicalPath;

    @Value("${learner.corpus.patterns.lexical.store}")
    boolean lexicalStore;

    @Value("${learner.corpus.patterns.lexical.test}")
    boolean lexicalTest;

    @Value("${learner.corpus.patterns.lexical.uri}")
    String lexicalUri;

    @Value("${learner.corpus.patterns.lexical.verbose}")
    boolean lexicalVerbose;

    @Value("${learner.corpus.sentences.description }")
    String sentencesDescription;

    @Value("${learner.corpus.sentences.maxlength }")
    int sentencesMaxLength;

    @Value("${learner.corpus.sentences.store}")
    boolean sentencesStore;

    @Value("${learner.corpus.sentences.thrift.port }")
    int sentencesThriftPort;

    @Value("${learner.corpus.sentences.type}")
    String sentencesType;

    @Value("${learner.corpus.sentences.uri}")
    String sentencesUri;

    @Value("${learner.corpus.sentences.verbose}")
    boolean sentencesVerbose;

    @Value("${learner.demo.harvester.label}")
    String harvesterLabel;

    @Value("${learner.demo.harvester.overwrite}")
    boolean harvesterOverwrite;

    @Value("${learner.demo.harvester.path}")
    String harvesterPath;

    @Value("${learner.demo.harvester.uri}")
    String harvesterUri;

    @Value("${learner.demo.harvester.verbose}")
    boolean harvesterVerbose;

    @Value("${learner.task.relations}")
    boolean relations;

    @Value("${learner.task.relations.extract}")
    boolean relationsExtract;

    @Value("${learner.task.relations.hypernyms.lexical.path}")
    String hypernymsLexicalPath;

    @Value("${learner.task.relations.hypernyms.threshold.expansion }")
    double hypernymsThresholdExpansion;

    @Value("${learner.task.relations.hypernyms.threshold.extraction }")
    double hypernymsThresholdExtraction;

    @Value("${learner.task.relations.knowledgebase }")
    boolean relationsKnowledgeBase;

    @Value("${learner.task.relations.maxdistance}")
    int relationsMaxDistance;

    @Value("${learner.task.relations.parallel}")
    boolean relationsParallel;

    @Value("${learner.task.relations.store}")
    boolean relationsStore;

    @Value("${learner.task.relations.thrift.port }")
    int relationsThriftPort;

    @Value("${learner.task.terms}")
    boolean terms;

    @Value("${learner.task.terms.extract}")
    boolean termsExtract;

    @Value("${learner.task.terms.initialterms}")
    int termsInitial;

    @Value("${learner.task.terms.store}")
    boolean termsStore;

    @Autowired
    URIGenerator uriGenerator;

    @Autowired
    UDM udm;

    @Autowired
    Learner learner;

    @Autowired
    NLPHandler nlpHandler;

    @Autowired
    KnowledgeBase knowledgeBase;

    @Autowired
    RelationalPatternsModel relationalPatternsModel;

    @Autowired
    RelationsRetriever relationsRetriever;

    @Autowired
    TermsRetriever termsRetriever;

    //	// Domain Definition
//	// Parameters--------------------------------------------------------------------------------
//
//	public static final String CONSIDERED_DOMAINS = "CONSIDERED_DOMAINS"; // List<Domain>
//	// The domains considered in the ontology learning process. It includes the
//	// target domain plus the ones used as a reference.
//
//	public static final String TARGET_DOMAIN_URI = "TARGET_DOMAIN_URI";//String (URI of the target domain that must be among the CONSIDERED_DOMAINS
//	// Domain that is the target of the ontology learning process, the learned
//	// ontology represents this domain.
//
//	// Term Extraction phase
//	// ----------------------------------------------------------------------------------------
//
//	// Expansion phase
//	// parameters-----------------------------------------------------------------------------------
//
//	public static final String MAX_SOURCE_TARGET_DISTANCE = "MAX_SOURCE_TARGET_DISTANCE";
//
//	public static final String NUMBER_INITIAL_TERMS = "NUMBER_INITIAL_TERMS";
//	// number of initial terms in the ontology
//
//	public static final String HYPERNYM_RELATION_EXPANSION_THRESHOLD = "HYPERNYM_RELATION_EXPANSION_THRESHOLD";
//	// Minimum probability for a detected hypernym relation to be consider for
//	// being expanded in the ontology learning process
//
//
//
//	public static final String EXTRACT_TERMS = "EXTRACT_TERMS";
//
//	public static final String OBTAIN_TERMS = "OBTAIN_TERMS";
//
//	public static final String HYPERNYM_MODEL_PATH = "HYPERNYM_MODEL_PATH";
//
//	public static final String HYPERNYM_MODEL = "HYPERNYM_MODEL";
//
//	public static final String HYPERNYM_RELATION_EXTRACTION_THRESHOLD = "HYPERNYM_RELATION_EXTRACTION_THRESHOLD";
//
//	// Minimum probability for a detected hypernym relation to be consider for
//	// being extracted in the relation extractor phase in the ontology learning
//	// process
//
//	public static final String OBTAIN_RELATIONS = "OBTAIN_RELATIONS";
//
//	public static final String EXTRACT_RELATIONS = "EXTRACT_RELATIONS";
//
//	public static final String CONSIDER_KNOWLEDGE_BASE= "CONSIDER_KNOWLDEDGE_BASE";
//
//	public static final String UIA_PATH = "UIA_PATH";
//
//
//	public static final String THRIFT_PORT = "THRIFT_PORT";
//
//	public static final String REST_PORT = "REST_PORT";
//
//	public static final String  STORE_TERMS = "STORE_TERMS";
//
//	public static final String STORE_RELATIONS = "STORE_RELATIONS";
//
//	public static final String EXTRACT_RELATIONS_PARALLEL = "EXTRACT_RELATIONS_PARALLEL";

}
