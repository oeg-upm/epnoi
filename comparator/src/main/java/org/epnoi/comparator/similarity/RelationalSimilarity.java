package org.epnoi.comparator.similarity;

import es.upm.oeg.epnoi.matching.metrics.similarity.JensenShannonSimilarity;
import org.epnoi.model.domain.relations.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by cbadenes on 13/01/16.
 */
public class RelationalSimilarity implements Serializable{

    private static final Logger LOG = LoggerFactory.getLogger(RelationalSimilarity.class);

    public static Double between(List<Relationship> relationships1,List<Relationship> relationships2){

        if (relationships1.isEmpty() || relationships2.isEmpty()) return 0.0;

        Comparator<Relationship> byUri = (e1, e2) ->e1.getUri().compareTo(e2.getUri());

        double[] weights1 = relationships1.stream().sorted(byUri).mapToDouble(x -> x.getWeight()).toArray();
        double[] weights2 = relationships2.stream().sorted(byUri).mapToDouble(x -> x.getWeight()).toArray();

        LOG.debug("weight1: " + Arrays.toString(weights1) + " - weights2:" + Arrays.toString(weights2));

        return JensenShannonSimilarity.apply(weights1, weights2);
    }
}
