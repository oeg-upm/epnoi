package org.epnoi.learner.scheduler;

import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.model.domain.resources.Analysis;
import org.epnoi.model.domain.resources.Domain;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.utils.TimeUtils;
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
        analysis.setCreationTime(TimeUtils.asISO());
        analysis.setDomain(domain.getUri());
        analysis.setType(type);
        analysis.setDescription(description);
        analysis.setConfiguration(configuration);
        return analysis;
    }

    @Override
    public void run() {
        //TODO

//        // Create Relational Sentences Corpus
//        Parameters<Object> runtimeParameters = new Parameters<Object>();
//
//        String uri = domain.getUri();
//        runtimeParameters.setParameter(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI, uri);
//        runtimeParameters.setParameter(RelationalSentencesCorpusCreationParameters.MAX_TEXT_CORPUS_SIZE, textCorpusMaxSize);
//
//
//        helper.getLearner().getTrainer().createRelationalSentencesCorpus(runtimeParameters);
//        URI createdResourceUri = null;
//        if (runtimeParameters.getParameterValue(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI) != null) {
//
//            createdResourceUri =
//                    UriBuilder.fromUri((String) helper.getLearner().getTrainer().getRuntimeParameters()
//                            .getParameterValue(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI)).build();
//        } else {
//            createdResourceUri =
//                    UriBuilder.fromUri((String) helper.getLearner().getTrainer().getRelationalSentencesCorpusCreationParameters()
//                            .getParameterValue(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI)).build();
//
//        }
//
//        // Create Relational Patterns Model
////        Parameters<Object> runtimeParameters = new Parameters<Object>();
//        runtimeParameters.setParameter(RelationalPatternsModelCreationParameters.MODEL_PATH, modelpath);
//        helper.getLearner().getTrainer().createRelationalPatternsModel(runtimeParameters);
//
//        URI uri =
//                UriBuilder.fromUri((String) learner.getTrainer().getRelationalSentencesCorpusCreationParameters().getParameterValue(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI)).build();


    }
}
