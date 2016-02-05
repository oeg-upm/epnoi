package org.epnoi.harvester.services;

import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.SpringCamelContext;
import org.epnoi.harvester.routes.RouteDefinitionFactory;
import org.epnoi.model.domain.*;
import org.epnoi.storage.generator.TimeGenerator;
import org.epnoi.storage.UDM;
import org.epnoi.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

/**
 * Created by cbadenes on 01/12/15.
 */
@Component
public class SourceService {

    private static final Logger LOG = LoggerFactory.getLogger(SourceService.class);

    @Autowired
    SpringCamelContext camelContext;

    @Autowired
    RouteDefinitionFactory routeDefinitionFactory;

    @Autowired
    UDM udm;

    @Autowired
    URIGenerator uriGenerator;

    @Autowired
    TimeGenerator timeGenerator;

    public SourceService(){

    }

    // TODO
    @PostConstruct
    public void setup(){
        LOG.info("Restoring sources from ddbb..");
        udm.find(Resource.Type.SOURCE).all().stream().map(uri -> udm.read(Resource.Type.SOURCE).byUri(uri)).filter(res -> res.isPresent()).map(res -> res.get()).forEach(source -> {
            try {
                List<String> res = udm.find(Resource.Type.DOMAIN).in(Resource.Type.SOURCE,source.getUri());
                if (res != null && !res.isEmpty()){
                    Optional<Resource> domain = udm.read(Resource.Type.DOMAIN).byUri(res.get(0));
                    if (domain.isPresent()) addRoute((Source) source, (Domain) domain.get());
                }
            } catch (Exception e) {
                LOG.error("Error initializing source from ddbb:" + source, e);
            }
        });
        LOG.info("All sources were restored from ddbb.");
    }

    public Source create(Source source) throws Exception {

        List<String> domains = udm.find(Resource.Type.DOMAIN).in(Resource.Type.SOURCE,source.getUri());

        Domain domain;
        if (domains == null || domains.isEmpty()){
            LOG.info("creating a new domain associated to source: " + source);
            domain = new Domain();
            domain.setUri(uriGenerator.newFor(Resource.Type.DOMAIN));
            domain.setName(source.getName());
            domain.setDescription("attached to source: " + source.getUri());
            udm.save(Resource.Type.DOMAIN).with(domain);
            LOG.info("Domain: " + domain + " attached to source: " + source);
            udm.attachFrom(source.getUri()).to(domain.getUri()).by(Relation.Type.SOURCE_COMPOSES_DOMAIN, RelationProperties.builder().date(timeGenerator.asISO()).build());

        }else{
            domain = ResourceUtils.map(udm.read(Resource.Type.DOMAIN).byUri(domains.get(0)).get(),Domain.class);
        }

        addRoute(source,domain);
        return source;
    }

    private void addRoute(Source source, Domain domain) throws Exception {
        // Create a new route for harvesting this source
        RouteDefinition route = routeDefinitionFactory.newRoute(source,domain);
        LOG.info("adding route to harvest: " + route);
        camelContext.addRouteDefinition(route);
    }

    public Source update(String uri,Source source){
        throw new RuntimeException("Method does not implemented yet");
    }


    public Source remove(String uri){
        throw new RuntimeException("Method does not implemented yet");
    }

    public List<Source> list(){
        throw new RuntimeException("Method does not implemented yet");
    }


    public Source get(String id){
        throw new RuntimeException("Method does not implemented yet");
    }


}