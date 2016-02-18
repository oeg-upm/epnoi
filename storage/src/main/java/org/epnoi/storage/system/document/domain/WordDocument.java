package org.epnoi.storage.system.document.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.domain.resources.Word;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by cbadenes on 22/12/15.
 */
@Document(indexName="research", type="words")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class WordDocument extends Word {

    @Override
    public Resource.Type getResourceType() {return Type.WORD;}

    @Id
    private String uri;

    private String domain;
}
