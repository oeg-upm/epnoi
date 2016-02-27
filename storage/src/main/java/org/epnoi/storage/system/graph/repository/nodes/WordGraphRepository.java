package org.epnoi.storage.system.graph.repository.nodes;

import org.epnoi.storage.system.graph.domain.nodes.WordNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface WordGraphRepository extends ResourceGraphRepository<WordNode> {

    @Override
    WordNode findOneByUri(String uri);

    @Query("match (w)-[:PAIRS_WITH]->(word{uri:{0}}) return w")
    Iterable<WordNode> findByWord(String uri);

    @Query("match (w:Word)-[:EMBEDDED_IN]->(domain{uri:{0}}) return w")
    Iterable<WordNode> findByDomain(String uri);

    @Query("match (w:Word)<-[:MENTIONS]-(term{uri:{0}}) return w")
    Iterable<WordNode> findByTerm(String uri);

    @Query("match (w:Word)<-[:MENTIONS]-(topic{uri:{0}}) return w")
    Iterable<WordNode> findByTopic(String uri);

    @Query("match (in:Word)-[s{domain:{0}}]->(out:Word) delete s")
    void deletePairingInDomain(String uri);

    @Query("match (in:Word)-[e:EMBEDDED_IN]->(domain{uri:{0}}) delete e")
    void deleteEmbeddingInDomain(String uri);




}
