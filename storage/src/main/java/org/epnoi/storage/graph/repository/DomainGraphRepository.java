package org.epnoi.storage.graph.repository;

import org.epnoi.storage.graph.domain.DomainNode;
import org.epnoi.storage.graph.domain.relationships.TopicDealtByDocument;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface DomainGraphRepository extends BaseGraphRepository<DomainNode> {

    // To avoid a class type exception
    @Override
    DomainNode findOneByUri(String uri);

    @Query("match (in:Domain)-[s{domain:{0}}]-(out:Domain) delete s")
    void deleteSimilarRelations(String uri);

    @Query("match (source{uri:{0}})-[:COMPOSES]->(d:Domain) return d")
    Iterable<DomainNode> findBySource(String source);

    @Query("match (d:Domain)-[:CONTAINS]->(doc{uri:{0}}) return d")
    Iterable<DomainNode> findByDocument(String document);

    @Query("match (d:Domain)-[:CONTAINS]->(doc:Document)-[:BUNDLES]->(it{uri:{0}}) return d")
    Iterable<DomainNode> findByItem(String item);

    @Query("match (d:Domain)-[:CONTAINS]->(doc:Document)-[:BUNDLES]->(it:Item)<-[:DESCRIBES]-(p{uri:{0}}) return d")
    Iterable<DomainNode> findByPart(String part);

    @Query("match (d:Domain)<-[:EMBEDDED_IN]-(w{uri:{0}}) return d")
    Iterable<DomainNode> findByWord(String word);
}
