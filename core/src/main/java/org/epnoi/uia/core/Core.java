package org.epnoi.uia.core;

import lombok.Getter;
import org.epnoi.model.modules.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Core {

    private static final Logger logger = LoggerFactory.getLogger(Core.class);

    @Getter
    @Autowired
    private SearchHandler searchHandler;

    @Getter
    @Autowired
    private AnnotationHandler annotationHandler;

    @Getter
    @Autowired
    private InformationHandler informationHandler;

    @Getter
    @Autowired
    private DomainsHandler domainsHandler = null;

    @Getter
    @Autowired(required = false)
    private InformationSourcesHandler informationSourcesHandler;

    @Getter
    @Autowired (required = false)
    private HarvestersHandler harvestersHandler;
}
