package org.epnoi.storage.documents;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.cbadenes.lab.test.IntegrationTest;
import org.apache.commons.lang.StringUtils;
import org.epnoi.model.Event;
import org.epnoi.model.domain.relations.HypernymOf;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.*;
import org.epnoi.model.modules.*;
import org.epnoi.storage.Config;
import org.epnoi.storage.Helper;
import org.epnoi.storage.UDM;
import org.epnoi.storage.generator.URIGenerator;
import org.epnoi.storage.system.document.domain.WordDocument;
import org.epnoi.storage.system.document.repository.WordDocumentRepository;
import org.epnoi.storage.system.graph.repository.edges.DealsWithFromDocumentEdgeRepository;
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

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
        "epnoi.neo4j.port = 5030",
        "epnoi.eventbus.host = drinventor.dia.fi.upm.es"})
public class ListDocumentsTest {

    private static final Logger LOG = LoggerFactory.getLogger(ListDocumentsTest.class);

    @Autowired
    UDM udm;

    @Autowired
    Helper helper;

    @Autowired
    Session session;

    @Autowired
    DocumentGraphRepository documentGraphRepository;

    @Autowired
    WordDocumentRepository wordDocumentRepository;

    @Autowired
    SourceGraphRepository sourceGraphRepository;

    @Autowired
    DomainGraphRepository domainGraphRepository;

    @Autowired
    DealsWithFromDocumentEdgeRepository dealsWithFromDocumentEdgeRepository;

    @Autowired
    EventBus eventBus;

    @Autowired
    URIGenerator uriGenerator;

    @Autowired
    UnifiedNodeGraphRepositoryFactory factory;


    @Test
    public void listOfDocuments() throws IOException {

        List<String> refFiles = Arrays.asList(new String[]{"a109-rivers.pdf",
                "a120-gutierrez",
                "a13-irving",
                "a142-alexa",
                "a148-daniels",
                "a27-lee",
                "a37-talvala",
                "a38-zheng",
                "a53-barbic",
                "a54-sun",
                "a55-palacios",
                "a57-chuang",
                "a57-mohammed",
                "a61-hasan",
                "a62-mahajan",
                "a63-wang",
                "a65-rosenberg",
                "a71-tagliasacchi",
                "a74-tang",
                "a76-wojtan",
                "a85-lau",
                "a87-ballan",
                "a87-harmon",
                "a88-neubert",
                "a95-fattal",
                "a98-dong",
                "a99-kopf",
                "p1169-wang",
                "p294-agarwala",
                "p295-smith",
                "p380-alexa",
                "p393-james",
                "p501-govindaraju",
                "p519-weiss",
                "p594-bridson",
                "p600-zitnick",
                "p617-alliez",
                "p631-jia",
                "p635-peng",
                "p681-liu",
                "p697-igarashi",
                "p698-tsang",
                "p716-treuille",
                "p777-lefebvre",
                "p805-irving",
                "p814-matusik",
                "p828-agrawal",
                "p862-goldman",
                "p869-borgeat",
                "p932-macintyre"});


        List<String> uris = udm.find(Resource.Type.ITEM).all();

        LOG.info("Item URIs: " + uris);

        List<Item> items = uris.stream().map(uri -> udm.read(Resource.Type.ITEM).byUri(uri).get().asItem()).collect(Collectors.toList());

//		items.forEach(item-> LOG.info("Item: " + item.getUri() + " -> " + item.getUrl()));

		LOG.info("Items: " + items.size());

        File baseFolder = new File("/Users/cbadenes/Documents/OEG/Projects/DrInventor/datasets/2nd-review");
        List<String> files = FolderUtils.listFiles(baseFolder);

        HashMap<String,String> fileTable = new HashMap<>();

        files.stream().map(path -> new File(path)).filter(file -> refFiles.contains(file.getName())).forEach(file->fileTable.put(file.getName(), StringUtils.substringAfter(file.getAbsolutePath(),baseFolder.getAbsolutePath())));


        String basedir = "/opt/drinventor/tomcat-background/../workspace/ftp/siggraph";

//        List<Document> documents = refFiles.stream().
//                flatMap(refName -> items.stream().filter(item -> item.getUrl().contains(refName))).
//                map(item -> udm.find(Resource.Type.DOCUMENT).by(Document.TITLE,item.getTitle())).
//                map(docUris -> udm.read(Resource.Type.DOCUMENT).byUri(docUris.get(0)).get().asDocument()).
//                collect(Collectors.toList());
//        ;


        List<Reference> references = new ArrayList<>();
        for (String fileName : refFiles){
            LOG.info("File: " + fileName);
            try{
                Item item           = items.stream().filter(el -> el.getUrl().contains(fileName)).collect(Collectors.toList()).get(0);
                String docUri       = udm.find(Resource.Type.DOCUMENT).by(Document.TITLE,item.getTitle()).get(0);
                Document document   = udm.read(Resource.Type.DOCUMENT).byUri(docUri).get().asDocument();

                Reference reference = new Reference();
                reference.setFileName(fileName);
                reference.setTitle(document.getTitle());
                reference.setUri(document.getUri());
                references.add(reference);
            }catch (Exception e){
                LOG.error("Error on file: " + fileName,e);
            }

        }


        ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(new File("documents.json"), references);


    }

}
