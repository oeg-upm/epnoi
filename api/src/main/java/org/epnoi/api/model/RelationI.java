package org.epnoi.api.model;

import lombok.Data;

/**
 * Created by cbadenes on 22/01/16.
 */
@Data
public class RelationI {

    public RelationI(){}

    public RelationI(String uri, Double weight){
        this.uri = uri;
        this.weight = weight;
    }

    Double weight;

    String uri;
}
