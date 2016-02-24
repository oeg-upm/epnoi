package org.epnoi.storage.system.graph.repository.nodes;

import org.epnoi.storage.system.graph.domain.nodes.DocumentNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface DocumentGraphRepository extends ResourceGraphRepository<DocumentNode> {

    // To avoid a class type exception
    @Override
    DocumentNode findOneByUri(String uri);

    @Query("match (document)-[:BUNDLES]->(item{uri:{0}}) return document")
    Iterable<DocumentNode> findByItem(String uri);

    @Query("match (document)<-[:CONTAINS]-(domain{uri:{0}}) return document")
    Iterable<DocumentNode> findByDomain(String uri);

    @Query("match (document)<-[:PROVIDES]-(source{uri:{0}}) return document")
    Iterable<DocumentNode> findBySource(String uri);

    @Query("match (document)-[:BUNDLES]->(item)<-[:DESCRIBES]-(part{uri:{0}}) return document")
    Iterable<DocumentNode> findByPart(String uri);

    @Query("match (document)-[:DEALS_WITH]->(topic{uri:{0}}) return document")
    Iterable<DocumentNode> findByTopic(String uri);

    @Query("match (document)-[:DEALS_WITH]->(topic)-[:MENTIONS]->(word{uri:{0}}) return document")
    Iterable<DocumentNode> findByWord(String uri);

    @Query("match (document)-[:DEALS_WITH]->(topic:Topic)-[:MENTIONS]->(word:Word)<-[:MENTIONS]-(word{uri:{0}}) return document")
    Iterable<DocumentNode> findByTerm(String uri);

}
