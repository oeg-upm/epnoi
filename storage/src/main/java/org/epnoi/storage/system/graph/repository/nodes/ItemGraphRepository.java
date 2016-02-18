package org.epnoi.storage.system.graph.repository.nodes;

import org.epnoi.storage.system.graph.domain.nodes.ItemNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface ItemGraphRepository extends ResourceGraphRepository<ItemNode> {

    // To avoid a class type exception
    @Override
    ItemNode findOneByUri(String uri);

    @Query("match (item)<-[:BUNDLES]-(document)<-[:CONTAINS]-(domain{uri:{0}}) return item")
    Iterable<ItemNode> findByDomain(String uri);

    @Query("match (item)<-[:BUNDLES]-(document{uri:{0}}) return item")
    Iterable<ItemNode> findByDocument(String uri);

    @Query("match (part{uri:{0}})-[:DESCRIBES]->(item) return item")
    Iterable<ItemNode> findByPart(String uri);

}
