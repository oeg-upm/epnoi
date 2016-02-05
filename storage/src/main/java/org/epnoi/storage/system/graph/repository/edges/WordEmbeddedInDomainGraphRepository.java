package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.WordEmbeddedInDomain;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface WordEmbeddedInDomainGraphRepository extends RelationGraphRepository<WordEmbeddedInDomain> {

    @Query("match (node1{uri:{0}})-[r:EMBEDDED_IN]->(node2{uri:{1}}) return r")
    Iterable<WordEmbeddedInDomain> findByNodes(String start, String end);

    @Query("match (:Word)-[r:EMBEDDED_IN]-(domain{uri:{0}}) return r")
    Iterable<WordEmbeddedInDomain> findByDomain(String uri);
}
