package org.epnoi.learner;

import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.learner.terms.TermVertice;
import org.epnoi.learner.terms.TermsTable;
import org.epnoi.model.RelationsTable;
import org.epnoi.model.domain.resources.Term;

import java.util.List;

public class OntologyGraphFactory {

	static OntologyGraph build(LearningHelper helper, TermsTable termsTable, RelationsTable table){
		OntologyGraph initialOntology = new OntologyGraph();
		
		//TODO int initialNumberOfTerms =  Integer.parseInt((String)ontologyLearningParamters.getParameterValue(LearningHelper.NUMBER_INITIAL_TERMS));
		int initialNumberOfTerms =  10;


		List<Term> mostProblabeTerms= termsTable.getMostProbable(initialNumberOfTerms);
		for (Term term: mostProblabeTerms){
			TermVertice termVertice = new TermVertice(term);
			initialOntology.addVertex(termVertice);
		}
		
		return initialOntology;
	}
}
