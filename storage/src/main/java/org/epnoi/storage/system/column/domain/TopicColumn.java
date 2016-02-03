package org.epnoi.storage.system.column.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.Topic;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by cbadenes on 22/12/15.
 */
@Table(value = "topics")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TopicColumn extends Topic{

    @PrimaryKey
    private String uri;
}
