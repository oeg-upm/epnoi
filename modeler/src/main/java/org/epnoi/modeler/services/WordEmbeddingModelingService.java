package org.epnoi.modeler.services;

import org.epnoi.model.domain.relations.EmergesIn;
import org.epnoi.model.domain.relations.Relation;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by cbadenes on 11/01/16.
 */
@Component
public class WordEmbeddingModelingService {

    private static final Logger LOG = LoggerFactory.getLogger(WordEmbeddingModelingService.class);

    private ConcurrentHashMap<String,ScheduledFuture<?>> tasks;

    private ThreadPoolTaskScheduler threadpool;

    @Value("${epnoi.modeler.delay}")
    protected Long delay;

    @Autowired
    ModelingHelper helper;

    @PostConstruct
    public void setup(){
        this.tasks = new ConcurrentHashMap<>();

        this.threadpool = new ThreadPoolTaskScheduler();
        this.threadpool.setPoolSize(10);
        this.threadpool.initialize();

    }


    public void buildModels(Relation relation){

        String domainUri = relation.getEndUri();

        // TODO Implement Multi Domain
        LOG.info("Planning a new task to build a word2vec model for words in domain: " + domainUri);

        ScheduledFuture<?> task = tasks.get(domainUri);
        if (task != null) task.cancel(false);
        task = this.threadpool.schedule(new WordEmbeddingModeler(domainUri,helper), new Date(System.currentTimeMillis() + delay));
        tasks.put(domainUri,task);

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
