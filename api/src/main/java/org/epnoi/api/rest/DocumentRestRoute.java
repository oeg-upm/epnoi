package org.epnoi.api.rest;

import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestsDefinition;
import org.epnoi.api.model.DocumentI;
import org.epnoi.api.model.SourceI;
import org.epnoi.model.domain.resources.Document;
import org.epnoi.model.domain.resources.Source;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 25/02/16.
 */
@Component
public class DocumentRestRoute extends RestRoute {

    private static final Class FACADE       = DocumentI.class;
    private static final Class RESOURCE     = Document.class;

    private static final String SINGULAR    = "document";
    private static final String PLURAL      = "documents";

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

                // BUNDLES
                .get("/{id}/items").description("List all items provided by a "+SINGULAR).outTypeList(String.class)
                .produces("application/json").to("bean:"+SERVICE+"?method=listItems(${header.id})")

                .delete("/{id}/items").description("Remove all existing items from a "+SINGULAR)
                .produces("application/json").to("bean:"+SERVICE+"?method=removeItems(${header.id})")

                .post("/{sid}/items/{did}").description("Add a new item to a "+SINGULAR)
                .produces("application/json").to("bean:"+SERVICE+"?method=addItem(${header.did},${header.did})")

                // DEALS_WITH
                .get("/{id}/topics").description("List all topics dealt by a document").outTypeList(String.class)
                .produces("application/json").to("bean:"+SERVICE+"?method=listTopics(${header.id})")

                .delete("/{id}/topics").description("Remove all existing topics dealt by a document")
                .produces("application/json").to("bean:"+SERVICE+"?method=removeTopics(${header.id})")

                .post("/{sid}/topics/{did}").description("Add a new topic to a document")
                .produces("application/json").to("bean:"+SERVICE+"?method=addTopic(${header.did},${header.did})")

                // SIMILAR_TO
                .get("/{id}/documents").description("List all documents similar to a document").outTypeList(String.class)
                .produces("application/json").to("bean:"+SERVICE+"?method=listSimilars(${header.id})")

                .delete("/{id}/documents").description("Remove all existing documents similar to a document")
                .produces("application/json").to("bean:"+SERVICE+"?method=removeSimilars(${header.id})")

                .post("/{sid}/documents/{did}").description("Add a new document similar to a document")
                .produces("application/json").to("bean:"+SERVICE+"?method=addSimilar(${header.did},${header.did})")

                ;

    }
}
