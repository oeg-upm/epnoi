package org.epnoi.harvester.eventbus;

import org.epnoi.harvester.services.ParserService;
import org.epnoi.model.Event;
import org.epnoi.model.domain.resources.File;
import org.epnoi.model.modules.BindingKey;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.EventBusSubscriber;
import org.epnoi.model.modules.RoutingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by cbadenes on 01/12/15.
 */
@Component
public class FileCreatedEventHandler implements EventBusSubscriber {

    private static final Logger LOG = LoggerFactory.getLogger(FileCreatedEventHandler.class);

    @Autowired
    ParserService parserService;

    @Autowired
    protected EventBus eventBus;

    @PostConstruct
    public void init(){
        RoutingKey routingKey = RoutingKey.of("file.created");
        LOG.info("Trying to register as subscriber of '" + routingKey + "' events ..");
        eventBus.subscribe(this, BindingKey.of(routingKey, "harvester-file"));
        LOG.info("registered successfully");
    }


    @Override
    public void handle(Event event) {
        LOG.info("New File event received: " + event);
        try{
            parserService.parse(event.to(File.class));
        } catch (RuntimeException e){
            // TODO Notify to event-bus when source has not been added
            LOG.warn(e.getMessage());
        }catch (Exception e){
            // TODO Notify to event-bus when source has not been added
            LOG.error("Error adding new source: " + event, e);
        }
    }
}
