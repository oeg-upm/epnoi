package org.epnoi.api.rest;

import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestsDefinition;
import org.epnoi.api.model.SourceI;
import org.epnoi.model.domain.resources.Source;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 25/02/16.
 */
@Component
public class SourceRestRoute extends RestRoute {

    private static final String SERVICE = "sourceService";

    @Override
    public RestDefinition configure(RestsDefinition definitions) {

        return definitions.rest("/sources").description("rest service for Source management")

                .post().description("Add a new source").type(SourceI.class).outType(Source.class)
                .produces("application/json").to("bean:"+SERVICE+"?method=create")

                .get("/").description("List all existing sources").outTypeList(String.class)
                .produces("application/json").to("bean:"+SERVICE+"?method=list")

                .get("/{id}").description("Find a source by id").outType(Source.class)
                .produces("application/json").to("bean:"+SERVICE+"?method=get(${header.id})")

                .delete("/").description("Remove all existing sources")
                .produces("application/json").to("bean:"+SERVICE+"?method=removeAll()")

                .delete("/{id}").description("Remove an existing Source")
                .produces("application/json").to("bean:"+SERVICE+"?method=remove(${header.id})")

                .put("/{id}").description("Update an existing Source").type(SourceI.class).outType(Source.class)
                .produces("application/json").to("bean:"+SERVICE+"?method=update")

                // PROVIDES
                .get("/{id}/provides").description("List all documents provided by a Source").outTypeList(String.class)
                .produces("application/json").to("bean:"+SERVICE+"?method=listDocuments(${header.id})")

                .delete("/{id}/provides").description("Remove all existing documents from a source")
                .produces("application/json").to("bean:"+SERVICE+"?method=removeDocuments(${header.id})")

                .post("/{sid}/provides/{did}").description("Add a new document to a source")
                .produces("application/json").to("bean:"+SERVICE+"?method=addDocument(${header.did},${header.did})")

                // COMPOSES
                .get("/{id}/composes").description("List all domains composed from a source").outTypeList(String.class)
                .produces("application/json").to("bean:"+SERVICE+"?method=listDomains(${header.id})")

                .delete("/{id}/composes").description("Remove all existing domains composed from a source")
                .produces("application/json").to("bean:"+SERVICE+"?method=removeDomains(${header.id})")

                .post("/{sid}/composes/{did}").description("Add a new domain to a source")
                .produces("application/json").to("bean:"+SERVICE+"?method=addDomain(${header.did},${header.did})")

                ;

    }
}
