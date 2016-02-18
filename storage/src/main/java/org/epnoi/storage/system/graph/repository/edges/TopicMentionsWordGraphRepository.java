package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.MentionsFromTopicEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface TopicMentionsWordGraphRepository extends RelationGraphRepository<MentionsFromTopicEdge> {

    // To avoid a class type exception
    @Override
    MentionsFromTopicEdge findOneByUri(String uri);

    @Query("match (node1{uri:{0}})-[r:MENTIONS]->(node2{uri:{1}}) return r")
    Iterable<MentionsFromTopicEdge> findByNodes(String start, String end);

    @Query("match (:Word)<-[r:MENTIONS]-(:Topic)-[:EMERGES_IN]->(domain{uri:{0}}) return r")
    Iterable<MentionsFromTopicEdge> findByDomain(String uri);

}
