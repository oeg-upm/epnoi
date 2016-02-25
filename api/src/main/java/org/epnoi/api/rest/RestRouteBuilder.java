package org.epnoi.api.rest;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.epnoi.api.model.SourceI;
import org.epnoi.model.domain.relations.HypernymOf;
import org.epnoi.model.domain.resources.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class RestRouteBuilder extends RouteBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(RestRouteBuilder.class);

    @Value("${epnoi.api.rest.port}")
    protected Integer port;

    @Autowired
    List<RestRoute> routes;

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("servlet")
                .scheme("http")
                .bindingMode(RestBindingMode.json_xml)
                .skipBindingOnErrorCode(true)
                .jsonDataFormat("json-jackson")
                .xmlDataFormat("jaxb")
                .enableCORS(false)
                .componentProperty("minThreads","1")
                .componentProperty("maxThreads","10")
                .componentProperty("maxConnectionsPerHost","-1")
                .componentProperty("maxTotalConnections","-1")
                .dataFormatProperty("prettyPrint", "true")
                .dataFormatProperty("json.in.disableFeatures", "FAIL_ON_UNKNOWN_PROPERTIES,ADJUST_DATES_TO_CONTEXT_TIME_ZONE")
//                .dataFormatProperty("json.out.disableFeatures", "WRITE_NULL_MAP_VALUES")
                .dataFormatProperty("xml.out.mustBeJAXBElement", "false")
                .contextPath("api/0.2")
                .port(port);


        // Configure REST routes
        this.getRestCollection().setCamelContext(this.getContext());
        routes.forEach(route -> configureRest(route.configure(getRestCollection())));

        // Resources
//        createReadUpdateDeleteOf("sources","source",SourceI.class,Source.class);
//        readDeleteOf("domains","domain",Domain.class);
//        readDeleteOf("documents","document",Document.class);
        readDeleteOf("items","item",Item.class);
        readDeleteOf("parts","part",Part.class);
        readDeleteOf("words","word",Word.class);
        readDeleteOf("topics","topic",Topic.class);
        readDeleteOf("terms","term",Term.class);

        // Relations
//        readDeleteOf("hypernyms","hypernym",HypernymOf.class);




//        createReadUpdateDeleteOf("contains","contains",ContainsI.class,Contains.class);
//        createReadUpdateDeleteOf("compositions","composes",ContainsI.class,Contains.class);
    }


    private void createReadUpdateDeleteOf(String domain, String label, Class facade, Class resource){

          rest("/"+domain).description("rest service for "+label+" management")
//                  .setBindingMode(RestBindingMode.auto)
//                  .consumes("application/json").produces("application/json")

                .post().description("Add a new "+label).type(facade).outType(resource)
                .produces("application/json").to("bean:"+label+"Service?method=create")

                .get("/").description("List all existing "+domain).outTypeList(String.class)
                .produces("application/json").to("bean:"+label+"Service?method=list")

                .get("/{id}").description("Find a "+label+" by id").outType(resource)
//                .param().name("id").type(path).description("The id of the user to get").dataType("int").endParam()
                .produces("application/json").to("bean:"+label+"Service?method=get(${header.id})")

                .delete("/").description("Remove all existing "+domain)
                .produces("application/json").to("bean:"+label+"Service?method=removeAll()")

                .delete("/{id}").description("Remove an existing "+label)
                .produces("application/json").to("bean:"+label+"Service?method=remove(${header.id})")

                .put("/{id}").description("Update an existing "+label).type(facade).outType(resource)
                .produces("application/json").to("bean:"+label+"Service?method=update");
    }

    //TODO remove this method. Chain-of-resposibility pattern
    private void readDeleteOf(String domain, String label, Class resource){

        rest("/"+domain).description("rest service for "+label+" management")
                //.consumes("application/json").produces("application/json")

                .get("/").description("List all existing "+domain).outTypeList(String.class)
                .produces("application/json").to("bean:"+label+"Service?method=list")

                .get("/{id}").description("Find a "+label+" by id").outType(resource)
//                .param().name("id").type(path).description("The id of the user to get").dataType("int").endParam()
                .produces("application/json").to("bean:"+label+"Service?method=get(${header.id})")

                .delete("/").description("Remove all existing "+domain).outType(resource)
                .produces("application/json").to("bean:"+label+"Service?method=removeAll()")

                .delete("/{id}").description("Remove an existing "+label).outType(resource)
                .produces("application/json").to("bean:"+label+"Service?method=remove(${header.id})");

//                .put("/{uri}").description("Update an existing "+label).type(resource).outType(resource)
//                .to("bean:"+label+"Service?method=update");

    }

}