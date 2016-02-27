package org.epnoi.api.model.relations;

import lombok.Data;
import org.epnoi.model.utils.TimeUtils;

/**
 * Created by cbadenes on 22/01/16.
 */
@Data
public class SimilarI {

    public SimilarI(){};

    public SimilarI(String uri, String creationTime, Double weight, String domain){
        this.uri = uri;
        this.creationTime = creationTime;
        this.weight = weight;
        this.domain = domain;
    }

    String uri;

    String creationTime = TimeUtils.asISO();

    Double weight;

    String domain;
}
