package org.epnoi.storage.system.graph.node;

import org.epnoi.storage.system.graph.domain.nodes.PartNode;
import org.epnoi.storage.system.graph.repository.nodes.ResourceGraphRepository;
import org.epnoi.storage.system.graph.repository.nodes.PartGraphRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class PartGraphRepositoryTest extends BaseGraphRepositoryTest<PartNode> {

    @Autowired
    PartGraphRepository repository;

    @Override
    public ResourceGraphRepository<PartNode> getRepository() {
        return repository;
    }

    @Override
    public PartNode getEntity() {
        PartNode node = new PartNode();
        node.setUri("parts/72ce5395-6268-439a-947e-802229e7f022");
        node.setCreationTime("2015-12-21T16:18:59Z");
        return node;
    }
}
