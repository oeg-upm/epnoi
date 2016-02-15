package org.epnoi.storage.system.column.repository;

import org.epnoi.storage.system.column.domain.SerializedObjectColumn;
import org.epnoi.storage.system.column.domain.WordColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface SerializedObjectColumnRepository extends BaseColumnRepository<SerializedObjectColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from serializations where uri = ?0")
    Iterable<WordColumn> findByUri(String uri);

    @Query("select * from serializations where creationTime = ?0")
    Iterable<WordColumn> findByCreationTime(String creationTime);

}
