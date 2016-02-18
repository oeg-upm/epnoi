package org.epnoi.learner.terms;

import org.epnoi.model.domain.resources.Term;

import java.util.*;

public class TermsIndex {

	static final Comparator<Term> TERMS_ORDER = new Comparator<Term>() {
		public int compare(Term term1, Term term2) {
			if (term1.getTermhood() < term2.getTermhood())
				return 1;
			else if (term1.getTermhood() == term2.getTermhood()) {
				return 0;
			} else {
				return -1;
			}

		}
	};

	// Terms are indexed per domain, thus this table is domain_uri->term_word ->
	// term
	private Map<String, Map<String, Term>> terms;

	// -------------------------------------------------------------------------------------------------------

	public void init() {
		this.terms = new HashMap<String, Map<String, Term>>();
	}

	// -------------------------------------------------------------------------------------------------------

	public Term lookUp(String domain, String word) {
		return terms.get(domain).get(word);
	}

	// -------------------------------------------------------------------------------------------------------

	public void updateTerm(String domain, Term term) {

		Map<String, Term> domainTerms = this.terms.get(domain);

		if (domainTerms == null) {
			domainTerms = new HashMap<>();
			this.terms.put(domain, domainTerms);

		}

		Term indexedTerm = domainTerms.get(term.getContent());
		if (indexedTerm == null) {
			domainTerms.put(term.getContent(), term);
		} else {
			// System.out.println("indexed> "+indexedTerm);
			indexedTerm.setOcurrences(indexedTerm.getOcurrences()+1);
		}
		// System.out.println("this.terms " + this.terms);
	}

	// -------------------------------------------------------------------------------------------------------

	public void updateSubTerm(String domain, Term term, Term subTerm) {

		Map<String, Term> domainTerms = this.terms.get(domain);

		if (domainTerms == null) {
			domainTerms = new HashMap<>();
			this.terms.put(domain, domainTerms);

		}

		Term indexedTerm = domainTerms.get(subTerm.getContent());
		if (indexedTerm == null) {
			domainTerms.put(subTerm.getContent(), subTerm);
			subTerm.setOcurrences(term.getOcurrences() - term.getSubterms());
			subTerm.setSubterms(term.getOcurrences() - term.getSubterms());
			subTerm.setSuperterms(1L);
		} else {

			indexedTerm.setOcurrences(
					indexedTerm.getOcurrences()
							+ term.getOcurrences()
							- term.getSubterms());

			indexedTerm.setSubterms(
					indexedTerm.getSubterms()
							+ term.getOcurrences()
							- term.getSubterms());
			indexedTerm.setSuperterms(
					indexedTerm.getSuperterms() + 1);

		}

	}

	// -------------------------------------------------------------------------------------------------------

	public List<Term> getTermCandidates(String domain) {
		if (this.terms.get(domain) != null) {
			List<Term> termCandidates = new ArrayList<Term>(this.terms.get(domain).values());
			Collections.sort(termCandidates);
			return termCandidates;
		}
		return Collections.EMPTY_LIST;
	}

	// -------------------------------------------------------------------------------------------------------

	public List<Term> getTerms(String domain) {
		if (this.terms.get(domain) != null) {
			List<Term> termCandidates = new ArrayList<Term>(this.terms.get(domain).values());
			Collections.sort(termCandidates, TERMS_ORDER);
			return termCandidates;
		}
		return Collections.EMPTY_LIST;

	}

	// -------------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "TermsIndex [terms=" + terms + "]";
	}

	// -------------------------------------------------------------------------------------------------------

}
