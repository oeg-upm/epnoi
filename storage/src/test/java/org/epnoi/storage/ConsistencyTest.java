package org.epnoi.storage;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.*;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.neo4j.ogm.session.Neo4jSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by cbadenes on 02/03/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {
        "epnoi.cassandra.contactpoints = wiig.dia.fi.upm.es",
        "epnoi.cassandra.port = 5011",
        "epnoi.cassandra.keyspace = research",
        "epnoi.elasticsearch.contactpoints = wiig.dia.fi.upm.es",
        "epnoi.elasticsearch.port = 5021",
        "epnoi.neo4j.contactpoints = wiig.dia.fi.upm.es",
        "epnoi.neo4j.port = 5030",
        "epnoi.eventbus.host = wiig.dia.fi.upm.es"})
public class ConsistencyTest {

    private static final Logger LOG = LoggerFactory.getLogger(ConsistencyTest.class);

    int NUM_WORDS           = 10;
    int NUM_TERMS           = 10;
    int NUM_DOC_TOPICS      = 5;
    int NUM_ITEM_TOPICS     = 6;
    int NUM_PART_TOPICS     = 11;
    int NUM_DOCUMENTS       = 100;
    int NUM_ITEMS           = 1;
    int NUM_PARTS           = 8;


    String domainUri        = "http://drinventor.eu/domains/20160310154040441-7ebbd0a179a8f1a7c254245e8ddb1272";

    // Resources
    int numDocs     = NUM_DOCUMENTS;
    int numItems    = numDocs*NUM_ITEMS;
    int numParts    = numItems*NUM_PARTS;
    int numTopics   = NUM_DOC_TOPICS+NUM_ITEM_TOPICS+NUM_PART_TOPICS;
    int numTerms    = NUM_TERMS;
    int numWords    = NUM_WORDS;

    // Relations
    int numProvides         = numDocs;
    int numComposes         = 1;
    int numContains         = numDocs;
    int numBundles          = numItems;
    int numDescribes        = numParts;
    int numDealsWithDoc     = numDocs*NUM_DOC_TOPICS;
    int numDealsWithItem    = numItems*NUM_ITEM_TOPICS;
    int numDealsWithPart    = numParts*NUM_PART_TOPICS;
    int numEmergesIn        = numTopics;
    int numSimilarDoc       = numDocs*numDocs;
    int numSimilarItem      = numItems*numItems;
    int numSimilarPart      = numParts*numParts;
    int numMentionTopic     = numTopics*numWords;
    int numMentionTerm      = numTerms*numWords;
    int numEmbedded         = numWords;
    int numAppearedIn       = numTerms;
    int numHypernyms        = numTerms*numTerms;
    int numPairs            = numWords*numWords;


    @Autowired
    UDM udm;

    @Test
    public void deleteAll() throws InterruptedException {
        udm.delete(Resource.Type.ANY).all();
    }

