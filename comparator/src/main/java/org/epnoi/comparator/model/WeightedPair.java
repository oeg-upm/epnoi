package org.epnoi.comparator.model;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by cbadenes on 05/02/16.
 */
@ToString
public class WeightedPair implements Serializable{

    @Getter
    String uri1;
    @Getter
    String uri2;
    @Getter
    Double weight;

    public WeightedPair(String uri1, String uri2, Double weight){
        this.uri1 = uri1;
        this.uri2 = uri2;
        this.weight = weight;
    }
}
