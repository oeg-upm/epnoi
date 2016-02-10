package org.epnoi.learner.terms;

import org.epnoi.model.Domain;
import org.epnoi.model.Term;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class TermsRetriever {

    private static final Logger logger = Logger.getLogger(TermsRetriever.class
            .getName());


    // -----------------------------------------------------------------------------------

    public TermsRetriever() {

    }

    // -----------------------------------------------------------------------------------

    public void store(Domain domain, TermsTable termsTable) {
        logger.info("Storing the Terms Table for domain " + domain);

        for (Term term : termsTable.getTerms()) {

            // TODO
            logger.severe("Pending to implement by using UDM");
//            core.getInformationHandler().put(term, Context.getEmptyContext());
//            core.getAnnotationHandler().label(term.getUri(), domain.getLabel());
        }
        System.out
                .println("=========================================================================================================================");
    }

    // -----------------------------------------------------------------------------------

    // -----------------------------------------------------------------------------------

    public TermsTable retrieve(Domain domain) {
        String domainLabel = domain.getLabel();

        TermsTable termsTable = getTermsTable(domainLabel);
        return termsTable;
    }

    // -----------------------------------------------------------------------------------

    public TermsTable retrieve(String domainUri) {
        // TODO
        logger.severe("Pending to implement by using UDM");
//        Domain domain = (Domain) core.getInformationHandler().get(domainUri, RDFHelper.DOMAIN_CLASS);
        Domain domain = null;

        if (domain != null) {

            TermsTable termsTable = getTermsTable(domain.getLabel());
            return termsTable;
        }
        return new TermsTable();
    }

    // -----------------------------------------------------------------------------------

    private TermsTable getTermsTable(String domainLabel) {
        TermsTable termsTable = new TermsTable();

        // First we retrieve the URIs of the resources associated with the
        // considered domain
        // TODO
        logger.severe("Pending to implement by using UDM");
//        List<String> foundURIs = this.core.getAnnotationHandler().getLabeledAs(domainLabel, RDFHelper.TERM_CLASS);
        List<String> foundURIs = Collections.EMPTY_LIST;


        // The terms are then retrieved and added to the Terms Table
        for (String termURI : foundURIs) {
            // TODO
            logger.severe("Pending to implement by using UDM");
//            Term term = (Term) this.core.getInformationHandler().get(termURI, RDFHelper.TERM_CLASS);
            Term term = null;

            termsTable.addTerm(term);
        }
        return termsTable;
    }

    // -----------------------------------------------------------------------------------

    private void remove(Domain domain) {
        // TODO
        logger.severe("Pending to implement by using UDM");
//        List<String> foundURIs = this.core.getAnnotationHandler().getLabeledAs(domain.getLabel(), RDFHelper.TERM_CLASS);
        List<String> foundURIs = Collections.emptyList();

        for (String termURI : foundURIs) {
            System.out.println("Removing the term " + termURI);
            // TODO
            logger.severe("Pending to implement by using UDM");
//            this.core.getInformationHandler().remove(termURI, RDFHelper.TERM_CLASS);
        }
    }

    // -----------------------------------------------------------------------------------

    public static void main(String[] args) {
        /*
		TermsExtractor termExtractor = new TermsExtractor();

		// List<String> consideredDomains = Arrays.asList("cs", "math");

		ArrayList<String> consideredDomains = new ArrayList(Arrays.asList("CGTestCorpus"));
		String targetDomain = "CGTestCorpus";
		Double hyperymMinimumThreshold = 0.7;
		boolean extractTerms = true;
		Integer numberInitialTerms = 10;
		String consideredResources = RDFHelper.PAPER_CLASS;

		LearningParameters learningParameters = new LearningParameters();
		learningParameters.setParameter(
				LearningParameters.CONSIDERED_DOMAINS,
				consideredDomains);
		learningParameters.setParameter(
				LearningParameters.TARGET_DOMAIN_URI, targetDomain);
		learningParameters
				.setParameter(
						LearningParameters.HYPERNYM_RELATION_EXPANSION_THRESHOLD,
						hyperymMinimumThreshold);
		learningParameters.setParameter(
				LearningParameters.EXTRACT_TERMS, extractTerms);
		learningParameters.setParameter(
				LearningParameters.NUMBER_INITIAL_TERMS,
				numberInitialTerms);

		Core core = CoreUtility.getUIACore();
		DomainsTableCreator domainGatherer = new DomainsTableCreator();
		domainGatherer.init(core, learningParameters);

		DomainsTable domainsTable = domainGatherer.create();

		termExtractor.init(core, domainsTable, learningParameters);
		// termExtractor.removeTerms();
		TermsTable termsTable = termExtractor.extract();
		termExtractor.storeTable(termsTable);
*/
    }

}
