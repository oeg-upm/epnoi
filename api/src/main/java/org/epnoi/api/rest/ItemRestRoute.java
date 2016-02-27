package org.epnoi.api.rest;

import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestsDefinition;
import org.epnoi.api.model.relations.*;
import org.epnoi.api.model.resources.ItemI;
import org.epnoi.model.domain.resources.Item;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 25/02/16.
 */
@Component
public class ItemRestRoute extends RestRoute {

    public ItemRestRoute() {
        super("items", "item");
    }

    @Override
    public RestDefinition configure(RestsDefinition definitions) {

        RestDefinition definition = addResourceCRUD(definitions, ItemI.class, Item.class);
        definition = addRelationCRUD(definition,"topics",WeightI.class,DealsI.class,"dealt by");
        definition = addRelationCRUD(definition,"items",WeightDomainI.class,SimilarI.class,"similar to");
        return definition;


    }


}
