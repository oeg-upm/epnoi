package org.epnoi.storage.system.column.repository;

import org.epnoi.storage.system.column.domain.TermColumn;
import org.epnoi.storage.system.column.domain.WordColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface TermColumnRepository extends BaseColumnRepository<TermColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from terms where uri = ?0")
    Iterable<TermColumn> findByUri(String uri);

    @Query("select * from terms where creationTime = ?0")
    Iterable<TermColumn> findByCreationTime(String creationTime);

    @Query("select * from terms where content = ?0")
    Iterable<TermColumn> findByContent(String content);

}
