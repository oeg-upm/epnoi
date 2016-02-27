package org.epnoi.api.rest;

import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestsDefinition;
import org.epnoi.api.model.relations.*;
import org.epnoi.api.model.resources.DocumentI;
import org.epnoi.model.domain.resources.Document;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 25/02/16.
 */
@Component
public class DocumentRestRoute extends RestRoute {

    public DocumentRestRoute() {
        super("documents", "document");
    }

    @Override
    public RestDefinition configure(RestsDefinition definitions) {

        RestDefinition definition = addResourceCRUD(definitions, DocumentI.class, Document.class);
        definition = addRelationCRUD(definition,"items",null,RelationI.class,"bundled by");
        definition = addRelationCRUD(definition,"topics",WeightI.class,DealsI.class,"dealt by");
        definition = addRelationCRUD(definition,"documents",WeightDomainI.class,SimilarI.class,"similar to");
        return definition;


    }


}
