package org.epnoi.learner.terms;

import org.apache.commons.lang3.StringUtils;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Domain;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.domain.resources.Term;
import org.epnoi.model.domain.resources.Word;
import org.epnoi.storage.UDM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class TermsRetriever {

    private static final Logger LOG = LoggerFactory.getLogger(TermsRetriever.class);

    @Autowired
    UDM udm;

    public void store(String domainUri, TermsTable termsTable) {

        LOG.info("Storing the Terms Table for domain: " + domainUri);
        for (Term term : termsTable.getTerms()) {
            // Check if exist
            List<String> res = udm.find(Resource.Type.TERM).by(Term.CONTENT, term.getContent());

            if (res.isEmpty()){
                // Save
                udm.save(term);

                // Relate term to words
                LOG.debug("Trying to relate term to words: " + term.getContent());
                for (String word : StringUtils.split(term.getContent(), " ")){
                    List<String> uris = udm.find(Resource.Type.WORD).by(Word.CONTENT, word);
                    if ((uris != null) && (!uris.isEmpty())){
                        udm.save(Relation.newMentionsFromTerm(term.getUri(),uris.get(0)));
                    }
                }
            }else{
                LOG.debug("Term already exists: " + term + " with uri: " + res.get(0));
                term.setUri(res.get(0));
            }

            // Relate to domain
            udm.save(Relation.newAppearedIn(term.getUri(),domainUri));
        }
    }

    public TermsTable retrieve(Domain domain) {
        return retrieve(domain.getUri());
    }


    public TermsTable retrieve(String domainUri) {
        TermsTable termsTable = new TermsTable();

        // First we retrieve the URIs of the resources associated with the considered domain
        List<String> foundURIs = udm.find(Resource.Type.TERM).in(Resource.Type.DOMAIN,domainUri);

        // The terms are then retrieved and added to the Terms Table
        for (String termURI : foundURIs) {

            Optional<Resource> res = udm.read(Resource.Type.TERM).byUri(termURI);

            if (res.isPresent()){
                termsTable.addTerm(res.get().asTerm());
            }
        }
        return termsTable;
    }

}
