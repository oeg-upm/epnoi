package org.epnoi.storage;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.Event;
import org.epnoi.model.domain.relations.HypernymOf;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.*;
import org.epnoi.model.modules.*;
import org.epnoi.storage.generator.URIGenerator;
import org.epnoi.storage.system.document.domain.WordDocument;
import org.epnoi.storage.system.document.repository.WordDocumentRepository;
import org.epnoi.storage.system.graph.domain.edges.SimilarToDocumentsEdge;
import org.epnoi.storage.system.graph.repository.edges.DealsWithFromDocumentEdgeRepository;
import org.epnoi.storage.system.graph.repository.edges.SimilarToEdgeRepository;
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

import java.util.*;
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
        "epnoi.eventbus.host = drinventor.dia.fi.upm.es"})
public class UDMTest {

    private static final Logger LOG = LoggerFactory.getLogger(UDMTest.class);

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


    @Autowired
    SimilarToEdgeRepository similarToEdgeRepository;


    @Test
    public void publish(){
        Domain domain = new Domain();
        domain.setUri("http://drinventor.eu/domains/7df34748-7fad-486e-a799-3bcd86a03499");
        domain.setName("siggraph");
        domain.setDescription("siggraph");
        eventBus.post(Event.from(domain),RoutingKey.of(Resource.Type.DOMAIN,Resource.State.UPDATED));
    }
    

    @Test
    public void removeSimilarities(){

        Domain domain = Resource.newDomain();
        domain.setUri("http://epnoi.org/domains/ce123683-512a-4f2a-a539-c77b666a8b79");
        domain.setName("siggraph");

        udm.find(Resource.Type.TOPIC).in(Resource.Type.DOMAIN,domain.getUri()).stream().forEach(topic -> udm.delete(Resource.Type.TOPIC).byUri(topic));
    }


    @Test
    public void read(){

        String startUri = "http://drinventor.eu/documents/af351b184d0bc10597573d31544a23a4";
        String endUri = "http://drinventor.eu/topics/72510e5c-f3b3-4a17-ab43-bc64b67a7db3";

        System.out.println(udm.find(Relation.Type.DEALS_WITH_FROM_DOCUMENT).btw(startUri, endUri));
    }


    @Test
    public void hypernym(){

        HypernymOf hypernym = Relation.newHypernymOf("http://epnoi.org/terms/3324e10e-87c5-49a5-a9ba-c79adf3beba0", "http://epnoi.org/terms/4cbd8d67-05d1-4d0b-b1da-7ccd3244298a");
        hypernym.setDomain("http://epnoi.org/domains/28cd53dc-bc1c-417d-9ae5-2b5a7052d819");
        hypernym.setWeight(0.04766949152542373);
        udm.save(hypernym);

        assert true;
    }


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


        Source source = Resource.newSource();
        source.setUri("http://epnoi.org/sources/0b3e80ae-d598-4dd4-8c54-38e2229f0bf8");
        source.setUrl("file://opt/epnoi/inbox/upm");
        source.setName("test-source");
        source.setProtocol("file");
        source.setCreationTime("20160101T22:02");
        source.setDescription("testing purposes");

        LOG.info("Saving source: " + source);
        udm.save(source);
        LOG.info("source saved!");

        Optional<Resource> source2 = udm.read(Resource.Type.SOURCE).byUri(source.getUri());
        Assert.assertTrue(source2.isPresent());
        Assert.assertEquals(source.getUri(),source2.get().getUri());
        Assert.assertEquals(source.getName(),source2.get().asSource().getName());

        LOG.info("Deleting source: " + source);
        udm.delete(Resource.Type.SOURCE).byUri(source.getUri());
        LOG.info("source deleted!");

