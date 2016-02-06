package org.epnoi.comparator.tasks;

import org.apache.spark.api.java.JavaRDD;
import org.epnoi.comparator.helper.ComparatorHelper;
import org.epnoi.comparator.model.TopicDistribution;
import org.epnoi.comparator.model.WeightedPair;
import org.epnoi.comparator.similarity.RelationalSimilarity;
import org.epnoi.model.domain.Analysis;
import org.epnoi.model.domain.Relation;
import org.epnoi.model.domain.RelationProperties;
import org.epnoi.model.domain.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by cbadenes on 05/02/16.
 */
public abstract class SimilarityTask implements Runnable,Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(SimilarityTask.class);

    protected final Analysis analysis;
    protected final ComparatorHelper helper;
    private final Relation.Type dealsType;
    private final Relation.Type similarityType;


    public SimilarityTask(Analysis analysis, ComparatorHelper helper, Relation.Type dealsType, Relation.Type similarityType){
        this.analysis = analysis;
        this.helper = helper;
        this.dealsType = dealsType;
        this.similarityType = similarityType;
    }

    @Override
    public void run() {
        // Delete previous similarities
        // TODO helper.getUdm().delete(similarityType).in(Resource.Type.DOMAIN, analysis.getDomain());
        Iterable<Relation> existingSimilarities = helper.getUdm().find(similarityType).in(Resource.Type.DOMAIN, analysis.getDomain());
        LOG.debug("deleting existing " + similarityType + " : " + existingSimilarities);
        if (existingSimilarities != null){
            for (Relation existingSimilarity : existingSimilarities) {
                helper.getUdm().detachFrom(existingSimilarity.getStart().getUri()).to(existingSimilarity.getEnd().getUri()).by(similarityType);
            }
        }

        // Get topic distributions
        Iterable<Relation> relations = helper.getUdm().find(dealsType).in(Resource.Type.DOMAIN, analysis.getDomain());
        LOG.debug("Read "+dealsType+": " + relations);

        // Calculate Similarities
        List<WeightedPair> similarities = compute(StreamSupport.stream(relations.spliterator(), false).map(rel -> new WeightedPair(rel.getStart().getUri(), rel.getEnd().getUri(), rel.getWeight())).collect(Collectors.toList()));

        // Save similarities in ddbb
        for (WeightedPair pair: similarities){
            LOG.info("Attaching "+similarityType+" based on " + pair);
            helper.getUdm().attachFrom(pair.getUri1()).to(pair.getUri2()).by(similarityType, RelationProperties.builder().weight(pair.getWeight()).domain(analysis.getDomain()).build());
            helper.getUdm().attachFrom(pair.getUri2()).to(pair.getUri1()).by(similarityType, RelationProperties.builder().weight(pair.getWeight()).domain(analysis.getDomain()).build());
        }

    }

    protected List<WeightedPair> compute(List<WeightedPair> pairs){

        LOG.info("Computing "+similarityType+" based on Topic Models..");

        JavaRDD<TopicDistribution> topicDistributions = helper.getSparkHelper().getSc().parallelize(pairs).
                mapToPair(x -> new Tuple2<String, WeightedPair>(x.getUri1(), x)).
                groupByKey().
                map(x -> new TopicDistribution(x._1(), x._2()))
                ;

        LOG.debug("Topic Distributions: "+topicDistributions.collect().size());

        List<WeightedPair> similarities = topicDistributions.
                cartesian(topicDistributions).
                filter(x -> x._1().getUri().compareTo(x._2().getUri()) > 0).
                map(x -> new WeightedPair(x._1().getUri(), x._2().getUri(), RelationalSimilarity.between(x._1().getRelationships(), x._2().getRelationships()))).
//                filter(x -> x.getWeight() > 0.5).
                collect();

        LOG.debug("Similarities: "+similarities);
        return similarities;

    }
}
