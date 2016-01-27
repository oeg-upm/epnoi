package org.epnoi.storage.graph.repository;

import org.epnoi.storage.graph.domain.TopicNode;
import org.epnoi.storage.graph.domain.WordNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface WordGraphRepository extends BaseGraphRepository<WordNode> {

    @Override
    WordNode findOneByUri(String uri);

    //@Query("match (w:Word)-[:EMBEDDED_IN]->(domain{uri:{0}}) return w")
    // match (w:Word)<-[:MENTIONS]-(Topic)-[:EMERGES_IN]->(domain {uri : 'http://epnoi.org/domains/382130c5-1d84-4b21-a591-90d2c235f0a5'}) return w
    @Query("match (w:Word)<-[:MENTIONS]-(Topic)-[:EMERGES_IN]->(domain{uri:{0}}) return w")
    Iterable<WordNode> findByDomain(String uri);

    @Query("match (in:Word)-[s{domain:{0}}]->(out:Word) delete s")
    void deletePairingInDomain(String uri);

    @Query("match (in:Word)-[e:EMBEDDED_IN]->(domain{uri:{0}}) delete e")
    void deleteEmbeddingInDomain(String uri);

}
