package org.epnoi.storage.system.document.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by cbadenes on 22/12/15.
 */
@Document(indexName="research", type="documents")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DocumentDocument extends org.epnoi.model.domain.Document {

    @Id
    private String uri;

    private String domain;
}
