package org.epnoi.storage.graph.repository;

import org.epnoi.storage.graph.domain.DocumentNode;
import org.epnoi.storage.graph.domain.relationships.TopicDealtByDocument;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface DocumentGraphRepository extends BaseGraphRepository<DocumentNode> {

    // To avoid a class type exception
    @Override
    DocumentNode findOneByUri(String uri);

    @Query("match (document)<-[:CONTAINS]-(domain{uri:{0}}) return document")
    Iterable<DocumentNode> findByDomain(String uri);

    @Query("match (document{uri:{0}})-[d:DEALS_WITH]->(topic)-[e:EMERGES_IN]->(domain{uri:{1}}) return d")
    Iterable<TopicDealtByDocument> dealsInDomain(String document, String domain);

    @Query("match (in:Document)-[s{domain:{0}}]-(out:Document) delete s")
    void deleteSimilarRelationsInDomain(String uri);

}
