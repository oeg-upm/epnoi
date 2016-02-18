package org.epnoi.model.domain.relations;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by cbadenes on 13/01/16.
 */
//TODO This class should be removed
@Data
public class Relationship implements Serializable{

    public static final String URI="uri";
    String uri;

    public static final String WEIGHT="weight";
    Double weight;

    public Relationship(String uri, Double weight){
        this.uri = uri;
        this.weight = weight;
    }

}
