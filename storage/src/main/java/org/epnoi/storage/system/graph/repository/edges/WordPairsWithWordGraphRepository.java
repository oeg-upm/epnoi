package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.storage.system.graph.domain.edges.WordPairsWithWord;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface WordPairsWithWordGraphRepository extends RelationGraphRepository<WordPairsWithWord> {

    @Query("match (node1{uri:{0}})-[r:PAIRS_WITH]->(node2{uri:{1}}) return r")
    Iterable<WordPairsWithWord> findByNodes(String start, String end);

    @Query("match (:Word)<-[r:PAIRS_WITH]-(:Word)-[r:EMBEDDED_IN]-(domain{uri:{0}}) return r")
    Iterable<WordPairsWithWord> findByDomain(String uri);

}
