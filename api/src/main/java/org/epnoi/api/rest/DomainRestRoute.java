package org.epnoi.api.rest;

import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestsDefinition;
import org.epnoi.api.model.DocumentI;
import org.epnoi.api.model.DomainI;
import org.epnoi.model.domain.resources.Document;
import org.epnoi.model.domain.resources.Domain;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 25/02/16.
 */
@Component
public class DomainRestRoute extends RestRoute {

    private static final Class FACADE       = DomainI.class;
    private static final Class RESOURCE     = Domain.class;

    private static final String SINGULAR    = "domain";
    private static final String PLURAL      = "domains";

    private static final String SERVICE = SINGULAR+"Service";

    @Override
    public RestDefinition configure(RestsDefinition definitions) {

        return definitions.rest("/"+PLURAL).description("rest service for "+SINGULAR+" management")

                .post().description("Add a new "+SINGULAR+"").type(FACADE).outType(RESOURCE)
                .produces("application/json").to("bean:"+SERVICE+"?method=create")

                .get("/").description("List all existing "+PLURAL+"").outTypeList(String.class)
                .produces("application/json").to("bean:"+SERVICE+"?method=list")

                .get("/{id}").description("Find a "+SINGULAR+" by id").outType(RESOURCE)
                .produces("application/json").to("bean:"+SERVICE+"?method=get(${header.id})")

                .delete("/").description("Remove all existing "+PLURAL+"")
                .produces("application/json").to("bean:"+SERVICE+"?method=removeAll()")

                .delete("/{id}").description("Remove an existing "+SINGULAR+"")
                .produces("application/json").to("bean:"+SERVICE+"?method=remove(${header.id})")

                .put("/{id}").description("Update an existing "+SINGULAR+"").type(FACADE).outType(RESOURCE)
                .produces("application/json").to("bean:"+SERVICE+"?method=update")

                // CONTAINS
                .get("/{id}/documents").description("List all items provided by a "+SINGULAR).outTypeList(String.class)
                .produces("application/json").to("bean:"+SERVICE+"?method=listDocuments(${header.id})")

                .delete("/{id}/documents").description("Remove all existing items from a "+SINGULAR)
                .produces("application/json").to("bean:"+SERVICE+"?method=removeDocuments(${header.id})")

                .post("/{sid}/documents/{did}").description("Add a new item to a "+SINGULAR)
                .produces("application/json").to("bean:"+SERVICE+"?method=addDocument(${header.did},${header.did})")

                ;

    }
}
