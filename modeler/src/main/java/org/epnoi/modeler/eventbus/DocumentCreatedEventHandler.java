package org.epnoi.modeler.eventbus;

import org.epnoi.model.Event;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Document;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.modules.BindingKey;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.EventBusSubscriber;
import org.epnoi.model.modules.RoutingKey;
import org.epnoi.modeler.services.TopicModelingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by cbadenes on 11/01/16.
 */
@Component
public class DocumentCreatedEventHandler implements EventBusSubscriber {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentCreatedEventHandler.class);

    @Autowired
    protected EventBus eventBus;

    @Autowired
    TopicModelingService topicModelingService;

    @PostConstruct
    public void init(){
        BindingKey bindingKey = BindingKey.of(RoutingKey.of(Relation.Type.CONTAINS, Relation.State.CREATED), "topic-modeler");
        LOG.info("Trying to register as subscriber of '" + bindingKey + "' events ..");
        eventBus.subscribe(this,bindingKey );
        LOG.info("registered successfully");
    }

    @Override
    public void handle(Event event) {
        LOG.info("Document created event received: " + event);
        try{
            Relation relation = event.to(Relation.class);
            topicModelingService.buildModels(relation);
        } catch (Exception e){
            // TODO Notify to event-bus when source has not been added
            LOG.error("Error scheduling a new topic model from domain: " + event, e);
        }
    }
}