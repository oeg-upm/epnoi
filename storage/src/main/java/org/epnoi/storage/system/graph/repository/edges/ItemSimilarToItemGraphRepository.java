package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.ItemDealsWithTopic;
import org.epnoi.storage.system.graph.domain.edges.ItemSimilarToItem;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface ItemSimilarToItemGraphRepository extends RelationGraphRepository<ItemSimilarToItem> {

    @Query("match (node1{uri:{0}})-[r:SIMILAR_TO]->(node2{uri:{1}}) return r")
    Iterable<ItemSimilarToItem> findByNodes(String start, String end);

    @Query("match (domain{uri:{0}})-[:CONTAINS]->(:Document)-[:BUNDLES]->(:Item)-[r:SIMILAR_TO]-(:Item) return r")
    Iterable<ItemSimilarToItem> findByDomain(String uri);

}
