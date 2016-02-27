package org.epnoi.storage.system.graph.repository.nodes;

import org.epnoi.storage.system.graph.domain.nodes.SourceNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface SourceGraphRepository extends ResourceGraphRepository<SourceNode> {

    // To avoid a class type exception
    @Override
    SourceNode findOneByUri(String uri);

    @Query("match (source)-[:COMPOSES]->(domain{uri:{0}}) return source")
    Iterable<SourceNode> findByDomain(String uri);

    @Query("match (source)-[:PROVIDES]->(document{uri:{0}}) return source")
    Iterable<SourceNode> findByDocument(String uri);

}
