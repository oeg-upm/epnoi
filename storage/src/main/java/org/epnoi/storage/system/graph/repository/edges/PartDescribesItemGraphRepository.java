package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.PartDealsWithTopic;
import org.epnoi.storage.system.graph.domain.edges.PartDescribesItem;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface PartDescribesItemGraphRepository extends RelationGraphRepository<PartDescribesItem> {

    @Query("match (node1{uri:{0}})-[r:DESCRIBES]->(node2{uri:{1}}) return r")
    Iterable<PartDescribesItem> findByNodes(String start, String end);

    @Query("match (:Part)-[r:DESCRIBES]->(:Item)<-[:BUNDLES]-(:Document)<-[:CONTAINS]-(domain{uri:{0}}) return r")
    Iterable<PartDescribesItem> findByDomain(String uri);

}
