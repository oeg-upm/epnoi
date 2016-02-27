package org.epnoi.api.model.relations;

import lombok.Data;
import org.epnoi.model.utils.TimeUtils;

/**
 * Created by cbadenes on 22/01/16.
 */
@Data
public class DealsI {

    public DealsI(){};

    public DealsI(String uri, String creationTime, Double weight){
        this.uri = uri;
        this.creationTime = creationTime;
        this.weight = weight;
    }

    String uri;

    String creationTime = TimeUtils.asISO();

    Double weight;
}
