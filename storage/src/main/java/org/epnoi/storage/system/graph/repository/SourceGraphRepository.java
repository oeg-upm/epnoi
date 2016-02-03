package org.epnoi.storage.system.graph.repository;

import org.epnoi.storage.system.graph.domain.ItemNode;
import org.epnoi.storage.system.graph.domain.SourceNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface SourceGraphRepository extends BaseGraphRepository<SourceNode> {

    // To avoid a class type exception
    @Override
    SourceNode findOneByUri(String uri);

    @Query("match (source)-[:COMPOSES]->(domain{uri:{0}}) return source")
    Iterable<SourceNode> findByDomain(String uri);

}
