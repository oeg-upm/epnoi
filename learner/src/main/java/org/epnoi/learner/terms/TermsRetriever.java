package org.epnoi.learner.terms;

import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.model.domain.resources.Domain;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.domain.resources.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class TermsRetriever {

    private static final Logger LOG = LoggerFactory.getLogger(TermsRetriever.class);

    private final LearningHelper helper;

    public TermsRetriever(LearningHelper helper){
        this.helper = helper;
    }

    public void store(Domain domain, TermsTable termsTable) {
        LOG.info("Storing the Terms Table for domain: " + domain);

        for (Term term : termsTable.getTerms()) {
            helper.getUdm().save(Resource.Type.TERM).with(term);
        }
    }

    public TermsTable retrieve(Domain domain) {
        return getTermsTable(domain.getUri());
    }


    public TermsTable retrieve(String domainUri) {

        Optional<Resource> res = helper.getUdm().read(Resource.Type.DOMAIN).byUri(domainUri);

        if (res.isPresent()) {
            Domain domain = (Domain) res.get();
            TermsTable termsTable = getTermsTable(domain.getUri());
            return termsTable;
        }
        return new TermsTable();
    }

    private TermsTable getTermsTable(String domainUri) {
        TermsTable termsTable = new TermsTable();

        // First we retrieve the URIs of the resources associated with the
        // considered domain
        List<String> foundURIs = helper.getUdm().find(Resource.Type.TERM).in(Resource.Type.DOMAIN, domainUri);

        // The terms are then retrieved and added to the Terms Table
        for (String termURI : foundURIs) {
            Optional<Resource> res = helper.getUdm().read(Resource.Type.TERM).byUri(termURI);

            if (res.isPresent()){
                Term term = (Term) res.get();
                termsTable.addTerm(term);
            }
        }
        return termsTable;
    }

    private void remove(Domain domain) {
        List<String> foundURIs = helper.getUdm().find(Resource.Type.TERM).in(Resource.Type.DOMAIN, domain.getUri());

        //TODO Only the terms appearing (only) in this domain should be removed
        for (String termURI : foundURIs) {
            helper.getUdm().delete(Resource.Type.TERM).byUri(termURI);
        }
    }


}
