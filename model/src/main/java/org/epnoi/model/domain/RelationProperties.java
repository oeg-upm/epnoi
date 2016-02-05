package org.epnoi.model.domain;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by cbadenes on 04/02/16.
 */
@Builder
public class RelationProperties {

    @Getter
    Double weight;

    @Getter
    String date;

    @Getter
    String domain;

    @Getter
    String description;

    @Getter
    Long times;

}
