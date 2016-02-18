package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.SimilarToPartsEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface SimilarToPartsEdgeRepository extends RelationGraphRepository<SimilarToPartsEdge> {

    @Query("match (node1{uri:{0}})-[r:SIMILAR_TO]->(node2{uri:{1}}) return r")
    Iterable<SimilarToPartsEdge> findByNodes(String start, String end);

    @Query("match (:Part)-[r:SIMILAR_TO]-(:Part)-[:DESCRIBES]->(:Item)<-[:BUNDLES]-(:Document)<-[:CONTAINS]-(domain{uri:{0}}) return r")
    Iterable<SimilarToPartsEdge> findByDomain(String uri);

}
