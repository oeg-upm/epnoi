package org.epnoi.comparator.model;

import lombok.Getter;
import org.epnoi.model.domain.relations.Relationship;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cbadenes on 05/02/16.
 */
public class TopicDistribution implements Serializable {

    @Getter
    String uri;

    @Getter
    List<Relationship> relationships;

    public TopicDistribution(String uri, Iterable<WeightedPair> weightedPairs) {
        this.uri = uri;
        this.relationships = new ArrayList<>();

        if (weightedPairs != null){
            for (WeightedPair weightedPair : weightedPairs) {
                this.relationships.add(new Relationship(weightedPair.getUri2(),weightedPair.getWeight()));
            }
        }

    }
}
