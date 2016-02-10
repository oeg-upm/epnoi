package org.epnoi.learner.service;

import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.learner.scheduler.LearningPoolExecutor;
import org.epnoi.model.domain.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cbadenes on 10/02/16.
 */
@Component
public class LearningService {

    private static final Logger LOG = LoggerFactory.getLogger(LearningService.class);

    private ConcurrentHashMap<String,LearningPoolExecutor> executors;

    @Value("${epnoi.learner.delay}")
    protected Long delay;

    @Autowired
    LearningHelper helper;

    @PostConstruct
    public void setup(){
        this.executors = new ConcurrentHashMap<>();
    }


    public void train(Domain domain){
        LOG.info("Plan a new task to build models for domain: " + domain);
        LearningPoolExecutor executor = executors.get(domain.getUri());
        if (executor == null){
            executor = new LearningPoolExecutor(domain,helper,delay);
        }
        executors.put(domain.getUri(),executor.train());
    }

}
