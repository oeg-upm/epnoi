package org.epnoi.storage;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.Event;
import org.epnoi.model.domain.Resource;
import org.epnoi.model.domain.*;
import org.epnoi.model.modules.BindingKey;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.EventBusSubscriber;
import org.epnoi.model.modules.RoutingKey;
import org.epnoi.storage.system.document.domain.WordDocument;
import org.epnoi.storage.system.document.repository.WordDocumentRepository;
import org.epnoi.storage.generator.TimeGenerator;
import org.epnoi.storage.generator.URIGenerator;
import org.epnoi.storage.system.graph.domain.DomainNode;
import org.epnoi.storage.system.graph.domain.SourceNode;
import org.epnoi.storage.system.graph.domain.relationships.DomainComposedBySource;
import org.epnoi.storage.system.graph.domain.relationships.TopicDealtByDocument;
import org.epnoi.storage.system.graph.repository.DocumentGraphRepository;
import org.epnoi.storage.system.graph.repository.DomainGraphRepository;
import org.epnoi.storage.system.graph.repository.SourceGraphRepository;
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
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {
        "epnoi.cassandra.contactpoints = drinventor.dia.fi.upm.es",
        "epnoi.cassandra.port = 5011",
        "epnoi.cassandra.keyspace = research",
        "epnoi.elasticsearch.contactpoints = drinventor.dia.fi.upm.es",
        "epnoi.elasticsearch.port = 5021",
        "epnoi.neo4j.contactpoints = drinventor.dia.fi.upm.es",
        "epnoi.neo4j.port = 5030",
        "epnoi.eventbus.uri = amqp://epnoi:drinventor@drinventor.dia.fi.upm.es:5041/drinventor"})

public class UDMTest {

    private static final Logger LOG = LoggerFactory.getLogger(UDMTest.class);

    @Autowired
    UDM udm;

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
    EventBus eventBus;

    @Autowired
    URIGenerator uriGenerator;

    @Autowired
    TimeGenerator timeGenerator;

    @Test
    public void saveSource(){
        AtomicInteger counter = new AtomicInteger(0);

        eventBus.subscribe(new EventBusSubscriber() {
            @Override
            public void handle(Event event) {
                LOG.info("Handle Event: " + event);
                counter.incrementAndGet();
            }
        }, BindingKey.of(RoutingKey.of(Resource.Type.SOURCE, Resource.State.CREATED),"test"));


        Source source = new Source();
        source.setUri("http://epnoi.org/sources/0b3e80ae-d598-4dd4-8c54-38e2229f0bf8");
        source.setUrl("file://opt/epnoi/inbox/upm");
        source.setName("test-source");
        source.setProtocol("file");
        source.setCreationTime("20160101T22:02");
        source.setDescription("testing purposes");

        LOG.info("Saving source: " + source);
        udm.saveSource(source);
        LOG.info("source saved!");

        Optional<Source> source2 = udm.readSource(source.getUri());
        Assert.assertTrue(source2.isPresent());
        Assert.assertEquals(source.getUri(),source2.get().getUri());
        Assert.assertEquals(source.getName(),source2.get().getName());

        LOG.info("Deleting source: " + source);
        udm.deleteSource(source.getUri());
        LOG.info("source deleted!");

        Optional<Source> source3 = udm.readSource(source.getUri());
        Assert.assertTrue(source3.isPresent());
        Assert.assertNotEquals(source,source3.get());

        Assert.assertEquals(1, counter.get());

    }

    @Test
    public void getDocumentsByDomain(){
        // Source
        Source source = new Source();
        source.setUri(uriGenerator.newSource());
        udm.saveSource(source);

        // Domain
        Domain domain = new Domain();
        domain.setUri(uriGenerator.newDomain());
        udm.saveDomain(domain);

        // Document 1
        Document doc1 = new Document();
        doc1.setUri(uriGenerator.newDocument());
        udm.saveDocument(doc1,source.getUri());
        // -> in domain
        udm.relateDocumentToDomain(doc1.getUri(),domain.getUri(),timeGenerator.getNowAsISO());

        // Document 2
        Document doc2 = new Document();
        doc2.setUri(uriGenerator.newDocument());
        udm.saveDocument(doc2,source.getUri());
        // -> in domain
        udm.relateDocumentToDomain(doc2.getUri(),domain.getUri(),timeGenerator.getNowAsISO());

        // Getting Documents
        List<String> documents = udm.findDocumentsByDomain(domain.getUri());

        // Delete
        udm.deleteSource(source.getUri());
        udm.deleteDomain(domain.getUri());
        udm.deleteDocument(doc1.getUri());
        udm.deleteDocument(doc2.getUri());

        Assert.assertTrue(documents != null);
        Assert.assertEquals(2,documents.size());
    }