    @Test
    public void transitiveRelationships() throws InterruptedException {

        // Summary
        System.out.println("Number of Documents: "  + numDocs);
        System.out.println("Number of Items: "      + numItems);
        System.out.println("Number of Parts: "      + numParts);
        System.out.println("Number of Topics: "     + numTopics);
        System.out.println("Number of Words: "      + numWords);
        System.out.println("Number of Terms: "      + numTerms);
        System.out.println("====");
        System.out.println("Number of Provides: "   + numProvides);
        System.out.println("Number of Composes: "   + numComposes);
        System.out.println("Number of Contains: "   + numContains);
        System.out.println("Number of Bundles: "    + numBundles);
        System.out.println("Number of Describes: "  + numDescribes);
        System.out.println("Number of Deals_With from Docs: "   + numDealsWithDoc);
        System.out.println("Number of Deals_With from Item: "   + numDealsWithItem);
        System.out.println("Number of Deals_With from Parts: "   + numDealsWithPart);
        System.out.println("Number of EmergesIn: "      + numEmergesIn);
        System.out.println("Number of Mentions from Topics: "   + numMentionTopic);
        System.out.println("Number of Mentions from Terms: "    + numMentionTerm);
        System.out.println("Number of Similar Doc: "    + numSimilarDoc);
        System.out.println("Number of Similar Item: "   + numSimilarItem);
        System.out.println("Number of Similar Part: "   + numSimilarPart);
        System.out.println("Number of Embedded Words: " + numEmbedded);
        System.out.println("Number of AppearedIn: "     + numAppearedIn);
        System.out.println("Number of HypernymsOf: "     + numHypernyms);
        System.out.println("Number of PairsWith: "     + numPairs);

        Long start = System.currentTimeMillis();

        // Delete All
        udm.delete(Resource.Type.ANY).all();

        // Source
        Source source = Resource.newSource();
        udm.save(source);

        // Domain
        Domain domain = Resource.newDomain();
        udm.save(domain);
        udm.save(Relation.newComposes(source.getUri(),domain.getUri()));
        domainUri = domain.getUri();

        // Words
        List<Word> words = IntStream.range(0, NUM_WORDS).mapToObj(i -> Resource.newWord()).collect(Collectors.toList());
        words.forEach(word -> {
            udm.save(word);
            udm.save(Relation.newEmbeddedIn(word.getUri(),domainUri));
        });


        // Terms
        List<Term> terms = IntStream.range(0, NUM_TERMS).mapToObj(i -> Resource.newTerm()).collect(Collectors.toList());
        terms.forEach(term -> {
            udm.save(term);
            udm.save(Relation.newAppearedIn(term.getUri(),domainUri));
            words.forEach(word -> udm.save(Relation.newMentionsFromTerm(term.getUri(),word.getUri())));
        });

        // hypernym_of
        terms.forEach(t1 ->{
            terms.forEach(t2 ->{
                udm.save(Relation.newHypernymOf(t1.getUri(),t2.getUri()));
            });
        });

        // pairs_with
        words.forEach(w1 ->{
            words.forEach(w2 ->{
                udm.save(Relation.newPairsWith(w1.getUri(),w2.getUri()));
            });
        });

        // Topics
        List<Topic> docTopics = IntStream.range(0, NUM_DOC_TOPICS).mapToObj(i -> Resource.newTopic()).collect(Collectors.toList());
        docTopics.forEach(topic -> {
            udm.save(topic);
            udm.save(Relation.newEmergesIn(topic.getUri(),domainUri));
            words.forEach(word -> udm.save(Relation.newMentionsFromTopic(topic.getUri(),word.getUri())));

        });

        List<Topic> itemTopics = IntStream.range(0, NUM_ITEM_TOPICS).mapToObj(i -> Resource.newTopic()).collect(Collectors.toList());
        itemTopics.forEach(topic -> {
            udm.save(topic);
            udm.save(Relation.newEmergesIn(topic.getUri(),domainUri));
            words.forEach(word -> udm.save(Relation.newMentionsFromTopic(topic.getUri(),word.getUri())));

        });

        List<Topic> partTopics = IntStream.range(0, NUM_PART_TOPICS).mapToObj(i -> Resource.newTopic()).collect(Collectors.toList());
        partTopics.forEach(topic -> {
            udm.save(topic);
            udm.save(Relation.newEmergesIn(topic.getUri(),domainUri));
            words.forEach(word -> udm.save(Relation.newMentionsFromTopic(topic.getUri(),word.getUri())));
        });


        // Documents
        List<Part> parts            = new ArrayList<>();
        List<Item> items            = new ArrayList<>();
        List<Document> documents    = IntStream.range(0, NUM_DOCUMENTS).mapToObj(i -> Resource.newDocument()).collect(Collectors.toList());
        documents.forEach(doc -> {
            udm.save(doc);
            udm.save(Relation.newProvides(source.getUri(),doc.getUri()));
            udm.save(Relation.newContains(domainUri,doc.getUri()));
            docTopics.forEach(topic -> udm.save(Relation.newDealsWithFromDocument(doc.getUri(),topic.getUri())));

            // Items
            List<Item> internalItems    = IntStream.range(0, NUM_ITEMS).mapToObj(i -> Resource.newItem()).collect(Collectors.toList());
            internalItems.forEach(item -> {
                udm.save(item);
                udm.save(Relation.newBundles(doc.getUri(),item.getUri()));
                itemTopics.forEach(topic -> udm.save(Relation.newDealsWithFromItem(item.getUri(),topic.getUri())));

                // Parts
                List<Part> internalParts    = IntStream.range(0, NUM_PARTS).mapToObj(i -> Resource.newPart()).collect(Collectors.toList());
                internalParts.forEach(part -> {
                    udm.save(part);
                    udm.save(Relation.newDescribes(part.getUri(),item.getUri()));
                    partTopics.forEach(topic -> udm.save(Relation.newDealsWithFromPart(part.getUri(),topic.getUri())));
                });
                parts.addAll(internalParts);

            });
            items.addAll(internalItems);
        });


        // -> similar_to document
        documents.forEach(d1 -> {
            documents.forEach(d2 -> {
                udm.save(Relation.newSimilarToDocuments(d1.getUri(),d2.getUri()));
            });
        });


        // -> similar_to item
        items.forEach(i1 ->{
            items.forEach(i2 -> {
                udm.save(Relation.newSimilarToItems(i1.getUri(),i2.getUri()));
            });
        });

        // -> similar_to item
        parts.forEach(p1 ->{
            parts.forEach(p2 -> {
                udm.save(Relation.newSimilarToParts(p1.getUri(),p2.getUri()));
            });
        });

        boolean finished = false;
        long created = 0l;
        while(!finished){
            long time = 5000;
            Thread.sleep(time);
            LOG.info("Checking step");
            //int numDocs = udm.find(Resource.Type.DOCUMENT).in(Resource.Type.DOMAIN, domainUri).size();
            //int res = udm.find(Relation.Type.SIMILAR_TO_PARTS).in(Resource.Type.DOMAIN, domainUri).size();
            long res = udm.count(Relation.Type.SIMILAR_TO_PARTS).in(Resource.Type.DOMAIN, domainUri);
            double ratio = Double.valueOf(res-created)/Double.valueOf(time/1000);
            created = res;
            LOG.info("At this time: " + res + " similar parts [" + numSimilarPart + "] : " + ratio + "rows/sec");
            finished = res >= numSimilarPart;
        }


        Period period = new Interval(start, System.currentTimeMillis()).toPeriod();
        System.out.println("Time inserting data: " + period.getMinutes() + " minutes, " + period.getSeconds() + " seconds");

        evaluate();

        LOG.info("Deleting Topics...");
        start = System.currentTimeMillis();
        udm.find(Relation.Type.EMERGES_IN).in(Resource.Type.DOMAIN,domainUri).forEach(emerges -> udm.delete(Resource.Type.TOPIC).byUri(emerges.getStartUri()));
        period = new Interval(start, System.currentTimeMillis()).toPeriod();
        LOG.info("Deleted Topics in: " + period.getMinutes() + " minutes, " + period.getSeconds() + " seconds");
        numTopics           = 0;
        numDealsWithDoc     = 0;
        numDealsWithItem    = 0;
        numDealsWithPart    = 0;
        numEmergesIn        = 0;
        numMentionTopic     = 0;
        evaluate();

        System.out.println("Delete PAIRS_WITH");
        udm.delete(Relation.Type.PAIRS_WITH).in(Resource.Type.DOMAIN,domainUri);
        numPairs            = 0;
        evaluate();

        System.out.println("Delete SIMILAR_TO DOCUMENT");
        udm.delete(Relation.Type.SIMILAR_TO_DOCUMENTS).in(Resource.Type.DOMAIN,domainUri);
        numSimilarDoc       = 0;
        evaluate();


        System.out.println("Delete SIMILAR_TO ITEMS");
        udm.delete(Relation.Type.SIMILAR_TO_ITEMS).in(Resource.Type.DOMAIN,domainUri);
        numSimilarItem      = 0;
        evaluate();

        System.out.println("Delete SIMILAR_TO PARTS");
        udm.delete(Relation.Type.SIMILAR_TO_PARTS).in(Resource.Type.DOMAIN,domainUri);
        numSimilarPart      = 0;
        evaluate();

        System.out.println("Delete Terms");
        start = System.currentTimeMillis();
        udm.find(Relation.Type.APPEARED_IN).in(Resource.Type.DOMAIN,domainUri).forEach(appearedIn -> udm.delete(Resource.Type.TERM).byUri(appearedIn.getStartUri()));
        period = new Interval(start, System.currentTimeMillis()).toPeriod();
        LOG.info("Deleted Terms in: " + period.getMinutes() + " minutes, " + period.getSeconds() + " seconds");
        numTerms            = 0;
        numHypernyms        = 0;
        numMentionTerm      = 0;
        numAppearedIn       = 0;
        evaluate();

        System.out.println("Delete Words");
        start = System.currentTimeMillis();
        udm.find(Relation.Type.EMBEDDED_IN).in(Resource.Type.DOMAIN,domainUri).forEach(embeddedIn -> udm.delete(Resource.Type.WORD).byUri(embeddedIn.getStartUri()));
        period = new Interval(start, System.currentTimeMillis()).toPeriod();
        LOG.info("Deleted Words in: " + period.getMinutes() + " minutes, " + period.getSeconds() + " seconds");
        numWords            = 0;
        numEmbedded         = 0;
        evaluate();

    }


