package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.DocumentDealsWithTopic;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface DocumentDealsWithTopicGraphRepository extends RelationGraphRepository<DocumentDealsWithTopic> {

    @Query("match (node1{uri:{0}})-[r:DEALS_WITH]->(node2{uri:{1}}) return r")
    Iterable<DocumentDealsWithTopic> findByNodes(String start, String end);

    @Query("match (domain{uri:{0}})-[:CONTAINS]->(:Document)-[r:DEALS_WITH]->(:Topic) return r")
    Iterable<DocumentDealsWithTopic> findByDomain(String uri);

    @Query("match (document{uri:{0}})-[d:DEALS_WITH]->(topic)-[e:EMERGES_IN]->(domain{uri:{1}}) return d")
    Iterable<DocumentDealsWithTopic> findByDocumentAndDomain(String document, String domain);

}
