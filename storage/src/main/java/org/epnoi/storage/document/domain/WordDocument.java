package org.epnoi.storage.document.domain;

import lombok.Data;
import lombok.ToString;
import org.epnoi.storage.model.Word;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by cbadenes on 22/12/15.
 */
@Document(indexName="research", type="words")
@Data
@ToString(callSuper = true)
public class WordDocument extends Word {

    @Id
    private String uri;

    private String domain;
}
