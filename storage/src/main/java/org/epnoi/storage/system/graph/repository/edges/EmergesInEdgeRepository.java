package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.EmergesInEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface EmergesInEdgeRepository extends RelationGraphRepository<EmergesInEdge> {


    @Query("match (node1{uri:{0}})-[r:EMERGES_IN]->(node2{uri:{1}}) return r")
    Iterable<EmergesInEdge> findByNodes(String start, String end);

    @Query("match (:Topic)-[r:EMERGES_IN]->(domain{uri:{0}}) return r")
    Iterable<EmergesInEdge> findByDomain(String uri);

}
