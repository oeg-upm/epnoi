package org.epnoi.storage.system.graph.repository.nodes;

import org.epnoi.storage.system.graph.domain.nodes.PartNode;
import org.epnoi.storage.system.graph.domain.edges.PartDealsWithTopic;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface PartGraphRepository extends ResourceGraphRepository<PartNode> {

    // To avoid a class type exception
    @Override
    PartNode findOneByUri(String uri);

    @Query("match (part)-[:DESCRIBES]->(item)<-[:BUNDLES]-(document)<-[:CONTAINS]-(domain{uri:{0}}) return part")
    Iterable<PartNode> findByDomain(String uri);

    @Query("match (part)-[:DESCRIBES]->(item{uri:{0}}) return part")
    Iterable<PartNode> findByItem(String uri);

    @Query("match (part{uri:{0}})-[d:DEALS_WITH]->(topic)-[e:EMERGES_IN]->(domain{uri:{1}}) return d")
    Iterable<PartDealsWithTopic> dealsInDomain(String part, String domain);

    @Query("match (in:Part)-[s{domain:{0}}]-(out:Part) delete s")
    void deleteSimilarRelationsInDomain(String uri);

}
