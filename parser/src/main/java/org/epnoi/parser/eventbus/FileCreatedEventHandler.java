package org.epnoi.parser.eventbus;

import org.epnoi.model.Event;
import org.epnoi.model.domain.File;
import org.epnoi.model.modules.RoutingKey;
import org.epnoi.parser.service.ParserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 01/12/15.
 */
@Component
public class FileCreatedEventHandler extends AbstractEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(FileCreatedEventHandler.class);

    @Autowired
    ParserService parserService;

    public FileCreatedEventHandler() {
        super(RoutingKey.of("file.created"));
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
