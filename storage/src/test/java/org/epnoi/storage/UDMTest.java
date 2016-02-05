package org.epnoi.storage;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.Event;
import org.epnoi.model.domain.*;
import org.epnoi.model.modules.BindingKey;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.EventBusSubscriber;
import org.epnoi.model.modules.RoutingKey;
import org.epnoi.storage.generator.TimeGenerator;
import org.epnoi.storage.generator.URIGenerator;
import org.epnoi.storage.system.document.domain.WordDocument;
import org.epnoi.storage.system.document.repository.WordDocumentRepository;
import org.epnoi.storage.system.graph.domain.edges.DocumentDealsWithTopic;
import org.epnoi.storage.system.graph.domain.edges.SourceComposesDomain;
import org.epnoi.storage.system.graph.domain.nodes.DocumentNode;
import org.epnoi.storage.system.graph.domain.nodes.DomainNode;
import org.epnoi.storage.system.graph.domain.nodes.SourceNode;
import org.epnoi.storage.system.graph.domain.nodes.TopicNode;
import org.epnoi.storage.system.graph.repository.edges.DocumentDealsWithTopicGraphRepository;
import org.epnoi.storage.system.graph.repository.nodes.DocumentGraphRepository;
import org.epnoi.storage.system.graph.repository.nodes.DomainGraphRepository;
import org.epnoi.storage.system.graph.repository.nodes.SourceGraphRepository;
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
    DocumentDealsWithTopicGraphRepository documentDealsWithTopicGraphRepository;

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
        udm.save(Resource.Type.SOURCE).with(source);
        LOG.info("source saved!");

        Optional<Resource> source2 = udm.read(Resource.Type.SOURCE).byUri(source.getUri());
        Assert.assertTrue(source2.isPresent());
        Assert.assertEquals(source.getUri(),source2.get().getUri());
        Assert.assertEquals(source.getName(),((Source)source2.get()).getName());

        LOG.info("Deleting source: " + source);
        udm.delete(Resource.Type.SOURCE).byUri(source.getUri());
        LOG.info("source deleted!");

        Optional<Resource> source3 = udm.read(Resource.Type.SOURCE).byUri(source.getUri());
        Assert.assertTrue(source3.isPresent());
        Assert.assertNotEquals(source,source3.get());

        Assert.assertEquals(1, counter.get());

    }

    @Test
    public void getDocumentsByDomain(){
        // Source
        Source source = new Source();
        source.setUri(uriGenerator.newFor(Resource.Type.SOURCE));
        udm.save(Resource.Type.SOURCE).with(source);

        // Domain
        Domain domain = new Domain();
        domain.setUri(uriGenerator.newFor(Resource.Type.DOMAIN));
        udm.save(Resource.Type.DOMAIN).with(domain);

        // Document 1
        Document doc1 = new Document();
        doc1.setUri(uriGenerator.newFor(Resource.Type.DOCUMENT));
        udm.save(Resource.Type.DOCUMENT).with(doc1);
        // -> in source
        udm.attachFrom(source.getUri()).to(doc1.getUri()).by(Relation.Type.SOURCE_PROVIDES_DOCUMENT,RelationProperties.builder().date(timeGenerator.asISO()).build());
        // -> in domain
        udm.attachFrom(domain.getUri()).to(doc1.getUri()).by(Relation.Type.DOMAIN_CONTAINS_DOCUMENT,RelationProperties.builder().date(timeGenerator.asISO()).build());


        // Document 2
        Document doc2 = new Document();
        doc2.setUri(uriGenerator.newFor(Resource.Type.DOCUMENT));
        udm.save(Resource.Type.DOCUMENT).with(doc2);
        // -> in source
        udm.attachFrom(source.getUri()).to(doc2.getUri()).by(Relation.Type.SOURCE_PROVIDES_DOCUMENT,RelationProperties.builder().date(timeGenerator.asISO()).build());
        // -> in domain
        udm.attachFrom(domain.getUri()).to(doc2.getUri()).by(Relation.Type.DOMAIN_CONTAINS_DOCUMENT,RelationProperties.builder().date(timeGenerator.asISO()).build());

        // Getting Documents
        List<String> documents = udm.find(Resource.Type.DOCUMENT).in(Resource.Type.DOMAIN,domain.getUri());

        // Delete
        udm.delete(Resource.Type.SOURCE).byUri(source.getUri());
        udm.delete(Resource.Type.DOMAIN).byUri(domain.getUri());
        udm.delete(Resource.Type.DOCUMENT).byUri(doc1.getUri());
        udm.delete(Resource.Type.DOCUMENT).byUri(doc2.getUri());

        Assert.assertTrue(documents != null);
        Assert.assertEquals(2,documents.size());
    }

    @Test
    public void getItemsByDomain(){
        // Source
        Source source = new Source();
        source.setUri(uriGenerator.newFor(Resource.Type.SOURCE));
        udm.save(Resource.Type.SOURCE).with(source);

        // Domain
        Domain domain = new Domain();
        domain.setUri(uriGenerator.newFor(Resource.Type.DOMAIN));
        udm.save(Resource.Type.DOMAIN).with(domain);

        // Document 1
        Document doc1 = new Document();
        doc1.setUri(uriGenerator.newFor(Resource.Type.DOCUMENT));
        udm.save(Resource.Type.DOCUMENT).with(doc1);
        // -> in source
        udm.attachFrom(source.getUri()).to(doc1.getUri()).by(Relation.Type.SOURCE_PROVIDES_DOCUMENT,RelationProperties.builder().date(timeGenerator.asISO()).build());
        // -> in domain
        udm.attachFrom(domain.getUri()).to(doc1.getUri()).by(Relation.Type.DOMAIN_CONTAINS_DOCUMENT,RelationProperties.builder().date(timeGenerator.asISO()).build());

        // -> Item 1
        Item item11 = new Item();
        item11.setUri(uriGenerator.newFor(Resource.Type.ITEM));
        udm.save(Resource.Type.ITEM).with(item11);
        // -> in document
        udm.attachFrom(doc1.getUri()).to(item11.getUri()).by(Relation.Type.DOCUMENT_BUNDLES_ITEM,RelationProperties.builder().date(timeGenerator.asISO()).build());

        // -> Item 2
        Item item12 = new Item();
        item12.setUri(uriGenerator.newFor(Resource.Type.ITEM));
        udm.save(Resource.Type.ITEM).with(item12);
        // -> in document
        udm.attachFrom(doc1.getUri()).to(item12.getUri()).by(Relation.Type.DOCUMENT_BUNDLES_ITEM,RelationProperties.builder().date(timeGenerator.asISO()).build());

        // Document 2
        Document doc2 = new Document();
        doc2.setUri(uriGenerator.newFor(Resource.Type.DOCUMENT));
        udm.save(Resource.Type.DOCUMENT).with(doc2);
        // -> in source
        udm.attachFrom(source.getUri()).to(doc2.getUri()).by(Relation.Type.SOURCE_PROVIDES_DOCUMENT,RelationProperties.builder().date(timeGenerator.asISO()).build());
        // -> in domain
        udm.attachFrom(domain.getUri()).to(doc2.getUri()).by(Relation.Type.DOMAIN_CONTAINS_DOCUMENT,RelationProperties.builder().date(timeGenerator.asISO()).build());

        // -> Item 1
        Item item21 = new Item();
        item21.setUri(uriGenerator.newFor(Resource.Type.ITEM));
        udm.save(Resource.Type.ITEM).with(item21);
        // -> in document
        udm.attachFrom(doc2.getUri()).to(item21.getUri()).by(Relation.Type.DOCUMENT_BUNDLES_ITEM,RelationProperties.builder().date(timeGenerator.asISO()).build());

        // -> Item 2
        Item item22 = new Item();
        item22.setUri(uriGenerator.newFor(Resource.Type.ITEM));
        udm.save(Resource.Type.ITEM).with(item22);
        // -> in document
        udm.attachFrom(doc2.getUri()).to(item22.getUri()).by(Relation.Type.DOCUMENT_BUNDLES_ITEM,RelationProperties.builder().date(timeGenerator.asISO()).build());

        // -> Item 3
        Item item23 = new Item();
        item23.setUri(uriGenerator.newFor(Resource.Type.ITEM));
        udm.save(Resource.Type.ITEM).with(item23);
        // -> in document
        udm.attachFrom(doc2.getUri()).to(item23.getUri()).by(Relation.Type.DOCUMENT_BUNDLES_ITEM,RelationProperties.builder().date(timeGenerator.asISO()).build());


        // Getting items in domain
        List<String> items = udm.find(Resource.Type.ITEM).in(Resource.Type.DOMAIN,domain.getUri());

        // Delete
        udm.delete(Resource.Type.SOURCE).byUri(source.getUri());
        udm.delete(Resource.Type.DOMAIN).byUri(domain.getUri());
        udm.delete(Resource.Type.DOCUMENT).byUri(doc1.getUri());
        udm.delete(Resource.Type.DOCUMENT).byUri(doc2.getUri());
        udm.delete(Resource.Type.ITEM).byUri(item11.getUri());
        udm.delete(Resource.Type.ITEM).byUri(item12.getUri());
        udm.delete(Resource.Type.ITEM).byUri(item21.getUri());
        udm.delete(Resource.Type.ITEM).byUri(item22.getUri());
        udm.delete(Resource.Type.ITEM).byUri(item23.getUri());

        Assert.assertTrue(items != null);
        Assert.assertEquals(5,items.size());
    }

    @Test
    public void getPartsByDomain(){

        // Source
        Source source = new Source();
        source.setUri(uriGenerator.newFor(Resource.Type.SOURCE));
        udm.save(Resource.Type.SOURCE).with(source);

        // Domain
        Domain domain = new Domain();
        domain.setUri(uriGenerator.newFor(Resource.Type.DOMAIN));
        udm.save(Resource.Type.DOMAIN).with(domain);

        // Document 1
        Document doc1 = new Document();
        doc1.setUri(uriGenerator.newFor(Resource.Type.DOCUMENT));
        udm.save(Resource.Type.DOCUMENT).with(doc1);
        // -> in source
        udm.attachFrom(source.getUri()).to(doc1.getUri()).by(Relation.Type.SOURCE_PROVIDES_DOCUMENT,RelationProperties.builder().date(timeGenerator.asISO()).build());
        // -> in domain
        udm.attachFrom(domain.getUri()).to(doc1.getUri()).by(Relation.Type.DOMAIN_CONTAINS_DOCUMENT,RelationProperties.builder().date(timeGenerator.asISO()).build());

        // -> Item 1
        Item item11 = new Item();
        item11.setUri(uriGenerator.newFor(Resource.Type.ITEM));
        udm.save(Resource.Type.ITEM).with(item11);
        // -> in document
        udm.attachFrom(doc1.getUri()).to(item11.getUri()).by(Relation.Type.DOCUMENT_BUNDLES_ITEM,RelationProperties.builder().date(timeGenerator.asISO()).build());


        // -> -> Part 1
        Part part111 = new Part();
        part111.setUri(uriGenerator.newFor(Resource.Type.PART));
        udm.save(Resource.Type.PART).with(part111);
        // -> item
        udm.attachFrom(part111.getUri()).to(item11.getUri()).by(Relation.Type.PART_DESCRIBES_ITEM,RelationProperties.builder().date(timeGenerator.asISO()).build());

        // -> -> Part 2
        Part part112 = new Part();
        part112.setUri(uriGenerator.newFor(Resource.Type.PART));
        udm.save(Resource.Type.PART).with(part112);
        // -> item
        udm.attachFrom(part112.getUri()).to(item11.getUri()).by(Relation.Type.PART_DESCRIBES_ITEM,RelationProperties.builder().date(timeGenerator.asISO()).build());



        // -> Item 2
        Item item12 = new Item();
        item12.setUri(uriGenerator.newFor(Resource.Type.ITEM));
        udm.save(Resource.Type.ITEM).with(item12);
        // -> in document
        udm.attachFrom(doc1.getUri()).to(item12.getUri()).by(Relation.Type.DOCUMENT_BUNDLES_ITEM,RelationProperties.builder().date(timeGenerator.asISO()).build());

        // -> -> Part 1
        Part part121 = new Part();
        part121.setUri(uriGenerator.newFor(Resource.Type.PART));
        udm.save(Resource.Type.PART).with(part121);
        // -> item
        udm.attachFrom(part121.getUri()).to(item12.getUri()).by(Relation.Type.PART_DESCRIBES_ITEM,RelationProperties.builder().date(timeGenerator.asISO()).build());



        // Document 2
        Document doc2 = new Document();
        doc2.setUri(uriGenerator.newFor(Resource.Type.DOCUMENT));
        udm.save(Resource.Type.DOCUMENT).with(doc2);
        // -> in source
        udm.attachFrom(source.getUri()).to(doc2.getUri()).by(Relation.Type.SOURCE_PROVIDES_DOCUMENT,RelationProperties.builder().date(timeGenerator.asISO()).build());
        // -> in domain
        udm.attachFrom(domain.getUri()).to(doc2.getUri()).by(Relation.Type.DOMAIN_CONTAINS_DOCUMENT,RelationProperties.builder().date(timeGenerator.asISO()).build());

        // -> Item 1
        Item item21 = new Item();
        item21.setUri(uriGenerator.newFor(Resource.Type.ITEM));
        udm.save(Resource.Type.ITEM).with(item21);
        // -> in document
        udm.attachFrom(doc2.getUri()).to(item21.getUri()).by(Relation.Type.DOCUMENT_BUNDLES_ITEM,RelationProperties.builder().date(timeGenerator.asISO()).build());

        // -> -> Part 1
        Part part211 = new Part();
        part211.setUri(uriGenerator.newFor(Resource.Type.PART));
        udm.save(Resource.Type.PART).with(part211);
        // -> item
        udm.attachFrom(part211.getUri()).to(item21.getUri()).by(Relation.Type.PART_DESCRIBES_ITEM,RelationProperties.builder().date(timeGenerator.asISO()).build());

        // -> -> Part 2
        Part part212 = new Part();
        part212.setUri(uriGenerator.newFor(Resource.Type.PART));
        udm.save(Resource.Type.PART).with(part212);
        // -> item
        udm.attachFrom(part212.getUri()).to(item21.getUri()).by(Relation.Type.PART_DESCRIBES_ITEM,RelationProperties.builder().date(timeGenerator.asISO()).build());




        // -> Item 2
        Item item22 = new Item();
        item22.setUri(uriGenerator.newFor(Resource.Type.ITEM));
        udm.save(Resource.Type.ITEM).with(item22);
        // -> in document
        udm.attachFrom(doc2.getUri()).to(item22.getUri()).by(Relation.Type.DOCUMENT_BUNDLES_ITEM,RelationProperties.builder().date(timeGenerator.asISO()).build());


        // -> Item 3
        Item item23 = new Item();
        item23.setUri(uriGenerator.newFor(Resource.Type.ITEM));
        udm.save(Resource.Type.ITEM).with(item23);
        // -> in document
        udm.attachFrom(doc2.getUri()).to(item23.getUri()).by(Relation.Type.DOCUMENT_BUNDLES_ITEM,RelationProperties.builder().date(timeGenerator.asISO()).build());

        // Getting parts in a domain
        List<String> parts = udm.find(Resource.Type.PART).in(Resource.Type.DOMAIN,domain.getUri());

        // Delete
        udm.delete(Resource.Type.SOURCE).byUri(source.getUri());
        udm.delete(Resource.Type.DOMAIN).byUri(domain.getUri());
        udm.delete(Resource.Type.DOCUMENT).byUri(doc1.getUri());
        udm.delete(Resource.Type.DOCUMENT).byUri(doc2.getUri());
        udm.delete(Resource.Type.ITEM).byUri(item11.getUri());
        udm.delete(Resource.Type.ITEM).byUri(item12.getUri());
        udm.delete(Resource.Type.ITEM).byUri(item21.getUri());
        udm.delete(Resource.Type.ITEM).byUri(item22.getUri());
        udm.delete(Resource.Type.ITEM).byUri(item23.getUri());
        udm.delete(Resource.Type.PART).byUri(part111.getUri());
        udm.delete(Resource.Type.PART).byUri(part112.getUri());
        udm.delete(Resource.Type.PART).byUri(part121.getUri());
        udm.delete(Resource.Type.PART).byUri(part211.getUri());
        udm.delete(Resource.Type.PART).byUri(part212.getUri());

        Assert.assertTrue(parts != null);
        Assert.assertEquals(5,parts.size());
    }

    @Test
    public void updateRelationships(){
        Word word = new Word();
        word.setUri(uriGenerator.newFor(Resource.Type.WORD));
        udm.save(Resource.Type.WORD).with(word);

        Domain domain = new Domain();
        domain.setUri(uriGenerator.newFor(Resource.Type.DOMAIN));
        udm.save(Resource.Type.DOMAIN).with(domain);

        udm.attachFrom(word.getUri()).to(domain.getUri()).by(Relation.Type.WORD_EMBEDDED_IN_DOMAIN,RelationProperties.builder().description("vector1").build());

        udm.detachFrom(word.getUri()).to(domain.getUri()).by(Relation.Type.WORD_EMBEDDED_IN_DOMAIN);

        udm.attachFrom(word.getUri()).to(domain.getUri()).by(Relation.Type.WORD_EMBEDDED_IN_DOMAIN,RelationProperties.builder().description("vector2").build());

        LOG.info("sample");
    }

    @Test
    public void query(){
        String document = "http://epnoi.org/documents/790b1e35-51f9-4c74-8a63-bbd0a17738f3";
        String domain = "http://epnoi.org/domains/72dba453-eaba-4cb6-99f3-a456e96f3768";


        Iterable<Relation> relations = udm.find(Relation.Type.DOCUMENT_DEALS_WITH_TOPIC).in(Resource.Type.DOMAIN, domain);
        LOG.info("Result: " + relations);

    }

    @Test
    public void deleteEmbeddedRelations(){

        try {
            String domain = "http://epnoi.org/domains/d4a5f93d-fc90-453e-a2d5-7ca27dfb4e29";
            String word = "http://epnoi.org/words/67f76420-4d11-42d5-a692-f2bd5c353ac9";

            LOG.info("First loop");
            udm.delete(Relation.Type.WORD_EMBEDDED_IN_DOMAIN).in(Resource.Type.DOMAIN,domain);


            Iterable<Relation> pairs = udm.find(Relation.Type.WORD_PAIRS_WITH_WORD).in(Resource.Type.DOMAIN, domain);
            if (pairs != null){
                for (Relation pair : pairs) {
                    udm.detachFrom(pair.getStart().getUri()).to(pair.getEnd().getUri()).by(Relation.Type.WORD_PAIRS_WITH_WORD);
                }
            }


            udm.attachFrom(word).to(domain).by(Relation.Type.WORD_EMBEDDED_IN_DOMAIN,RelationProperties.builder().build());


            LOG.info("Second loop");
            udm.delete(Relation.Type.WORD_EMBEDDED_IN_DOMAIN).in(Resource.Type.DOMAIN,domain);
            udm.delete(Relation.Type.WORD_PAIRS_WITH_WORD).in(Resource.Type.DOMAIN,domain);
            udm.attachFrom(word).to(domain).by(Relation.Type.WORD_EMBEDDED_IN_DOMAIN,RelationProperties.builder().build());

            LOG.info("Third loop");
            udm.delete(Relation.Type.WORD_EMBEDDED_IN_DOMAIN).in(Resource.Type.DOMAIN,domain);
            udm.delete(Relation.Type.WORD_PAIRS_WITH_WORD).in(Resource.Type.DOMAIN,domain);
            udm.attachFrom(word).to(domain).by(Relation.Type.WORD_EMBEDDED_IN_DOMAIN,RelationProperties.builder().build());


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


        SourceComposesDomain relation = new SourceComposesDomain();
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
            List<String> wordsByDomain = udm.find(Resource.Type.WORD).in(Resource.Type.DOMAIN,domain);
            LOG.info("Words in domain '" + domain + "': " + wordsByDomain.size());
        });


        List<String> words = udm.find(Resource.Type.WORD).all();
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
        udm.delete(Relation.Type.WORD_PAIRS_WITH_WORD).in(Resource.Type.DOMAIN,domainURI);

        // Clean Embedded relations
        udm.delete(Relation.Type.WORD_EMBEDDED_IN_DOMAIN).in(Resource.Type.DOMAIN,domainURI);

    }

    @Test
    public void findWord (){

        String wordURI = "http://epnoi.org/words/f643a3b3-16ce-4158-b4f8-0f82c53092bc";
        String domainURI ="http://epnoi.org/domains/382130c5-1d84-4b21-a591-90d2c235f0a5";

        WordDocument doc = wordDocumentRepository.findOne(wordURI);
        System.out.println(doc);
    }


    @Test
    public void insertDeals(){

        udm.delete(Resource.Type.ANY);

        DocumentNode document = new DocumentNode();
        document.setUri("docs/1");
        udm.save(Resource.Type.DOCUMENT).with(document);

        DomainNode domain = new DomainNode();
        domain.setUri("domains/1");
        udm.save(Resource.Type.DOMAIN).with(domain);

        TopicNode topic = new TopicNode();
        topic.setUri("topics/1");
        udm.save(Resource.Type.TOPIC).with(topic);


        udm.attachFrom(domain.getUri()).to(document.getUri()).by(Relation.Type.DOMAIN_CONTAINS_DOCUMENT,RelationProperties.builder().date("2016").build());

        udm.attachFrom(document.getUri()).to(topic.getUri()).by(Relation.Type.DOCUMENT_DEALS_WITH_TOPIC,RelationProperties.builder().weight(0.2).build());

        udm.attachFrom(topic.getUri()).to(domain.getUri()).by(Relation.Type.TOPIC_EMERGES_IN_DOMAIN,RelationProperties.builder().date("2017").description("analysis/1").build());


    }

    @Test
    public void readDeals(){

        Iterable<DocumentDealsWithTopic> deals = documentDealsWithTopicGraphRepository.findAll();
        System.out.println(deals);

        Iterable<DocumentDealsWithTopic> subdeals = documentDealsWithTopicGraphRepository.findByDocumentAndDomain("docs/1", "domains/1");
        System.out.println(subdeals);

        DocumentDealsWithTopic deal = subdeals.iterator().next();
        System.out.println(deal.getDocument());
        System.out.println(deal.getWeight());
        System.out.println(deal.getTopic());

    }


}
