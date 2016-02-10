package org.epnoi.knowledgebase;

import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Component
public class KnowledgeBaseHandler {


    private static final Logger logger = Logger.getLogger(KnowledgeBaseHandler.class.getName());

    @Autowired
    KnowledgeBaseFactory knowledgeBaseCreator;

    private KnowledgeBase knowledgeBase;


    @PostConstruct
    public void init() throws EpnoiInitializationException {

        try {
            this.knowledgeBase = knowledgeBaseCreator.build();
        } catch (EpnoiInitializationException e) {
            logger.severe("The KnowledgeBase couldn't be initialized");
            throw new EpnoiInitializationException(e.getMessage());
        }
    }


    public synchronized KnowledgeBase getKnowledgeBase()
            throws EpnoiInitializationException, EpnoiResourceAccessException {
        return this.knowledgeBase;
    }
}
