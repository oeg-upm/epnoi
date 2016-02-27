package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.SimilarToDocumentsEdge;
import org.epnoi.storage.system.graph.domain.edges.SimilarToEdge;
import org.epnoi.storage.system.graph.domain.edges.SimilarToItemsEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface SimilarToEdgeRepository extends GraphRepository<SimilarToEdge> {

    // To avoid a class type exception
    SimilarToEdge findOneByUri(String uri);

    @Query("match (node1:Document{uri:{0}})-[r:SIMILAR_TO]->(node2:Document{uri:{1}}) return r")
    Iterable<SimilarToDocumentsEdge> findDocumentsByNodes(String start, String end);

//    @Query("match (node1:Item{uri:{0}})-[r:SIMILAR_TO]->(node2:Item{uri:{1}}) return r")
//    Iterable<SimilarToItemsEdge> findItemsByNodes(String start, String end);

//    @Query("match (domain{uri:{0}})-[c:CONTAINS]->(d1:Document)-[r:SIMILAR_TO]->(d2:Document) return r")
//    Iterable<SimilarToDocumentsEdge> findByDomain(String uri);
////
//    @Query("match (document{uri:{0}})-[r:SIMILAR_TO]-(doc2) return r")
//    Iterable<SimilarToDocumentsEdge> findByDocument(String uri);

}
