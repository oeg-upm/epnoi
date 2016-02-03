package org.epnoi.storage.system.graph;

import org.epnoi.storage.system.graph.domain.DocumentNode;
import org.epnoi.storage.system.graph.repository.BaseGraphRepository;
import org.epnoi.storage.system.graph.repository.DocumentGraphRepository;
import org.epnoi.storage.system.graph.repository.DomainGraphRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class DocumentGraphRepositoryTest extends BaseGraphRepositoryTest<DocumentNode> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentGraphRepositoryTest.class);

    @Autowired
    DocumentGraphRepository repository;

    @Autowired
    DomainGraphRepository domainRepository;

    @Override
    public BaseGraphRepository<DocumentNode> getRepository() {
        return repository;
    }

    @Override
    public DocumentNode getEntity() {
        DocumentNode node = new DocumentNode();
        node.setUri("documents/72ce5395-6268-439a-947e-802229e7f022");
        node.setCreationTime("2015-12-21T16:18:59Z");
        return node;
    }


    @Test
    public void similar(){
        repository.deleteSimilarRelationsInDomain("http://epnoi.org/domains/1f02ae0b-7d96-42c6-a944-25a3050bf1e2");
    }

}