    @Test
    public void getItemsByDomain(){
        // Source
        Source source = new Source();
        source.setUri(uriGenerator.newSource());
        udm.saveSource(source);

        // Domain
        Domain domain = new Domain();
        domain.setUri(uriGenerator.newDomain());
        udm.saveDomain(domain);

        // Document 1
        Document doc1 = new Document();
        doc1.setUri(uriGenerator.newDocument());
        udm.saveDocument(doc1,source.getUri());
        // -> in domain
        udm.relateDocumentToDomain(doc1.getUri(),domain.getUri(),timeGenerator.getNowAsISO());
        // -> Item 1
        Item item11 = new Item();
        item11.setUri(uriGenerator.newItem());
        udm.saveItem(item11,doc1.getUri());
        // -> Item 2
        Item item12 = new Item();
        item12.setUri(uriGenerator.newItem());
        udm.saveItem(item12,doc1.getUri());

        // Document 2
        Document doc2 = new Document();
        doc2.setUri(uriGenerator.newDocument());
        udm.saveDocument(doc2,source.getUri());
        // -> in domain
        udm.relateDocumentToDomain(doc2.getUri(),domain.getUri(),timeGenerator.getNowAsISO());
        // -> Item 1
        Item item21 = new Item();
        item21.setUri(uriGenerator.newItem());
        udm.saveItem(item21,doc2.getUri());
        // -> Item 2
        Item item22 = new Item();
        item22.setUri(uriGenerator.newItem());
        udm.saveItem(item22,doc2.getUri());
        // -> Item 3
        Item item23 = new Item();
        item23.setUri(uriGenerator.newItem());
        udm.saveItem(item23,doc2.getUri());


        // Getting items in domain
        List<String> items = udm.findItemsByDomain(domain.getUri());

        // Delete
        udm.deleteSource(source.getUri());
        udm.deleteDomain(domain.getUri());
        udm.deleteDocument(doc1.getUri());
        udm.deleteDocument(doc2.getUri());
        udm.deleteItem(item11.getUri());
        udm.deleteItem(item12.getUri());
        udm.deleteItem(item21.getUri());
        udm.deleteItem(item22.getUri());
        udm.deleteItem(item23.getUri());

        Assert.assertTrue(items != null);
        Assert.assertEquals(5,items.size());
    }

