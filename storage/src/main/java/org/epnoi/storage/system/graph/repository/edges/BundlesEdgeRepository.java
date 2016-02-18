package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.BundlesEdge;
import org.epnoi.storage.system.graph.domain.nodes.DocumentNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface BundlesEdgeRepository extends RelationGraphRepository<BundlesEdge> {

    // To avoid a class type exception
    @Override
    BundlesEdge findOneByUri(String uri);

    @Query("match (node1{uri:{0}})-[r:BUNDLES]->(node2{uri:{1}}) return r")
    Iterable<BundlesEdge> findByNodes(String start, String end);

    @Query("match (domain{uri:{0}})-[:CONTAINS]->(:Document)-[r:BUNDLES]->(:Item) return r")
    Iterable<BundlesEdge> findByDomain(String uri);


}
