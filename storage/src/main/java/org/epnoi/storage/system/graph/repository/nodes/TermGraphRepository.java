package org.epnoi.storage.system.graph.repository.nodes;

import org.epnoi.storage.system.graph.domain.nodes.TermNode;
import org.epnoi.storage.system.graph.domain.nodes.WordNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface TermGraphRepository extends ResourceGraphRepository<TermNode> {

    @Override
    TermNode findOneByUri(String uri);

    @Query("match (term)-[:APPEARED_IN]->(domain{uri:{0}}) return term")
    Iterable<TermNode> findByDomain(String uri);

    @Query("match (term)-[:MENTIONS]->(word{uri:{0}}) return term")
    Iterable<TermNode> findByWord(String uri);

    @Query("match (term)-[:HYPERNYM_OF]->(t{uri:{0}}) return term")
    Iterable<TermNode> findByTerm(String uri);

    @Query("match (term)-[:MENTIONS]->(word)<-[:MENTIONS]-(topic{uri:{0}}) return term")
    Iterable<TermNode> findByTopic(String uri);

}
