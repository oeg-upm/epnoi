package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.DocumentBundlesItem;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface DocumentBundlesItemGraphRepository extends RelationGraphRepository<DocumentBundlesItem> {

    @Query("match (node1{uri:{0}})-[r:BUNDLES]->(node2{uri:{1}}) return r")
    Iterable<DocumentBundlesItem> findByNodes(String start, String end);

    @Query("match (domain{uri:{0}})-[:CONTAINS]->(:Document)-[r:BUNDLES]->(:Item) return r")
    Iterable<DocumentBundlesItem> findByDomain(String uri);


}
