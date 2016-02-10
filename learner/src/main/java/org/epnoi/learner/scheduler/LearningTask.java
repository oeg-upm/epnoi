package org.epnoi.learner.scheduler;

import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.model.domain.Analysis;
import org.epnoi.model.domain.Domain;
import org.epnoi.model.domain.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 10/02/16.
 */
public class LearningTask implements Runnable{

    private static final Logger LOG = LoggerFactory.getLogger(LearningTask.class);

    protected final Domain domain;
    protected final LearningHelper helper;

    public LearningTask(Domain domain, LearningHelper modelingHelper){
        this.domain = domain;
        this.helper = modelingHelper;
    }

    protected Analysis newAnalysis(String type, String configuration, String description){

        Analysis analysis = new Analysis();
        analysis.setUri(helper.getUriGenerator().newFor(Resource.Type.ANALYSIS));
        analysis.setCreationTime(helper.getTimeGenerator().asISO());
        analysis.setDomain(domain.getUri());
        analysis.setType(type);
        analysis.setDescription(description);
        analysis.setConfiguration(configuration);
        return analysis;
    }

    @Override
    public void run() {
        //TODO
    }
}
