package org.epnoi.storage.system.column.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.resources.Domain;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by cbadenes on 21/12/15.
 */
@Table(value = "domains")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DomainColumn extends Domain {

    @PrimaryKey
    private String uri;

}
