package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.TopicEmergesInDomain;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface TopicEmergesInDomainGraphRepository extends RelationGraphRepository<TopicEmergesInDomain> {


    @Query("match (node1{uri:{0}})-[r:EMERGES_IN]->(node2{uri:{1}}) return r")
    Iterable<TopicEmergesInDomain> findByNodes(String start, String end);

    @Query("match (:Topic)-[r:EMERGES_IN]->(domain{uri:{0}}) return r")
    Iterable<TopicEmergesInDomain> findByDomain(String uri);

}
