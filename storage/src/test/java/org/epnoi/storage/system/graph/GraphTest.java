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
import org.epnoi.storage.system.graph.domain.nodes.DocumentNode;
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
        "epnoi.cassandra.contactpoints = drinventor.dia.fi.upm.es",
        "epnoi.cassandra.port = 5011",
        "epnoi.cassandra.keyspace = research",
        "epnoi.elasticsearch.contactpoints = drinventor.dia.fi.upm.es",
        "epnoi.elasticsearch.port = 5021",
        "epnoi.neo4j.contactpoints = drinventor.dia.fi.upm.es",
        "epnoi.neo4j.port = 5030",
        "epnoi.eventbus.host = drinventor.dia.fi.upm.es"})
public class GraphTest {

    private static final Logger LOG = LoggerFactory.getLogger(GraphTest.class);


    @Autowired
    SimilarToDocumentsEdgeRepository similarToDocumentsEdgeRepository;

    @Autowired
    DocumentGraphRepository documentGraphRepository;

    @Autowired
    SimilarToItemsEdgeRepository similarToItemsEdgeRepository;

    @Autowired
    SimilarToPartsEdgeRepository similarToPartsEdgeRepository;

    @Autowired
    SimilarToEdgeRepository similarToEdgeRepository;

    @Autowired
    BundlesEdgeRepository bundlesEdgeRepository;




    @Test
    public void findSimilar(){

        String startDocUri = "http://drinventor.eu/documents/c369c917fecf3b4828688bdb6677dd6e";
        String endDocUri   = "http://drinventor.eu/documents/f6f36164961229eac1bf19431a3744a0";

        String startItemUri = "http://drinventor.eu/items/c369c917fecf3b4828688bdb6677dd6e";
        String endItemUri   = "http://drinventor.eu/items/715f6df41fdf75cb3d0db7fce050f301";

        try{
//            Iterable<SimilarToDocumentsEdge> result = similarToDocumentsEdgeRepository.giveme(startUri, endUri);
//            System.out.println("1->" + result);
            System.out.println("1->" + similarToItemsEdgeRepository.findByNodes(startItemUri,endItemUri));
            System.out.println("2->" + similarToDocumentsEdgeRepository.findByNodes(startDocUri, endDocUri));
            System.out.println("3->" + similarToEdgeRepository.findDocumentsByNodes(startDocUri,endDocUri));
//            System.out.println("3->" + similarToEdgeRepository.findItemsByNodes(startItemUri,endItemUri));

//            Iterable<DocumentNode> result2 = documentGraphRepository.findByDocument(startUri);
//            System.out.println(result2);

        }catch (Exception e){
            e.printStackTrace();
        }
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
