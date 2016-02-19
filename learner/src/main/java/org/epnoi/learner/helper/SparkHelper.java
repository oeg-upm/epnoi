package org.epnoi.learner.helper;

import lombok.Getter;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by cbadenes on 19/02/16.
 */
@Component
public class SparkHelper{

    @Value("${epnoi.learner.spark.threads}")
    String threads; // 24

    @Value("${epnoi.learner.spark.memory}")
    String memory; // 3g

    private SparkConf conf;

    @Getter
    private JavaSparkContext sc;


    @PostConstruct
    public void setup(){

        // Initialize Spark Context
        this.conf = new SparkConf().
                setMaster("local["+threads+"]").
                setAppName("DrInventor-Learner").
                set("spark.executor.memory", memory).
                set("spark.driver.maxResultSize","0");
        sc = new JavaSparkContext(conf);
    }

}
