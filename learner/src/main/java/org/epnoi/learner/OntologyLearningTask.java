package org.epnoi.learner;

import org.apache.spark.api.java.JavaSparkContext;
import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.learner.relations.RelationsRetriever;
import org.epnoi.learner.relations.extractor.RelationsExtractor;
import org.epnoi.learner.terms.TermsExtractor;
import org.epnoi.learner.terms.TermsRetriever;
import org.epnoi.learner.terms.TermsTable;
import org.epnoi.model.RelationsTable;
import org.epnoi.model.domain.resources.Domain;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OntologyLearningTask {
    private static final Logger logger = LoggerFactory.getLogger(OntologyLearningTask.class);

    private TermsExtractor termExtractor;
    private TermsRetriever termsRetriever;
    private TermsTable termsTable;
    private RelationsTable relationsTable;

    private RelationsExtractor relationsTableExtractor;
    private RelationsRetriever relationsTableRetriever;


    private DomainsTableCreator domainsTableCreator;
    private DomainsTable domainsTable;

    private String domainURI;
    private List<String> consideredDomains = Collections.EMPTY_LIST;

    private double hypernymRelationsThreshold;
    private boolean obtainTerms;
    private boolean obtainRelations;
    private boolean parallelRelationsExtraction;
    private boolean extractTerms;
    private boolean extractRelations;

    private JavaSparkContext sparkContext;
    private LearningHelper helper;

    public void init(LearningHelper helper, JavaSparkContext sparkContext) throws EpnoiInitializationException{
        this.helper = helper;
        this.sparkContext = sparkContext;
    }


    private void _init(LearningHelper learningHelper){

        logger.info("Initializing the OntologyLearningTask with the following parameters: ");
        logger.info(learningHelper.toString());

        this.helper = learningHelper;


        this.obtainTerms = helper.isTerms();
        this.obtainRelations = helper.isRelations();
        this.hypernymRelationsThreshold = helper.getHypernymsThresholdExpansion();
        this.extractTerms = helper.isTermsExtract();
        this.extractRelations = helper.isRelationsExtract();
        this.parallelRelationsExtraction = helper.isRelationsParallel();

        this.domainsTableCreator = new DomainsTableCreator();
        this.domainsTableCreator.init(learningHelper,consideredDomains,domainURI);
        this.domainsTable = this.domainsTableCreator.create(domainURI);

        if (obtainTerms) {
            this.termExtractor = new TermsExtractor();
            this.termExtractor.init(this.domainsTable, learningHelper);
            this.termsRetriever = learningHelper.getTermsRetriever();
        }
        if (obtainRelations) {
            this.relationsTableExtractor = new RelationsExtractor();
            this.relationsTableExtractor.init(this.domainsTable, learningHelper);
            this.relationsTableRetriever = learningHelper.getRelationsRetriever();
        }
    }

    public void _execute() {
        logger.info("Starting the execution of a Ontology Learning Task");

        Domain targetDomain = this.domainsTable.getTargetDomain();
        if (obtainTerms) {
            if (extractTerms) {
                this.termsTable = this.termExtractor.extract();
            } else {
                this.termsTable = this.termsRetriever.retrieve(targetDomain);
            }
        }
        //      termsTable.show(30);


        if (obtainRelations) {
            if (extractRelations) {
                this.relationsTable = this.relationsTableExtractor.extract(this.termsTable);
            } else {
                this.relationsTable = this.relationsTableRetriever.retrieve(targetDomain);
            }

        }
        logger.info("Relations Table> " + this.relationsTable);

        logger.info("end");

    }

    // ---------------------------------------------------------------------------------------------------------

    public void perform(String domainURI) {

        logger.info("Starting the Ontology Learning Task");
        this.consideredDomains = new ArrayList(Arrays.asList(domainURI));
        this.domainURI = domainURI;
        _init(helper);
        _execute();
        logger.info("Ending the Ontology Learning Process!");
    }

    // ---------------------------------------------------------------------------------------------------------

    public TermsTable getTermsTable() {
        return this.termsTable;
    }

    // ---------------------------------------------------------------------------------------------------------

    public RelationsTable getRelationsTable() {
        return this.relationsTable;
    }

    // ---------------------------------------------------------------------------------------------------------

}
