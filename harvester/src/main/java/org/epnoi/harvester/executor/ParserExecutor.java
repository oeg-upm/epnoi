package org.epnoi.harvester.executor;

import org.epnoi.harvester.annotator.helper.ParserHelper;
import org.epnoi.model.domain.resources.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * Created by cbadenes on 24/02/16.
 */
@Component
public class ParserExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(ParserExecutor.class);

    @Value("${parser.parallel.tasks.threads}")
    int threads;

    @Value("${parser.parallel.tasks.max}")
    int tasks;

    @Autowired
    ParserHelper helper;

    private ThreadPoolExecutor executor;


    @PostConstruct
    public void setup(){
        BlockingQueue<Runnable> worksQueue = new ArrayBlockingQueue<Runnable>(tasks);
        RejectedExecutionHandler executionHandler = new ParserExecutionHandler();

        // Create the ThreadPoolExecutor
        this.executor = new ThreadPoolExecutor(threads, threads, 10, TimeUnit.SECONDS, worksQueue, executionHandler);
        executor.allowCoreThreadTimeOut(true);
    }

    public void parse(File file){
        executor.execute(new ParserTask(file,helper));
        LOG.info("Created task to annotate document: " + file);
    }

}