    @Test
    public void getPartsByDomain(){

        // Source
        Source source = new Source();
        source.setUri(uriGenerator.newSource());
        udm.saveSource(source);

        // Domain
        Domain domain = new Domain();
        domain.setUri(uriGenerator.newDomain());
        udm.saveDomain(domain);

        // Document 1
        Document doc1 = new Document();
        doc1.setUri(uriGenerator.newDocument());
        udm.saveDocument(doc1,source.getUri());
        // -> in domain
        udm.relateDocumentToDomain(doc1.getUri(),domain.getUri(),timeGenerator.getNowAsISO());
        // -> Item 1
        Item item11 = new Item();
        item11.setUri(uriGenerator.newItem());
        udm.saveItem(item11,doc1.getUri());
        // -> -> Part 1
        Part part111 = new Part();
        part111.setUri(uriGenerator.newPart());
        udm.savePart(part111,item11.getUri());
        // -> -> Part 2
        Part part112 = new Part();
        part112.setUri(uriGenerator.newPart());
        udm.savePart(part112,item11.getUri());
        // -> Item2
        Item item12 = new Item();
        item12.setUri(uriGenerator.newItem());
        udm.saveItem(item12,doc1.getUri());
        // -> -> Part 1
        Part part121 = new Part();
        part121.setUri(uriGenerator.newPart());
        udm.savePart(part121,item12.getUri());

        // Document 2
        Document doc2 = new Document();
        doc2.setUri(uriGenerator.newDocument());
        udm.saveDocument(doc2,source.getUri());
        // -> in domain
        udm.relateDocumentToDomain(doc2.getUri(),domain.getUri(),timeGenerator.getNowAsISO());
        // -> Item 1
        Item item21 = new Item();
        item21.setUri(uriGenerator.newItem());
        udm.saveItem(item21,doc2.getUri());
        // -> -> Part 1
        Part part211 = new Part();
        part211.setUri(uriGenerator.newPart());
        udm.savePart(part211,item21.getUri());
        // -> -> Part 2
        Part part212 = new Part();
        part212.setUri(uriGenerator.newPart());
        udm.savePart(part212,item21.getUri());
        // -> Item 2
        Item item22 = new Item();
        item22.setUri(uriGenerator.newItem());
        udm.saveItem(item22,doc2.getUri());
        // -> Item 3
        Item item23 = new Item();
        item23.setUri(uriGenerator.newItem());
        udm.saveItem(item23,doc2.getUri());

        // Getting parts in a domain
        List<String> parts = udm.findPartsByDomain(domain.getUri());

        // Delete
        udm.deleteSource(source.getUri());
        udm.deleteDomain(domain.getUri());
        udm.deleteDocument(doc1.getUri());
        udm.deleteDocument(doc2.getUri());
        udm.deleteItem(item11.getUri());
        udm.deleteItem(item12.getUri());
        udm.deleteItem(item21.getUri());
        udm.deleteItem(item22.getUri());
        udm.deleteItem(item23.getUri());
        udm.deletePart(part111.getUri());
        udm.deletePart(part112.getUri());
        udm.deletePart(part121.getUri());
        udm.deletePart(part211.getUri());
        udm.deletePart(part212.getUri());

        Assert.assertTrue(parts != null);
        Assert.assertEquals(5,parts.size());
    }

    @Test
    public void updateRelationships(){
        Word word = new Word();
        word.setUri(uriGenerator.newWord());
        udm.saveWord(word);

        Domain domain = new Domain();
        domain.setUri(uriGenerator.newDomain());
        udm.saveDomain(domain);

        udm.relateWordToDomain(word.getUri(),domain.getUri(),"vector1");

        udm.deleteEmbeddingWordsInDomain(domain.getUri());

        udm.relateWordToDomain(word.getUri(),domain.getUri(),"vector2");

        LOG.info("sample");
    }

    @Test
    public void query(){
        String document = "http://epnoi.org/documents/790b1e35-51f9-4c74-8a63-bbd0a17738f3";
        String domain = "http://epnoi.org/domains/72dba453-eaba-4cb6-99f3-a456e96f3768";



        List<Relationship> res = udm.findDealsByDocumentInDomain(document, domain);
        LOG.info("Result: " + res);

        Iterable<TopicDealtByDocument> res2 = documentGraphRepository.dealsInDomain(document, domain);
        LOG.info("Result2: " + res2);



    }

    @Test
    public void deleteEmbeddedRelations(){

        try {
            String domain = "http://epnoi.org/domains/d4a5f93d-fc90-453e-a2d5-7ca27dfb4e29";
            String word = "http://epnoi.org/words/67f76420-4d11-42d5-a692-f2bd5c353ac9";

            LOG.info("First loop");
            udm.deleteEmbeddingWordsInDomain(domain);
            udm.deleteSimilarsBetweenWordsInDomain(domain);
            udm.relateWordToDomain(word, domain, "");

            LOG.info("Second loop");
            udm.deleteEmbeddingWordsInDomain(domain);
            udm.deleteSimilarsBetweenWordsInDomain(domain);
            udm.relateWordToDomain(word, domain, "");

            LOG.info("Third loop");
            udm.deleteEmbeddingWordsInDomain(domain);
            udm.deleteSimilarsBetweenWordsInDomain(domain);
            udm.relateWordToDomain(word, domain, "");


        }catch (Exception e){
            LOG.error("Error",e);
        }
    }

