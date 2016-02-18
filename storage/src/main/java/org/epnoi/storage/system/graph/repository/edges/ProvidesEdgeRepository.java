package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.ProvidesEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface ProvidesEdgeRepository extends RelationGraphRepository<ProvidesEdge> {

    @Query("match (node1{uri:{0}})-[r:PROVIDES]->(node2{uri:{1}}) return r")
    Iterable<ProvidesEdge> findByNodes(String start, String end);

    @Query("match (:Source)-[r:PROVIDES]->(:Document)<-[:CONTAINS]-(domain{uri:{0}}) return r")
    Iterable<ProvidesEdge> findByDomain(String uri);

}
