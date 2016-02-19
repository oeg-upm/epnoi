package org.epnoi.learner.modules;


import org.apache.spark.api.java.JavaSparkContext;
import org.epnoi.learner.Config;
import org.epnoi.learner.OntologyLearningTask;
import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.learner.helper.SparkHelper;
import org.epnoi.learner.relations.RelationsRetriever;
import org.epnoi.learner.terms.TermsRetriever;
import org.epnoi.learner.terms.TermsTable;
import org.epnoi.model.RelationsTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



/**
 * Created by rgonza on 13/11/15.
 */
@Component
public class Learner {

    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    @Value("${learner.task.terms}")
    boolean terms;

    @Value("${learner.task.terms.store}")
    boolean termsStore;

    @Value("${learner.task.relations}")
    boolean relations;

    @Value("${learner.task.relations.store}")
    boolean relationsStore;

    @Autowired
    Trainer trainer;

    @Autowired
    LearningHelper helper;

    @Autowired
    TermsRetriever termsRetriever;

    @Autowired
    RelationsRetriever relationsRetriever;

    public Trainer getTrainer() {
        return this.trainer;
    }

    public LearningHelper getParameters() {
        return this.helper;
    }

    public void learn(String domainUri) {

        try {
            OntologyLearningTask ontologyLearningTask = new OntologyLearningTask();
            ontologyLearningTask.init(this.helper);
            try {
                ontologyLearningTask.perform(domainUri);
                _storeLearningResults(ontologyLearningTask, domainUri);
            } catch (Exception e) {
                logger.error("There was a problem while learning the domain " + domainUri,e);
            }
        } catch (Exception e) {
            logger.warn("Something went wrong when learning about the domain " + domainUri, e);
        }

    }

    private void _storeLearningResults(OntologyLearningTask ontologyLearningTask, String domainUri) {
        if (terms && termsStore) {
            this.termsRetriever.store(domainUri, ontologyLearningTask.getTermsTable());
        }

        if (relations && relationsStore) {
            this.relationsRetriever.store(ontologyLearningTask.getRelationsTable());
        }
    }

    public RelationsTable retrieveRelations(String domainUri) {
        return relationsRetriever.retrieve(domainUri);
    }

    public TermsTable retrieveTerminology(String domainUri) {
        return termsRetriever.retrieve(domainUri);
    }
}