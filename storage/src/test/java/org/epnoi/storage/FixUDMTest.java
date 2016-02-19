package org.epnoi.storage;

import es.cbadenes.lab.test.IntegrationTest;
import org.apache.commons.collections.CollectionUtils;
import org.epnoi.model.domain.relations.Contains;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.*;
import org.epnoi.storage.system.column.repository.UnifiedColumnRepository;
import org.epnoi.storage.system.graph.repository.edges.UnifiedEdgeGraphRepository;
import org.epnoi.storage.system.graph.repository.nodes.UnifiedNodeGraphRepository;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * Created by cbadenes on 01/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {
        "epnoi.cassandra.contactpoints = zavijava.dia.fi.upm.es",
        "epnoi.cassandra.port = 5011",
        "epnoi.cassandra.keyspace = research",
        "epnoi.elasticsearch.contactpoints = zavijava.dia.fi.upm.es",
        "epnoi.elasticsearch.port = 5021",
        "epnoi.neo4j.contactpoints = zavijava.dia.fi.upm.es",
        "epnoi.neo4j.port = 5032",
        "epnoi.eventbus.uri = amqp://epnoi:drinventor@drinventor.dia.fi.upm.es:5041/drinventor"})
public class FixUDMTest {

    private static final Logger LOG = LoggerFactory.getLogger(FixUDMTest.class);

    @Autowired
    UDM udm;

    @Autowired
    UnifiedColumnRepository columnRepository;

    @Autowired
    UnifiedNodeGraphRepository nodeRepository;

    @Autowired
    UnifiedEdgeGraphRepository edgeRepository;


    @Test
    public void fixDomain(){

        String sourceUri = "http://epnoi.org/sources/75b7c67d-f9e1-4b7d-8e96-543bc621d3a3";
        String domainUri = "http://epnoi.org/domains/ce123683-512a-4f2a-a539-c77b666a8b79";
//
//        udm.find(Relation.Type.CONTAINS).in(Resource.Type.DOMAIN,domainUri).forEach(rel -> udm.delete(Relation.Type.CONTAINS).byUri(rel.getUri()));
//
//        udm.find(Resource.Type.DOCUMENT).all().forEach(res -> udm.save(Relation.newContains(domainUri,res)));
//

        List<String> alld = udm.find(Resource.Type.DOCUMENT).all();
        List<String> ind = udm.find(Resource.Type.DOCUMENT).in(Resource.Type.DOMAIN, domainUri);


        alld.stream().filter(x -> !ind.contains(x)).forEach(res -> udm.save(Relation.newContains(domainUri,res)));



    }

    @Test
    public void newTopicModel(){

        String domain = "http://epnoi.org/domains/ce123683-512a-4f2a-a539-c77b666a8b79";

        Analysis analysis = Resource.newAnalysis();
        analysis.setDomain(domain);
        analysis.setConfiguration("sample");
        analysis.setDescription("Topic Model");
        analysis.setType("topic-model");
        udm.save(analysis);
    }

}
