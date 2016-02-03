package org.epnoi.model.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(of = "uri", callSuper = true)
public class Word extends Resource {

    private String content;

    private String lemma;

    private String stem;

    private String pos;

    private String type;
}
