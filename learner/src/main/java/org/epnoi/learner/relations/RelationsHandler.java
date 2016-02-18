package org.epnoi.learner.relations;

import org.epnoi.knowledgebase.KnowledgeBase;
import org.epnoi.learner.helper.LearningHelper;
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
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 
 * @author
 *
 */

@Component
public class RelationsHandler {
	private static final Logger LOG = LoggerFactory.getLogger(RelationsHandler.class);


	@Autowired
	KnowledgeBase knowledgeBase;// The curated Knowledge Base

	@Autowired
	TermsRetriever termsRetriever;

	@Autowired
	LearningHelper helper;

	private Map<String, RelationsTable> relationsTable;// Map to store the
														// RelationsTable of
														// each domain
	private Map<String, TermsTable> termsTable;// Map to store the TermsTable of
												// each domain

	private List<Domain> consideredDomains;

	private Set<String> retrievedDomains;// Set of the domains which

	// Realtions/TermsTables have been
											// successfully retrieved

	// ---------------------------------------------------------------------------------------------------------------------

	public RelationsHandler() {
		this.relationsTable = new HashMap<>();
		this.termsTable = new HashMap<>();
		this.retrievedDomains = new HashSet<>();
	}

	// ---------------------------------------------------------------------------------------------------------------------

	public void init(List<Domain> domains)
			throws EpnoiInitializationException {
		LOG.info("Initializing the RelationsHandler with the following parameters:" + this);
		this.consideredDomains = domains;
		_initDomainsRelationsTables();

	}

	// ---------------------------------------------------------------------------------------------------------------------
	/**
	 * Method that retrieves for each considered domain the RelationsTable and
	 * TermsTable. If there exists any problem, the domain is
	 */
	private void _initDomainsRelationsTables() {
		if (consideredDomains == null) {
			LOG.info("The consideredDomains parameter was not set");
		} else if (consideredDomains.size() == 0) {
			LOG.info("The consideredDomains parameter was empty");
		} else {
			for (Domain domain : this.consideredDomains) {
				LOG.info("Retrieving information from the domain " + domain.getUri());
				try {
					TermsTable termsTable = termsRetriever.retrieve(domain);
					this.termsTable.put(domain.getUri(), termsTable);
					RelationsTable relationsTable = helper.getRelationsRetriever().retrieve(domain);
					this.relationsTable.put(domain.getUri(), relationsTable);
					this.retrievedDomains.add(domain.getUri());
				} catch (Exception e) {
					LOG.info("There was a problem retrieving the domain "
							+ domain.getUri() + " Terms/RelationsTable");
					e.printStackTrace();
					this.termsTable.put(domain.getUri(), null);
					this.relationsTable.put(domain.getUri(), null);
				}
			}
		}
	}

	/**
	 * Method used to determine if there exists a relationship of an specific
	 * type on a given domain. It test also if the relation exists in the
	 * Knowledge Base, as we consider it domain-independent
	 * 
	 * @param sourceTermSurfaceForm
	 * @param targetTermSurfaceForm
	 * @param type
	 * @param domain
	 * @return
	 */

	public Double areRelated(String sourceTermSurfaceForm,
			String targetTermSurfaceForm, String type, String domain) {
		LOG.info("sourceTermSurfaceForm " + sourceTermSurfaceForm
				+ " targetTermSurfaceForm " + targetTermSurfaceForm + ", type "
				+ type + ", domain " + domain);
		Double existenceProbability = 0.;
		if (this.knowledgeBase.areRelated(sourceTermSurfaceForm,
				targetTermSurfaceForm, type)) {
			existenceProbability = 1.;
		} else {
			if (this.relationsTable.get(domain) != null) {

				Term sourceTerm = this.termsTable.get(domain)
						.getTermBySurfaceForm(sourceTermSurfaceForm);
				Term targetTerm = this.termsTable.get(domain)
						.getTermBySurfaceForm(targetTermSurfaceForm);
				boolean found = false;

				Iterator<ProvenanceRelation> relationsIt = this.relationsTable
						.get(domain).getRelations(sourceTerm.getUri(), 0)
						.iterator();
				while (!found && relationsIt.hasNext()) {

					ProvenanceRelation relation = relationsIt.next();

					if (relation.getEndUri().equals(targetTerm.getUri())) {
						existenceProbability = relation.getWeight();
					}

				}
			}

		}
		LOG.debug("-----------------------------> "
				+ existenceProbability);
		return existenceProbability;
	}

}
