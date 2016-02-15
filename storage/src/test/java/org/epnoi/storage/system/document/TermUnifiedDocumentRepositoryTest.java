package org.epnoi.storage.system.document;

import org.epnoi.storage.system.document.domain.TermDocument;
import org.epnoi.storage.system.document.domain.WordDocument;
import org.epnoi.storage.system.document.repository.BaseDocumentRepository;
import org.epnoi.storage.system.document.repository.TermDocumentRepository;
import org.epnoi.storage.system.document.repository.WordDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class TermUnifiedDocumentRepositoryTest extends BaseDocumentRepositoryTest<TermDocument> {

    @Autowired
    TermDocumentRepository repository;

    @Override
    public BaseDocumentRepository<TermDocument> getRepository() {
        return repository;
    }

    @Override
    public TermDocument getEntity() {
        TermDocument document = new TermDocument();
        document.setUri("words/72ce5395-6268-439a-947e-802229e7f022");
        document.setCreationTime("2015-12-21T16:18:59Z");
        document.setContent("molecular");
        document.setConsensus(2.0);
        document.setCvalue(0.8);
        document.setLength(1);
        document.setOcurrences(2);
        document.setPertinence(0.8);
        document.setProbability(0.5);
        document.setSubterms(2);
        document.setSuperterms(2);
        document.setTermhood(0.9);
        return document;
    }
}
