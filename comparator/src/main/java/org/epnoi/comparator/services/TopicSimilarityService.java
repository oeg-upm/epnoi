package org.epnoi.comparator.services;

import org.epnoi.comparator.helper.ComparatorHelper;
import org.epnoi.comparator.similarity.SimilarityExecutor;
import org.epnoi.comparator.tasks.DocumentSimilarityTask;
import org.epnoi.comparator.tasks.ItemSimilarityTask;
import org.epnoi.comparator.tasks.PartSimilarityTask;
import org.epnoi.storage.model.Analysis;
import org.epnoi.storage.model.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by cbadenes on 13/01/16.
 */
@Component
public class TopicSimilarityService {

    private static final Logger LOG = LoggerFactory.getLogger(TopicSimilarityService.class);

    @Autowired
    ComparatorHelper helper;

    @Autowired
    SimilarityExecutor executor;

    @PostConstruct
    public void setup(){

    }


    public void calculate(Analysis analysis){
        LOG.info("Planning a new calculus of semantic similarity based on Topic Modeling from Analysis: " + analysis);

        switch(analysis.getDescription().toLowerCase()){
            case "document":
                LOG.info("taking into account the Documents");
                executor.execute(new DocumentSimilarityTask(analysis,helper));
                break;
            case "item":
                LOG.info("taking into account the Items");
                executor.execute(new ItemSimilarityTask(analysis,helper));
                break;
            case "part":
                LOG.info("taking into account the Parts");
                executor.execute(new PartSimilarityTask(analysis,helper));
                break;

        }
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
