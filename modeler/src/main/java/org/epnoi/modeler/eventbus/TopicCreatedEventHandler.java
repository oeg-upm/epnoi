package org.epnoi.modeler.eventbus;

import org.epnoi.model.Event;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.domain.resources.Topic;
import org.epnoi.model.modules.RoutingKey;
import org.epnoi.modeler.services.WordEmbeddingModelingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 11/01/16.
 */
@Component
public class TopicCreatedEventHandler extends AbstractEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(TopicCreatedEventHandler.class);

    @Autowired
    WordEmbeddingModelingService wordEmbeddingModelingService;

    public TopicCreatedEventHandler() {
        super(RoutingKey.of(Resource.Type.TOPIC, Resource.State.CREATED));
    }

    @Override
    public void handle(Event event) {
        LOG.info("Domain updated event received: " + event);
        try{
            Topic topic = event.to(Topic.class);
            wordEmbeddingModelingService.buildModels(topic);
        } catch (Exception e){
            // TODO Notify to event-bus when source has not been added
            LOG.error("Error scheduling a new topic model from domain: " + event, e);
        }
    }
}