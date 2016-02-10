package org.epnoi.learner;


import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.learner.relations.RelationsHandler;
import org.epnoi.learner.relations.RelationsRetriever;
import org.epnoi.learner.relations.extractor.RelationsExtractor;
import org.epnoi.learner.terms.TermVertice;
import org.epnoi.learner.terms.TermsExtractor;
import org.epnoi.learner.terms.TermsRetriever;
import org.epnoi.learner.terms.TermsTable;
import org.epnoi.model.Domain;
import org.epnoi.model.Relation;
import org.epnoi.model.RelationsTable;
import org.epnoi.model.Term;
import org.epnoi.model.exceptions.EpnoiInitializationException;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class OntologyLearningWorkflow {
	private static final Logger logger = Logger
			.getLogger(OntologyLearningWorkflow.class.getName());
	private LearningParameters learningParameters;
	private TermsExtractor termExtractor;
	private TermsRetriever termsRetriever;
	private TermsTable termsTable;
	private RelationsTable relationsTable;
	private RelationsHandler relationsHandler;
	private RelationsExtractor relationsTableExtractor;
	private RelationsRetriever relationsTableRetriever;

	private DomainsTableCreator domainsTableCreator;
	private DomainsTable domainsTable;

	private double hypernymRelationsThreshold;
	private boolean extractTerms;
	private boolean extractRelations;
	public static final String DOMAIN_URI = "http://www.epnoi.org/CGTestCorpusDomain";
	// ---------------------------------------------------------------------------------------------------------

	public void init(LearningParameters learningParameters)
			throws EpnoiInitializationException {

		logger.info("Initializing the OntologyLearningWorlow with the following parameters: ");
		logger.info(learningParameters.toString());

		this.learningParameters = learningParameters;
		this.hypernymRelationsThreshold = (double) this.learningParameters
				.getParameterValue(LearningParameters.HYPERNYM_RELATION_EXPANSION_THRESHOLD);
		this.extractTerms = (boolean) this.learningParameters
				.getParameterValue(LearningParameters.EXTRACT_TERMS);

		this.learningParameters = learningParameters;

		this.domainsTableCreator = new DomainsTableCreator();
		this.domainsTableCreator.init(learningParameters);
		this.domainsTable = this.domainsTableCreator.create();

		this.termExtractor = new TermsExtractor();

		
		this.termsRetriever = new TermsRetriever();
		
		
		
		this.relationsTableExtractor = new RelationsExtractor();
		this.relationsTableExtractor.init(this.domainsTable, learningParameters);

		this.relationsTableRetriever = new RelationsRetriever();
		
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
				this.learningParameters, this.termsTable,
				this.relationsTable);

		Set<TermVertice> visitedTerms = new HashSet<TermVertice>();

		Set<TermVertice> termsVerticesToExpand = ontologyNoisyGraph.vertexSet();

		do {

			for (TermVertice termVerticeToExpand : termsVerticesToExpand) {
				for (Relation relation : relationsTable.getRelations(
						termVerticeToExpand.getTerm().getUri(),
						hypernymRelationsThreshold)) {
					Term destinationTerm = this.termsTable.getTerm(relation
							.getTarget());
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
