package org.epnoi.storage.system.graph.repository.nodes;

import org.epnoi.storage.system.graph.domain.nodes.TopicNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface TopicGraphRepository extends ResourceGraphRepository<TopicNode> {

    // To avoid a class type exception
    @Override
    TopicNode findOneByUri(String uri);

    @Query("match (topic)-[:EMERGES_IN]->(domain{uri:{0}}) return topic")
    Iterable<TopicNode> findByDomain(String uri);

}