    private void evaluate(){
        LOG.info("Evaluating Num Docs...");
        Assert.assertEquals(numDocs,            udm.count(Relation.Type.CONTAINS).in(Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Words...");
        Assert.assertEquals(numWords,           udm.count(Relation.Type.EMBEDDED_IN).in(Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Terms...");
        Assert.assertEquals(numTerms,           udm.count(Relation.Type.APPEARED_IN).in(Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Items...");
        Assert.assertEquals(numItems,           udm.count(Relation.Type.BUNDLES).in(Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Parts...");
        Assert.assertEquals(numParts,           udm.count(Relation.Type.DESCRIBES).in(Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Topics...");
        Assert.assertEquals(numTopics,          udm.count(Relation.Type.EMERGES_IN).in(Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Pairs...");
        Assert.assertEquals(numPairs,           udm.count(Relation.Type.PAIRS_WITH).in(Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Hypernyms...");
        Assert.assertEquals(numHypernyms,       udm.count(Relation.Type.HYPERNYM_OF).in(Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Mentions from Terms...");
        Assert.assertEquals(numMentionTerm,     udm.count(Relation.Type.MENTIONS_FROM_TERM).in(Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Mentions from Topics...");
        Assert.assertEquals(numMentionTopic,    udm.count(Relation.Type.MENTIONS_FROM_TOPIC).in(Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Deals With Doc...");
        Assert.assertEquals(numDealsWithDoc,    udm.count(Relation.Type.DEALS_WITH_FROM_DOCUMENT).in(Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Deals With Item...");
        Assert.assertEquals(numDealsWithItem,   udm.count(Relation.Type.DEALS_WITH_FROM_ITEM).in(Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Deals With Parts...");
        Assert.assertEquals(numDealsWithPart,   udm.count(Relation.Type.DEALS_WITH_FROM_PART).in(Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Similar Docs...");
        Assert.assertEquals(numSimilarDoc,      udm.count(Relation.Type.SIMILAR_TO_DOCUMENTS).in(Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Similar Items...");
        Assert.assertEquals(numSimilarItem,     udm.count(Relation.Type.SIMILAR_TO_ITEMS).in(Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Similar Parts...");
        Assert.assertEquals(numSimilarPart,     udm.count(Relation.Type.SIMILAR_TO_PARTS).in(Resource.Type.DOMAIN,domainUri));
    }

}
