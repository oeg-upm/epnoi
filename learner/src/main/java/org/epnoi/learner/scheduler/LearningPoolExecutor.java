package org.epnoi.learner.scheduler;

import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.model.domain.resources.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by cbadenes on 10/02/16.
 */
public class LearningPoolExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(LearningPoolExecutor.class);

    private final Domain domain;
    private final long delay;
    private final LearningHelper helper;

    private ThreadPoolTaskScheduler threadpool;
    private ScheduledFuture<?> task;


    public LearningPoolExecutor(Domain domain, LearningHelper helper, long delayInMsecs) {
        this.domain = domain;
        this.delay  = delayInMsecs;
        this.helper = helper;

        this.threadpool = new ThreadPoolTaskScheduler();
        this.threadpool.setPoolSize(1);
        this.threadpool.initialize();

        LOG.info("created a new learning executor delayed by: " + delayInMsecs + "msecs for domain: " + domain);
    }

    public LearningPoolExecutor train(){
        LOG.info("scheduling a new training task");
        if (task != null) task.cancel(false);
        this.task = this.threadpool.schedule(new LearningTask(domain,helper), new Date(System.currentTimeMillis() + delay));
        return this;
    }
}