package org.epnoi.api.routes.rest;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.epnoi.storage.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class RestRouteBuilder extends RouteBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(RestRouteBuilder.class);

    @Value("${epnoi.api.rest.port}")
    protected Integer port;

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json_xml)
                .dataFormatProperty("prettyPrint", "true")
                .dataFormatProperty("json.in.disableFeatures", "FAIL_ON_UNKNOWN_PROPERTIES,ADJUST_DATES_TO_CONTEXT_TIME_ZONE")
                .dataFormatProperty("xml.out.mustBeJAXBElement", "false")
                .contextPath("api/rest")
                .port(port);

        crudOf("sources","source",Source.class);
        crudOf("domains","domain",Domain.class);
        rudOf("documents","document",Document.class);
        rudOf("items","item",Item.class);
        rudOf("parts","part",Part.class);
        crudOf("words","word",Word.class);
        crudOf("topics","topic",Topic.class);
        crudOf("analyses","analysis",Analysis.class);
        crudOf("relations","relation",Relation.class);
//        crdOf("searches","search", Search.class);
//        crdOf("explorations","exploration", Exploration.class);
    }

    private void crudOf(String domain, String label, Class resource){
        rest("/"+domain).description("rest service for "+label+" management")
                .consumes("application/json").produces("application/json")

                .post().description("Add a new "+label).type(resource).outType(resource)
                .to("bean:"+label+"Service?method=create")

                .get("/").description("List all existing "+domain).outTypeList(String.class)
                .to("bean:"+label+"Service?method=list")

                .get("/{uri}").description("Find a "+label+" by uri").outType(resource)
//                .param().name("id").type(path).description("The id of the user to get").dataType("int").endParam()
                .to("bean:"+label+"Service?method=get(${header.uri})")

                .delete("/").description("Remove all existing "+domain).outType(resource)
                .to("bean:"+label+"Service?method=removeAll()")

                .delete("/{uri}").description("Remove an existing "+label).outType(resource)
                .to("bean:"+label+"Service?method=remove(${header.uri})")

                .put("/{uri}").description("Update an existing "+label).type(resource).outType(resource)
                .to("bean:"+label+"Service?method=update");
    }

    //TODO remove this method. Chain-of-resposibility pattern
    private void rudOf(String domain, String label, Class resource){
        rest("/"+domain).description("rest service for "+label+" management")
                .consumes("application/json").produces("application/json")

                .get("/").description("List all existing "+domain).outTypeList(String.class)
                .to("bean:"+label+"Service?method=list")

                .get("/{uri}").description("Find a "+label+" by uri").outType(resource)
//                .param().name("id").type(path).description("The id of the user to get").dataType("int").endParam()
                .to("bean:"+label+"Service?method=get(${header.uri})")

                .delete("/").description("Remove all existing "+domain).outType(resource)
                .to("bean:"+label+"Service?method=removeAll()")

                .delete("/{uri}").description("Remove an existing "+label).outType(resource)
                .to("bean:"+label+"Service?method=remove(${header.uri})")

                .put("/{uri}").description("Update an existing "+label).type(resource).outType(resource)
                .to("bean:"+label+"Service?method=update");
    }

    //TODO remove this method. Chain-of-resposibility pattern
    private void crdOf(String domain, String label, Class resource){
        rest("/"+domain).description("rest service for "+label+" management")
                .consumes("application/json").produces("application/json")

                .post().description("Add a new "+label).type(resource).outType(resource)
                .to("bean:"+label+"Service?method=create")

                .get("/").description("List all existing "+domain).outTypeList(String.class)
                .to("bean:"+label+"Service?method=list")

                .get("/{uri}").description("Find a "+label+" by uri").outType(resource)
//                .param().name("id").type(path).description("The id of the user to get").dataType("int").endParam()
                .to("bean:"+label+"Service?method=get(${header.uri})")

                .delete("/").description("Remove all existing "+domain).outType(resource)
                .to("bean:"+label+"Service?method=removeAll()")

                .delete("/{uri}").description("Remove an existing "+label).outType(resource)
                .to("bean:"+label+"Service?method=remove(${header.uri})");
    }

}