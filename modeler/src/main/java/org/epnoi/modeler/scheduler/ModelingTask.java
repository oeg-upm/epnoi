package org.epnoi.modeler.scheduler;

import org.epnoi.model.domain.resources.Analysis;
import org.epnoi.model.domain.resources.Document;
import org.epnoi.model.domain.resources.Domain;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.utils.TimeUtils;
import org.epnoi.modeler.helper.ModelingHelper;
import org.epnoi.modeler.models.topic.TopicModeler;
import org.epnoi.modeler.models.word.WordEmbeddingModeler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 13/01/16.
 */
public class ModelingTask implements Runnable{

    private static final Logger LOG = LoggerFactory.getLogger(ModelingTask.class);

    protected final ModelingHelper helper;
    protected final Document document;

    public ModelingTask(Document document, ModelingHelper modelingHelper){
        this.document = document;
        this.helper = modelingHelper;
    }

    protected Analysis newAnalysis(String type, String configuration, String description, String domainUri){

        Analysis analysis = Resource.newAnalysis();
        analysis.setUri(helper.getUriGenerator().newFor(Resource.Type.ANALYSIS));
        analysis.setCreationTime(TimeUtils.asISO());
        analysis.setDomain(domainUri);
        analysis.setType(type);
        analysis.setDescription(description);
        analysis.setConfiguration(configuration);
        return analysis;
    }

    @Override
    public void run() {
        //TODO Parallelize
        helper.getModelBuilder().execute(new TopicModeler(document,helper));
        helper.getModelBuilder().execute(new WordEmbeddingModeler(document,helper));
    }
}
