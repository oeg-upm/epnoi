package org.epnoi.storage.system.graph;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.Event;
import org.epnoi.model.domain.relations.HypernymOf;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Document;
import org.epnoi.model.domain.resources.Domain;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.domain.resources.Source;
import org.epnoi.model.modules.BindingKey;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.EventBusSubscriber;
import org.epnoi.model.modules.RoutingKey;
import org.epnoi.storage.Config;
import org.epnoi.storage.Helper;
import org.epnoi.storage.UDM;
import org.epnoi.storage.generator.URIGenerator;
import org.epnoi.storage.system.document.domain.WordDocument;
import org.epnoi.storage.system.document.repository.WordDocumentRepository;
import org.epnoi.storage.system.graph.domain.edges.BundlesEdge;
import org.epnoi.storage.system.graph.domain.edges.SimilarToDocumentsEdge;
import org.epnoi.storage.system.graph.domain.edges.SimilarToItemsEdge;
import org.epnoi.storage.system.graph.repository.edges.*;
import org.epnoi.storage.system.graph.repository.nodes.DocumentGraphRepository;
import org.epnoi.storage.system.graph.repository.nodes.DomainGraphRepository;
import org.epnoi.storage.system.graph.repository.nodes.SourceGraphRepository;
import org.epnoi.storage.system.graph.repository.nodes.UnifiedNodeGraphRepositoryFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cbadenes on 01/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GraphConfig.class)
@TestPropertySource(properties = {
        "epnoi.neo4j.contactpoints = drinventor.dia.fi.upm.es",
        "epnoi.neo4j.port = 5030",
        "epnoi.eventbus.host = drinventor.dia.fi.upm.es"})
public class GraphTest {

    private static final Logger LOG = LoggerFactory.getLogger(GraphTest.class);


    @Autowired
    SimilarToDocumentsEdgeRepository similarToDocumentsEdgeRepository;


    @Autowired
    SimilarToItemsEdgeRepository similarToItemsEdgeRepository;

    @Autowired
    SimilarToPartsEdgeRepository similarToPartsEdgeRepository;


    @Autowired
    BundlesEdgeRepository bundlesEdgeRepository;



    @Test
    public void deleteSimilars(){
//        udm.delete(Relation.Type.SIMILAR_TO_DOCUMENTS).in(Resource.Type.DOMAIN,"http://drinventor.eu/domains/7df34748-7fad-486e-a799-3bcd86a03499");
        try{
            Iterable<SimilarToDocumentsEdge> result = similarToDocumentsEdgeRepository.findByDomain("http://drinventor.eu/domains/7df34748-7fad-486e-a799-3bcd86a03499");
            System.out.println(result);
        }catch (Exception e){
            LOG.error("Error",e);
        }
    }

    @Test
    public void findItems(){

        String relUri = "http://drinventor.eu/bundles/6c81b934-e9ee-46d7-a3f2-03713f2fabc1";
        BundlesEdge result = bundlesEdgeRepository.findOneByUri(relUri);
        System.out.println(result);

        String uri = "http://drinventor.eu/documents/af351b184d0bc10597573d31544a23a4";
        Iterable<BundlesEdge> results = bundlesEdgeRepository.findByDocument(uri);
        System.out.println(results);
    }


    @Test
    public void findByUri1(){
        String uri = "http://drinventor.eu/similarities/f3087fda-02e4-4311-b491-0c91509b4e50";
        Object result = similarToDocumentsEdgeRepository.findOneByUri(uri);
        System.out.println(result);
    }

    @Test
    public void findByUri2(){
        String uri = "http://drinventor.eu/similarities/f3087fda-02e4-4311-b491-0c91509b4e50";
        Object result = similarToItemsEdgeRepository.findOneByUri(uri);
        System.out.println(result);
    }


    @Test
    public void findByUri3(){
        String uri = "http://drinventor.eu/similarities/f3087fda-02e4-4311-b491-0c91509b4e50";
        Object result = similarToPartsEdgeRepository.findOneByUri(uri);
        System.out.println(result);
    }

}
