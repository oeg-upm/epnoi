package org.epnoi.modeler.services;

import org.epnoi.model.domain.resources.Analysis;
import org.epnoi.model.domain.resources.Domain;
import org.epnoi.model.domain.resources.Topic;
import org.epnoi.modeler.helper.ModelingHelper;
import org.epnoi.modeler.models.word.WordEmbeddingModeler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by cbadenes on 11/01/16.
 */
@Component
public class WordEmbeddingModelingService {

    private static final Logger LOG = LoggerFactory.getLogger(WordEmbeddingModelingService.class);

    // TODO Multi Domain
    //private ConcurrentHashMap<String,ModelingPoolExecutor> executors;

    private ThreadPoolTaskScheduler threadpool;

    private ScheduledFuture<?> task;

    @Value("${epnoi.modeler.delay}")
    protected Long delay;

    @Autowired
    ModelingHelper helper;

    @PostConstruct
    public void setup(){
//        this.executors = new ConcurrentHashMap<>();

        this.threadpool = new ThreadPoolTaskScheduler();
        this.threadpool.setPoolSize(1);
        this.threadpool.initialize();

    }


    public void buildModels(Topic topic){
        // TODO Implement Multi Domain
        LOG.info("Plan a new task to build a word2vec model for words from: " + topic);
        if (task != null) task.cancel(false);
        this.task = this.threadpool.schedule(new WordEmbeddingModeler(topic,helper), new Date(System.currentTimeMillis() + delay));
    }

    public String create(Domain domain){
        throw new RuntimeException("Method not implemented yet");
    }

    public List<Analysis> list(){
        throw new RuntimeException("Method not implemented yet");
    }

    public Analysis get(String uri){
        throw new RuntimeException("Method not implemented yet");
    }

    public Analysis remove(String uri){
        throw new RuntimeException("Method not implemented yet");
    }

    public Analysis update(String uri, Analysis analysis){
        throw new RuntimeException("Method not implemented yet");
    }

}
