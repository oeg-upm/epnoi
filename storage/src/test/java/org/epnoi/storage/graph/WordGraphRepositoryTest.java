package org.epnoi.storage.graph;

import org.epnoi.storage.graph.domain.WordNode;
import org.epnoi.storage.graph.repository.BaseGraphRepository;
import org.epnoi.storage.graph.repository.WordGraphRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class WordGraphRepositoryTest extends BaseGraphRepositoryTest<WordNode> {

    @Autowired
    WordGraphRepository repository;

    @Override
    public BaseGraphRepository<WordNode> getRepository() {
        return repository;
    }

    @Override
    public WordNode getEntity() {
        WordNode node = new WordNode();
        node.setUri("words/72ce5395-6268-439a-947e-802229e7f022");
        node.setCreationTime("2015-12-21T16:18:59Z");
        return node;
    }

    @Test
    public void deleteEmbeddedRelations(){

        String domain = "http://epnoi.org/domains/d4a5f93d-fc90-453e-a2d5-7ca27dfb4e29";

        repository.deleteEmbeddingInDomain(domain);
        repository.deletePairingInDomain(domain);
    }
}