        Optional<Resource> source3 = udm.read(Resource.Type.SOURCE).byUri(source.getUri());
        Assert.assertFalse(source3.isPresent());

    }

    @Test
    public void findDocumentsInDomain(){
        // Source
        Source source = Resource.newSource();
        udm.save(source);

        // Domain
        Domain domain = Resource.newDomain();
        udm.save(domain);

        // Document 1
        Document doc1 = Resource.newDocument();
        udm.save(doc1);
        // -> document1 in source
        udm.save(Relation.newProvides(source.getUri(),doc1.getUri()));
        // -> document1 in domain
        udm.save(Relation.newContains(domain.getUri(),doc1.getUri()));

        // Document 2
        Document doc2 = Resource.newDocument();
        udm.save(doc2);
        // -> document2 in source
        udm.save(Relation.newProvides(source.getUri(),doc2.getUri()));
        // -> document2 in domain
        udm.save(Relation.newContains(domain.getUri(),doc2.getUri()));

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
    public void getTopicDistributionOfDocumentsInDomain(){

        String domain = "http://epnoi.org/domains/72dba453-eaba-4cb6-99f3-a456e96f3768";

        Iterable<Relation> relations = udm.find(Relation.Type.DEALS_WITH_FROM_DOCUMENT).in(Resource.Type.DOMAIN, domain);
        LOG.info("Result: " + relations);

    }

    @Test
    public void deleteAndCreateEmbeddedINRelations(){

        try {
            String domain = "http://epnoi.org/domains/d4a5f93d-fc90-453e-a2d5-7ca27dfb4e29";
            String word = "http://epnoi.org/words/67f76420-4d11-42d5-a692-f2bd5c353ac9";

            LOG.info("First loop");
            udm.delete(Relation.Type.EMBEDDED_IN).in(Resource.Type.DOMAIN,domain);


            Iterable<Relation> pairs = udm.find(Relation.Type.PAIRS_WITH).in(Resource.Type.DOMAIN, domain);
            if (pairs != null){
                for (Relation pair : pairs) {
                    udm.delete(Relation.Type.PAIRS_WITH).byUri(pair.getUri());
                }
            }

            udm.save(Relation.newEmbeddedIn(word,domain));


            LOG.info("Second loop");
            udm.delete(Relation.Type.EMBEDDED_IN).in(Resource.Type.DOMAIN,domain);
            udm.delete(Relation.Type.PAIRS_WITH).in(Resource.Type.DOMAIN,domain);
            udm.save(Relation.newEmbeddedIn(word,domain));

            LOG.info("Third loop");
            udm.delete(Relation.Type.EMBEDDED_IN).in(Resource.Type.DOMAIN,domain);
            udm.delete(Relation.Type.PAIRS_WITH).in(Resource.Type.DOMAIN,domain);
            udm.save(Relation.newEmbeddedIn(word,domain));


        }catch (Exception e){
            LOG.error("Error",e);
        }
    }

    public void deleteAll(){
        udm.delete(Resource.Type.ANY).all();
    }


    @Test
    public void findWordsInDomains(){

        List<String> domains = Arrays.asList(new String[]{
                "http://epnoi.org/domains/e19f1196-9233-41be-9eb0-f3ee5895998d",
                "http://epnoi.org/domains/031a7709-17d0-4b53-9982-4feac9281082",
                "http://epnoi.org/domains/6be14ee9-72ac-4c35-868f-9735851b1042",
                "http://epnoi.org/domains/cb0b34ba-9401-48da-8a7a-c435361516a4"
        });

        domains.forEach(domain -> {
            List<String> wordsByDomain = udm.find(Resource.Type.WORD).in(Resource.Type.DOMAIN,domain);
            LOG.info("Domain '" + domain + "' contains: " + wordsByDomain.size() + " words");
        });


        List<String> words = udm.find(Resource.Type.WORD).all();
        LOG.info("Total words: " + words.size());

    }

    @Test
    public void readADomain(){

        Optional<Resource>  domain = udm.read(Resource.Type.DOMAIN).byUri("http://epnoi.org/domains/4830d78e-a6c4-440a-951c-6afe080d41f4");
        LOG.info("Domain: " + domain);
    }


    @Test
    public void findWord (){

        String wordURI = "http://epnoi.org/words/f643a3b3-16ce-4158-b4f8-0f82c53092bc";
        String domainURI ="http://epnoi.org/domains/382130c5-1d84-4b21-a591-90d2c235f0a5";

        WordDocument doc = wordDocumentRepository.findOne(wordURI);
        System.out.println(doc);
    }

}
