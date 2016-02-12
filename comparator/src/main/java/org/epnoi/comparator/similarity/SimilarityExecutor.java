package org.epnoi.comparator.similarity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * Created by cbadenes on 25/01/16.
 */
@Component
public class SimilarityExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(SimilarityExecutor.class);

    @Value("${epnoi.comparator.delay}")
    protected Long delay;

    ThreadPoolTaskScheduler executor;

    @PostConstruct
    public void setup(){
        executor = new ThreadPoolTaskScheduler();
        executor.setPoolSize(3);
        executor.initialize();
    }


    public void execute(Runnable task){
        LOG.info("Added to queue similarity task: " + task + " and delayed: " + delay + "msecs");
        //this.executor.execute(task);
        this.executor.schedule(task, new Date(System.currentTimeMillis() + delay));
    }

}
