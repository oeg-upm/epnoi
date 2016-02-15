package org.epnoi.learner.helper;

import lombok.Getter;
import org.epnoi.knowledgebase.KnowledgeBase;
import org.epnoi.learner.modules.Learner;
import org.epnoi.nlp.NLPHandler;
import org.epnoi.storage.UDM;
import org.epnoi.storage.generator.TimeGenerator;
import org.epnoi.storage.generator.URIGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 10/02/16.
 */
@Component
public class LearningHelper {

    @Getter
    @Autowired
    URIGenerator uriGenerator;

    @Getter
    @Autowired
    TimeGenerator timeGenerator;

    @Getter
    @Autowired
    UDM udm;

    @Getter
    @Autowired
    Learner learner;

    @Getter
    @Autowired
    NLPHandler nlpHandler;

    @Getter
    @Autowired
    KnowledgeBase knowledgeBase;



}
