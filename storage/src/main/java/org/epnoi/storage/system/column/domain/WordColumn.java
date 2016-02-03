package org.epnoi.storage.system.column.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.Word;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by cbadenes on 22/12/15.
 */
@Table(value = "words")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WordColumn extends Word{

    @PrimaryKey
    private String uri;

    private String content;

    private String lemma;

    private String stem;

    private String pos;

    private String type;
}
