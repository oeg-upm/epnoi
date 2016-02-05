package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.SourceProvidesDocument;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface SourceProvidesDocumentGraphRepository extends RelationGraphRepository<SourceProvidesDocument> {

    @Query("match (node1{uri:{0}})-[r:PROVIDES]->(node2{uri:{1}}) return r")
    Iterable<SourceProvidesDocument> findByNodes(String start, String end);

    @Query("match (:Source)-[r:PROVIDES]->(:Document)<-[:CONTAINS]-(domain{uri:{0}}) return r")
    Iterable<SourceProvidesDocument> findByDomain(String uri);

}
