package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.ItemDealsWithTopic;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface ItemDealsWithTopicGraphRepository extends RelationGraphRepository<ItemDealsWithTopic> {

    @Query("match (node1{uri:{0}})-[r:DEALS_WITH]->(node2{uri:{1}}) return r")
    Iterable<ItemDealsWithTopic> findByNodes(String start, String end);

    @Query("match (:Item)-[r:DEALS_WITH]->(:Topic)-[:EMERGES_IN]->(domain{uri:{0}}) return r")
    Iterable<ItemDealsWithTopic> findByDomain(String uri);

}
