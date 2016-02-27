package org.epnoi.api.model.relations;

import lombok.Data;
import org.epnoi.model.utils.TimeUtils;

/**
 * Created by cbadenes on 22/01/16.
 */
@Data
public class EmbeddedI {

    public EmbeddedI(){};

    public EmbeddedI(String uri, String creationTime, float[] vector){
        this.uri = uri;
        this.creationTime = creationTime;
        this.vector = vector;
    }

    String uri;

    String creationTime = TimeUtils.asISO();

    float[] vector;
}
