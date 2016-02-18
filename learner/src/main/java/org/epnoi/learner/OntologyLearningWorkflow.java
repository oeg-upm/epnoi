package org.epnoi.learner;


import lombok.Getter;
import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.learner.relations.RelationsHandler;
import org.epnoi.learner.relations.RelationsRetriever;
import org.epnoi.learner.relations.extractor.RelationsExtractor;
import org.epnoi.learner.terms.TermVertice;
import org.epnoi.learner.terms.TermsExtractor;
import org.epnoi.learner.terms.TermsRetriever;
import org.epnoi.learner.terms.TermsTable;
import org.epnoi.model.RelationsTable;
import org.epnoi.model.domain.relations.ProvenanceRelation;
import org.epnoi.model.domain.resources.Domain;
import org.epnoi.model.domain.resources.Term;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Component
public class OntologyLearningWorkflow {

	private static final Logger logger = LoggerFactory.getLogger(OntologyLearningWorkflow.class);

	@Value("${learner.task.relations.hypernyms.threshold.expansion }")
	double hypernymRelationsThreshold;

	@Getter @Value("${learner.task.terms.extract}")
	boolean extractTerms;

	@Autowired
	RelationsRetriever relationsTableRetriever;

	@Autowired
	TermsRetriever termsRetriever;



	@Autowired
	LearningHelper helper;

	private TermsExtractor termExtractor;
	private TermsTable termsTable;
	private RelationsTable relationsTable;
	private RelationsHandler relationsHandler;
	private RelationsExtractor relationsTableExtractor;

	private DomainsTableCreator domainsTableCreator;
	private DomainsTable domainsTable;


	private boolean extractRelations;
	public static final String DOMAIN_URI = "http://www.epnoi.org/CGTestCorpusDomain";


	@PostConstruct
	public void init() throws EpnoiInitializationException {

		logger.info("Initializing the OntologyLearningWorlow with the following parameters: " + this);


		this.domainsTableCreator = new DomainsTableCreator();
		this.domainsTableCreator.init(helper, Collections.EMPTY_LIST,null);
		this.domainsTable = this.domainsTableCreator.create();

		this.termExtractor = new TermsExtractor();

		
		this.relationsTableExtractor = new RelationsExtractor();
		this.relationsTableExtractor.init(this.domainsTable, helper);
		
	}

	// ---------------------------------------------------------------------------------------------------------

	public void execute() {
		logger.info("Starting the execution of a Ontology Learning Process");

		Domain targetDomain = this.domainsTable.getTargetDomain();
		
		if (extractTerms) {

			this.termsTable = this.termExtractor.extract();
		} else {
			this.termsTable = this.termsRetriever.retrieve(targetDomain);
		}

		//termsTable.show(30);

		System.out.println("Extracting relations table");

		this.relationsTable = this.relationsTableExtractor
				.extract(this.termsTable);

		System.out.println("Relations Table> " + this.relationsTable);

		System.out.println("end");
		System.exit(0);
		OntologyGraph ontologyNoisyGraph = OntologyGraphFactory.build(
				this.helper, this.termsTable,
				this.relationsTable);

		Set<TermVertice> visitedTerms = new HashSet<TermVertice>();

		Set<TermVertice> termsVerticesToExpand = ontologyNoisyGraph.vertexSet();

		do {

			for (TermVertice termVerticeToExpand : termsVerticesToExpand) {
				for (ProvenanceRelation relation : relationsTable.getRelations(
						termVerticeToExpand.getTerm().getUri(),
						hypernymRelationsThreshold)) {
					Term destinationTerm = this.termsTable.getTerm(relation.getEndUri());
					TermVertice destinationTermVertice = new TermVertice(
							destinationTerm);
					ontologyNoisyGraph.addEdge(termVerticeToExpand,
							destinationTermVertice);

					// If the destination term vertice has not been visited, we
					// must add it to the vertices to expnad so that it is
					// considered in the next iteration.
					if (!visitedTerms.contains(destinationTerm)) {
						termsVerticesToExpand.add(destinationTermVertice);
					}
				}
				visitedTerms.add(termVerticeToExpand);
			}
			// We stop the expansion when we have no further term vertices to
			// expand
		} while (termsVerticesToExpand.size() > 0);

		// In future versions the ontology graph should be cleaned here.

	}

}
