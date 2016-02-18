package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.AppearedInEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface AppearedInEdgeRepository extends RelationGraphRepository<AppearedInEdge> {

    @Query("match (node1{uri:{0}})-[r:APPEARED_IN]->(node2{uri:{1}}) return r")
    Iterable<AppearedInEdge> findByNodes(String start, String end);

    @Query("match (:Term)-[r:APPEARED_IN]-(domain{uri:{0}}) return r")
    Iterable<AppearedInEdge> findByDomain(String uri);
}
