package org.epnoi.storage.system.graph.node;

import org.epnoi.storage.system.graph.domain.nodes.DomainNode;
import org.epnoi.storage.system.graph.repository.nodes.ResourceGraphRepository;
import org.epnoi.storage.system.graph.repository.nodes.DomainGraphRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class DomainGraphRepositoryTest extends BaseGraphRepositoryTest<DomainNode> {

    @Autowired
    DomainGraphRepository repository;

    @Override
    public ResourceGraphRepository<DomainNode> getRepository() {
        return repository;
    }

    @Override
    public DomainNode getEntity() {
        DomainNode node = new DomainNode();
        node.setUri("domains/72ce5395-6268-439a-947e-802229e7f022");
        node.setCreationTime("2015-12-21T16:18:59Z");
        return node;
    }
}
