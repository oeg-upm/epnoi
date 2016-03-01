package org.epnoi.harvester.services;

import org.epnoi.harvester.annotator.TextAnnotator;
import org.epnoi.harvester.executor.ParserExecutor;
import org.epnoi.model.domain.resources.File;
import org.epnoi.storage.UDM;
import org.epnoi.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 11/02/16.
 */
@Component
public class ParserService {

    private static final Logger LOG = LoggerFactory.getLogger(ParserService.class);

    @Autowired
    UDM udm;

    @Autowired
    URIGenerator uriGenerator;

    @Autowired
    TextAnnotator textMiner;

    @Autowired
    ParserExecutor executor;

    public void parse(File file){
        executor.parse(file);
    }


}
