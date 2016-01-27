package org.epnoi.storage.graph;

import org.epnoi.storage.graph.domain.ItemNode;
import org.epnoi.storage.graph.repository.BaseGraphRepository;
import org.epnoi.storage.graph.repository.ItemGraphRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class ItemGraphRepositoryTest extends BaseGraphRepositoryTest<ItemNode> {

    @Autowired
    ItemGraphRepository repository;

    @Override
    public BaseGraphRepository<ItemNode> getRepository() {
        return repository;
    }

    @Override
    public ItemNode getEntity() {
        ItemNode node = new ItemNode();
        node.setUri("items/72ce5395-6268-439a-947e-802229e7f022");
        node.setCreationTime("2015-12-21T16:18:59Z");
        return node;
    }
}
