package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.DocumentSimilarToDocument;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface DocumentSimilarToDocumentGraphRepository extends RelationGraphRepository<DocumentSimilarToDocument> {


    @Query("match (node1{uri:{0}})-[r:SIMILAR_TO]-(node2{uri:{1}}) return r")
    Iterable<DocumentSimilarToDocument> findByNodes(String start, String end);

    @Query("match (domain{uri:{0}})-[:CONTAINS]->(:Document)-[r:SIMILAR_TO]-(:Document) return r")
    Iterable<DocumentSimilarToDocument> findByDomain(String uri);

}
