package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.ComposesEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface ComposesEdgeRepository extends RelationGraphRepository<ComposesEdge> {


    // To avoid a class type exception
    @Override
    ComposesEdge findOneByUri(String uri);

    @Query("match (node1{uri:{0}})-[r:COMPOSES]->(node2{uri:{1}}) return r")
    Iterable<ComposesEdge> findByNodes(String start, String end);

    @Query("match (:Source)-[r:COMPOSES]->(domain{uri:{0}}) return r")
    Iterable<ComposesEdge> findByDomain(String uri);

    @Query("match (source{uri:{0}})-[r:COMPOSES]->(domain) return r")
    Iterable<ComposesEdge> findBySource(String uri);

}
