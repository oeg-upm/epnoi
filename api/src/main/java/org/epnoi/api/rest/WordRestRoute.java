package org.epnoi.api.rest;

import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestsDefinition;
import org.epnoi.api.model.relations.*;
import org.epnoi.api.model.resources.TopicI;
import org.epnoi.api.model.resources.WordI;
import org.epnoi.model.domain.resources.Topic;
import org.epnoi.model.domain.resources.Word;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 25/02/16.
 */
@Component
public class WordRestRoute extends RestRoute {

    public WordRestRoute() {
        super("words", "word");
    }

    @Override
    public RestDefinition configure(RestsDefinition definitions) {

        RestDefinition definition = addResourceCRUD(definitions, WordI.class, Word.class);
        definition = addRelationCRUD(definition,"domains", VectorI.class,EmbeddedI.class,"where appears");
        definition = addRelationCRUD(definition,"words",WeightDomainI.class,SimilarI.class,"paired with");
        return definition;


    }


}
