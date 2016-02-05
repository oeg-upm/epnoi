package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.DomainContainsDocument;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface DomainContainsDocumentGraphRepository extends RelationGraphRepository<DomainContainsDocument> {

    @Query("match (node1{uri:{0}})-[r:CONTAINS]->(node2{uri:{1}}) return r")
    Iterable<DomainContainsDocument> findByNodes(String start, String end);

    @Query("match (domain{uri:{0}})-[r:CONTAINS]->(:Document) return r")
    Iterable<DomainContainsDocument> findByDomain(String uri);

}
