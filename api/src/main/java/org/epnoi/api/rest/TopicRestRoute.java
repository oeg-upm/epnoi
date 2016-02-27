package org.epnoi.api.rest;

import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestsDefinition;
import org.epnoi.api.model.relations.*;
import org.epnoi.api.model.resources.TopicI;
import org.epnoi.model.domain.resources.Topic;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 25/02/16.
 */
@Component
public class TopicRestRoute extends RestRoute {

    public TopicRestRoute() {
        super("topics", "topic");
    }

    @Override
    public RestDefinition configure(RestsDefinition definitions) {

        RestDefinition definition = addResourceCRUD(definitions, TopicI.class, Topic.class);
        definition = addRelationCRUD(definition,"domains", AnalysisI.class,EmergesI.class,"where emerges");
        definition = addRelationCRUD(definition,"words",WeightTimesI.class,MentionsI.class,"mentioned by");
        return definition;


    }


}
