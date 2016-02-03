package org.epnoi.storage.system.document.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.Domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by cbadenes on 22/12/15.
 */
@Document(indexName="research", type="domains")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DomainDocument extends Domain {

    @Id
    private String uri;
}
