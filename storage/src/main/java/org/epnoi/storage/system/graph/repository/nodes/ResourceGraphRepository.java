package org.epnoi.storage.system.graph.repository.nodes;

import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.system.graph.domain.nodes.Node;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by cbadenes on 22/12/15.
 */
@NoRepositoryBean
public interface ResourceGraphRepository<T extends Resource> extends GraphRepository<T>{

    T findOneByUri(String uri);

    @Query("match (n) return n")
    Iterable<T> findByDomain(String uri);
}