    @Test
    public void sourceAndDomain(){

        String sourceURI = "http://epnoi.org/sources/48afa130-28c1-4bb3-bbc2-dac5da760fa1";
        String domainURI = "http://epnoi.org/domains/72dba453-eaba-4cb6-99f3-a456e96f3768";

        Iterable<DomainNode> domains = domainGraphRepository.findBySource(sourceURI);
        LOG.info("Domains: "+ domains);


        SourceNode source = sourceGraphRepository.findOneByUri(sourceURI);

        DomainNode domain = domainGraphRepository.findOneByUri(domainURI);


        DomainComposedBySource relation = new DomainComposedBySource();
        relation.setSource(source);
        relation.setDomain(domain);
        relation.setDate("sample");
        source.addDomainComposedBySource(relation);

        sourceGraphRepository.save(source);

        domains = domainGraphRepository.findBySource(sourceURI);
        LOG.info("Domains: "+ domains);
    }

    @Test
    public void findWordByDomain(){

        List<String> domains = Arrays.asList(new String[]{
                "http://epnoi.org/domains/e19f1196-9233-41be-9eb0-f3ee5895998d",
                "http://epnoi.org/domains/031a7709-17d0-4b53-9982-4feac9281082",
                "http://epnoi.org/domains/6be14ee9-72ac-4c35-868f-9735851b1042",
                "http://epnoi.org/domains/cb0b34ba-9401-48da-8a7a-c435361516a4"
        });

        domains.forEach(domain -> {
            List<String> wordsByDomain = udm.findWordsByDomain(domain);
            LOG.info("Words in domain '" + domain + "': " + wordsByDomain.size());
        });


        List<String> words = udm.findWords();
        LOG.info("Total words: " + words.size());

    }

    @Test
    public void findDomain(){

        DomainNode domain = domainGraphRepository.findOneByUri("http://epnoi.org/domains/4830d78e-a6c4-440a-951c-6afe080d41f4");

        LOG.info("Domain: " + domain);


        LOG.info("Related Documents: " + domain.getDocuments());

    }


    @Test
    public void updateDocuments(){

        String sourceURI    = "http://epnoi.org/sources/f561032f-a583-478e-bcdc-60859f603c1d";
        String domainURI    = "http://epnoi.org/domains/1d61ca7c-9cdf-4b22-8356-2a2c1e945186";

        String documentURI1  = "http://epnoi.org/documents/e126afcd-8acd-43e4-93a0-7bb086bbaa39";
        String documentURI2  = "http://epnoi.org/documents/d854a2a8-b6bb-4c35-a2dc-0efd3603d5fc";
        String documentURI3  = "http://epnoi.org/documents/181cdd4c-7711-410a-9050-e064416fd0e9";

//        udm.findTopicsByDomain(domainURI).stream().forEach(topic -> udm.deleteTopic(topic));

//        udm.relateDocumentToDocument(documentURI1,documentURI2,1.0,domainURI);
//        udm.relateDocumentToDocument(documentURI2,documentURI1,1.0,domainURI);
//        udm.relateDocumentToDocument(documentURI1,documentURI3,1.0,domainURI);
//        udm.relateDocumentToDocument(documentURI3,documentURI1,1.0,domainURI);
//        udm.relateDocumentToDocument(documentURI2,documentURI3,1.0,domainURI);
//        udm.relateDocumentToDocument(documentURI3,documentURI2,1.0,domainURI);

//        udm.relateDocumentToDomain(documentURI1,domainURI,"");
//        udm.relateDocumentToDomain(documentURI2,domainURI,"");

//        udm.deleteSimilarsBetweenDocumentsInDomain(domainURI);

        // Clean Similar relations
        udm.deleteSimilarsBetweenWordsInDomain(domainURI);

        // Clean Embedded relations
        udm.deleteEmbeddingWordsInDomain(domainURI);

    }

    @Test
    public void findWord (){

        String wordURI = "http://epnoi.org/words/f643a3b3-16ce-4158-b4f8-0f82c53092bc";
        String domainURI ="http://epnoi.org/domains/382130c5-1d84-4b21-a591-90d2c235f0a5";

        WordDocument doc = wordDocumentRepository.findOne(wordURI);
        System.out.println(doc);
    }



}
