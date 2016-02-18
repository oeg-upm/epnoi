package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.model.domain.relations.Relation;
import org.epnoi.storage.system.graph.domain.edges.Edge;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by cbadenes on 22/12/15.
 */
@NoRepositoryBean
public interface RelationGraphRepository<T extends Relation> extends GraphRepository<T>{

    T findOneByUri(String uri);

    Iterable<T> findByNodes(String startUri, String endUri);

    Iterable<T> findByDomain(String uri);
}
