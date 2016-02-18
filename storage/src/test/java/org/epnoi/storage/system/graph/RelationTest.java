package org.epnoi.storage.system.graph;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.storage.system.graph.domain.edges.BundlesEdge;
import org.epnoi.storage.system.graph.domain.nodes.DocumentNode;
import org.epnoi.storage.system.graph.domain.nodes.ItemNode;
import org.epnoi.storage.system.graph.domain.nodes.SourceNode;
import org.epnoi.storage.system.graph.repository.nodes.*;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by cbadenes on 22/12/15.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GraphConfig.class)
@TestPropertySource(properties = { "epnoi.neo4j.contactpoints = drinventor.dia.fi.upm.es", "epnoi.neo4j.port = 5030" })
public class RelationTest {

    @Autowired
    DocumentGraphRepository documentGraphRepository;

    @Autowired
    ItemGraphRepository itemGraphRepository;

    @Autowired
    TopicGraphRepository topicGraphRepository;

    @Autowired
    SourceGraphRepository sourceGraphRepository;

    @Autowired
    DomainGraphRepository domainGraphRepository;

    @Autowired
    PartGraphRepository partGraphRepository;

    @Autowired
    Session session;

    @Test
    public void insertBundle(){

        // Document
        String dUri = "documents/1212-1212-1212-1212";
        DocumentNode dNode = documentGraphRepository.findOneByUri(dUri);
        if (dNode == null){
            dNode = new DocumentNode();
            dNode.setUri(dUri);
        }

        // Item
        String iUri = "items/1212-1212-1212-1212";
        ItemNode iNode = itemGraphRepository.findOneByUri(iUri);
        if (iNode == null){
            iNode = new ItemNode();
            iNode.setUri(iUri);
        }

        // Relation
        BundlesEdge bundle = new BundlesEdge();
        bundle.setStart(dNode);
        bundle.setEnd(iNode);
        dNode.add(bundle);

        // save
        documentGraphRepository.save(dNode);

    }

    @Test
    public void readItemFromDocument(){

        DocumentNode result = documentGraphRepository.findOneByUri("documents/1212-1212-1212-1212");
        System.out.println(result);

        System.out.println(result.getItems());
    }


    @Test
    public void clean(){
        itemGraphRepository.deleteAll();
        documentGraphRepository.deleteAll();
        topicGraphRepository.deleteAll();
        sourceGraphRepository.deleteAll();
        domainGraphRepository.deleteAll();
        partGraphRepository.deleteAll();
    }

    @Test
    public void createSource(){
        SourceNode sourceNode = new SourceNode();
        sourceNode.setUri("http://epnoi.org/sources/7aa484ca-d968-43b2-b336-2a5af501d1e1");
        sourceGraphRepository.save(sourceNode);
    }


}
