package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.HypernymOfEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface HypernymOfEdgeRepository extends RelationGraphRepository<HypernymOfEdge> {

    // To avoid a class type exception
    @Override
    HypernymOfEdge findOneByUri(String uri);

    @Query("match (node1{uri:{0}})-[r:PAIRS_WITH]->(node2{uri:{1}}) return r")
    Iterable<HypernymOfEdge> findByNodes(String start, String end);

    @Query("match (:Term)<-[r:HYPERNYM_OF]-(:Term)-[:APPEARED_IN]-(domain{uri:{0}}) return r")
    Iterable<HypernymOfEdge> findByDomain(String uri);

}
