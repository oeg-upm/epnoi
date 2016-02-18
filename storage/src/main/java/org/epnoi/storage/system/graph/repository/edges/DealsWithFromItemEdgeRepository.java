package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.DealsWithFromItemEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface DealsWithFromItemEdgeRepository extends RelationGraphRepository<DealsWithFromItemEdge> {

    @Query("match (node1{uri:{0}})-[r:DEALS_WITH]->(node2{uri:{1}}) return r")
    Iterable<DealsWithFromItemEdge> findByNodes(String start, String end);

    @Query("match (:Item)-[r:DEALS_WITH]->(:Topic)-[:EMERGES_IN]->(domain{uri:{0}}) return r")
    Iterable<DealsWithFromItemEdge> findByDomain(String uri);

}
