package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.SimilarToItemsEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface SimilarToItemsEdgeRepository extends RelationGraphRepository<SimilarToItemsEdge> {

    // To avoid a class type exception
    @Override
    SimilarToItemsEdge findOneByUri(String uri);

//    @Query("match (node1{uri:{0}})-[r:SIMILAR_TO]->(node2) return r")
//    Iterable<SimilarToItemsEdge> fromNode(String start, String end);

    @Query("match (node1{uri:{0}})-[r:SIMILAR_TO]->(node2{uri:{1}}) return r")
    Iterable<SimilarToItemsEdge> findByNodes(String start, String end);

    @Query("match (domain{uri:{0}})-[:CONTAINS]->(:Document)-[:BUNDLES]->(:Item)-[r:SIMILAR_TO]-(:Item) return r")
    Iterable<SimilarToItemsEdge> findByDomain(String uri);

    @Query("match (item{uri:{0}})-[r:SIMILAR_TO]-(i) return r")
    Iterable<SimilarToItemsEdge> findByItem(String uri);
}
