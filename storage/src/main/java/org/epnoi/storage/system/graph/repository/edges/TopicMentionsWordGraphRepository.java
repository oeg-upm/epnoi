package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.TopicEmergesInDomain;
import org.epnoi.storage.system.graph.domain.edges.TopicMentionsWord;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface TopicMentionsWordGraphRepository extends RelationGraphRepository<TopicMentionsWord> {

    @Query("match (node1{uri:{0}})-[r:MENTIONS]->(node2{uri:{1}}) return r")
    Iterable<TopicMentionsWord> findByNodes(String start, String end);

    @Query("match (:Word)<-[r:MENTIONS]-(:Topic)-[:EMERGES_IN]->(domain{uri:{0}}) return r")
    Iterable<TopicMentionsWord> findByDomain(String uri);

}
