package org.epnoi.storage.system.column.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.Document;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by cbadenes on 22/12/15.
 */
@Table(value = "documents")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DocumentColumn extends Document {

    @PrimaryKey
    private String uri;

}
