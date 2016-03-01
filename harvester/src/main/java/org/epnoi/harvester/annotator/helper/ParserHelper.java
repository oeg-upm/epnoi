package org.epnoi.harvester.annotator.helper;

import lombok.Getter;
import lombok.Setter;
import org.epnoi.harvester.annotator.TextAnnotator;
import org.epnoi.model.modules.EventBus;
import org.epnoi.storage.UDM;
import org.epnoi.storage.generator.URIGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 24/02/16.
 */
@Component
public class ParserHelper {

    @Getter @Setter
    @Value("${parser.serializer.directory}")
    String serializationDirectory;

    @Getter
    @Autowired
    UDM udm;

    @Getter
    @Autowired
    EventBus eventBus;

    @Getter
    @Autowired
    TextAnnotator textMiner;

    @Getter
    @Autowired
    URIGenerator uriGenerator;

}
