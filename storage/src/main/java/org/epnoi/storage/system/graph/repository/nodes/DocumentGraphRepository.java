package org.epnoi.storage.system.graph.repository.nodes;

import org.epnoi.storage.system.graph.domain.nodes.DocumentNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface DocumentGraphRepository extends ResourceGraphRepository<DocumentNode> {

    // To avoid a class type exception
    @Override
    DocumentNode findOneByUri(String uri);

    @Query("match (document)<-[:CONTAINS]-(domain{uri:{0}}) return document")
    Iterable<DocumentNode> findByDomain(String uri);

    @Query("match (document)<-[:PROVIDES]-(source{uri:{0}}) return document")
    Iterable<DocumentNode> findBySource(String uri);

}
