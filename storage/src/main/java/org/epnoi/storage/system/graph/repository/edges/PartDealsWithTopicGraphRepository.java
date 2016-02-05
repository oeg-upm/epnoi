package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.ItemDealsWithTopic;
import org.epnoi.storage.system.graph.domain.edges.ItemSimilarToItem;
import org.epnoi.storage.system.graph.domain.edges.PartDealsWithTopic;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface PartDealsWithTopicGraphRepository extends RelationGraphRepository<PartDealsWithTopic> {


    @Query("match (node1{uri:{0}})-[r:DEALS_WITH]->(node2{uri:{1}}) return r")
    Iterable<PartDealsWithTopic> findByNodes(String start, String end);

    @Query("match (:Part)-[r:DEALS_WITH]->(:Topic)-[:EMERGES_IN]->(domain{uri:{0}}) return r")
    Iterable<PartDealsWithTopic> findByDomain(String uri);

}
