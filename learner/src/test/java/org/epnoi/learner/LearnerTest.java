package org.epnoi.learner;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.learner.modules.Learner;
import org.epnoi.learner.terms.TermsTable;
import org.epnoi.model.RelationsTable;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by cbadenes on 10/02/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {
        "epnoi.nlp.gatePath = /Users/cbadenes/Tools/drinventor/gate",
        "epnoi.learner.delay = 1000",
        "epnoi.learner.rest.port = 8083",
        "epnoi.learner.spark.memory=1g",
        "epnoi.learner.spark.threads=4",
        "epnoi.learner.thrift.port=10",
        "learner.corpus.patterns.lexical.interpolation = 0.5",
        "learner.corpus.patterns.lexical.maxlength = 20",
        "learner.corpus.patterns.lexical.path= /Users/cbadenes/Tools/drinventor/lexicalModel/model.bin",
        "learner.corpus.patterns.lexical.store=true",
        "learner.corpus.patterns.lexical.test=false",
        "learner.corpus.patterns.lexical.uri=http://drInventor.eu/reviews/second/relationalSentencesCorpus",
        "learner.corpus.patterns.lexical.verbose=true",
        "learner.corpus.sentences.description =DrInventor second review relational sentences corpus",
        "learner.corpus.sentences.maxlength = 80",
        "learner.corpus.sentences.store=true",
        "learner.corpus.sentences.thrift.port = 8585",
        "learner.corpus.sentences.type=HYPERNYM",
        "learner.corpus.sentences.uri=http://drInventor.eu/reviews/second/relationalSentencesCorpus",
        "learner.corpus.sentences.verbose=true",
        "learner.task.relations.extract=true",
        "learner.task.relations.hypernyms.lexical.path=/Users/cbadenes/Tools/drinventor/lexicalModel/model.bin",
        "learner.task.relations.hypernyms.threshold.expansion = 0.7",
        "learner.task.relations.hypernyms.threshold.extraction = 0.01",
        "learner.task.relations.knowledgebase = false",
        "learner.task.relations.maxdistance= 20",
        "learner.task.relations.parallel=false",
        "learner.task.relations.store=true",
        "learner.task.relations.thrift.port = 8585",
        "learner.task.relations=true",
        "learner.task.terms.extract=true",
        "learner.task.terms.initialterms=10",
        "learner.task.terms.store=true",
        "learner.task.terms=true",
        "epnoi.knowledgeBase.wordnet.dictionaryPath=/Users/cbadenes/Tools/drinventor/wordnet/dictWN3.1",
        "epnoi.knowledgeBase.wikidata.dump.path = #{environment.EPNOI_HOME}/wikidata"})
public class LearnerTest {

    private static final Logger LOG = LoggerFactory.getLogger(LearnerTest.class);

    @Autowired
    Learner learner;

    @Test
    public void learnOntology(){

        String domainURI = "sample";

        // Learn Ontology
        learner.learn(domainURI);

        // Retrieve Terms
        TermsTable terms = learner.retrieveTerminology(domainURI);
        LOG.info("Terms: " + terms);

        RelationsTable relations = learner.retrieveRelations(domainURI);
        LOG.info("Relations: " + relations);


        LOG.info("success");
    }

}
