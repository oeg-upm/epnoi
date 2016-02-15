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

    @Query("match (t:Term)-[:APPEARED_IN]->(domain{uri:{0}}) return t")
    Iterable<TermNode> findByDomain(String uri);

    @Query("match (in:Domain)-[e:APPEARED_IN]->(domain{uri:{0}}) delete e")
    void deleteAppearedInDomain(String uri);

}
