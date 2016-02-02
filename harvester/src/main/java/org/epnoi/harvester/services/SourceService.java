package org.epnoi.harvester.services;

import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.SpringCamelContext;
import org.epnoi.harvester.routes.RouteDefinitionFactory;
import org.epnoi.storage.TimeGenerator;
import org.epnoi.storage.UDM;
import org.epnoi.storage.URIGenerator;
import org.epnoi.storage.model.Domain;
import org.epnoi.storage.model.ResourceUtils;
import org.epnoi.storage.model.Source;
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
        udm.findSources().stream().map(uri -> udm.readSource(uri)).filter(res -> res.isPresent()).map(res -> res.get()).forEach(source -> {
            try {
                List<String> res = udm.findDomainBySource(source.getUri());
                if (res != null && !res.isEmpty()){
                    Optional<Domain> domain = udm.readDomain(res.get(0));
                    if (domain.isPresent()) addRoute(source,domain.get());
                }
            } catch (Exception e) {
                LOG.error("Error initializing source from ddbb:" + source, e);
            }
        });
        LOG.info("All sources were restored from ddbb.");
    }

    public Source create(Source source) throws Exception {

        List<String> domains = udm.findDomainBySource(source.getUri());

        Domain domain;
        if (domains == null || domains.isEmpty()){
            LOG.info("creating a new domain associated to source: " + source);
            domain = new Domain();
            domain.setUri(uriGenerator.newDomain());
            domain.setName(source.getName());
            domain.setDescription("attached to source: " + source.getUri());
            udm.saveDomain(domain);
            LOG.info("Domain: " + domain + " attached to source: " + source);

            udm.relateDomainToSource(domain.getUri(),source.getUri(),timeGenerator.getNowAsISO());
        }else{
            domain = ResourceUtils.map(udm.readDomain(domains.get(0)).get(),Domain.class);
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