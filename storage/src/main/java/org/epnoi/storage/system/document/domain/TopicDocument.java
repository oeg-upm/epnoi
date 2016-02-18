package org.epnoi.storage.system.document.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.domain.resources.Topic;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by cbadenes on 22/12/15.
 */
@Document(indexName="research", type="topics")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TopicDocument extends Topic {

    @Override
    public Resource.Type getResourceType() {return Type.TOPIC;}

    @Id
    private String uri;

    private String domain;
}
