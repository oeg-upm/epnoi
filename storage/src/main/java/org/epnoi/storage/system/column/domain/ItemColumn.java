package org.epnoi.storage.system.column.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.resources.Item;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by cbadenes on 22/12/15.
 */
@Table(value = "items")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ItemColumn extends Item {

    @PrimaryKey
    private String uri;

}
