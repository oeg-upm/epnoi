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

    @Autowired
    UDM udm;

    @Test
    public void deleteAll() throws InterruptedException {
        udm.delete(Resource.Type.ANY).all();
    }

    @Test
    public void transitiveRelationships() throws InterruptedException {



        udm.delete(Resource.Type.ANY).all();

        int NUM_WORDS           = 100;
        int NUM_TERMS           = 100;
        int NUM_DOC_TOPICS      = 5;
        int NUM_ITEM_TOPICS     = 6;
        int NUM_PART_TOPICS     = 11;
        int NUM_DOCUMENTS       = 100;
        int NUM_ITEMS           = 1;
        int NUM_PARTS           = 8;



        Long start = System.currentTimeMillis();

        // Source
        Source source = Resource.newSource();
        udm.save(source);

        // Domain
        Domain domain = Resource.newDomain();
        udm.save(domain);
        udm.save(Relation.newComposes(source.getUri(),domain.getUri()));

        // Words
        List<Word> words = IntStream.range(0, NUM_WORDS).mapToObj(i -> Resource.newWord()).collect(Collectors.toList());
        words.forEach(word -> {
            udm.save(word);
            udm.save(Relation.newEmbeddedIn(word.getUri(),domain.getUri()));
        });


        // Terms
        List<Term> terms = IntStream.range(0, NUM_TERMS).mapToObj(i -> Resource.newTerm()).collect(Collectors.toList());
        terms.forEach(term -> {
            udm.save(term);
            udm.save(Relation.newAppearedIn(term.getUri(),domain.getUri()));
            words.forEach(word -> udm.save(Relation.newMentionsFromTerm(term.getUri(),word.getUri())));
        });

        // hypernym_of
        terms.forEach(t1 ->{
            terms.forEach(t2 ->{
                udm.save(Relation.newHypernymOf(t1.getUri(),t2.getUri()));
            });
        });

        // Topics
        List<Topic> docTopics = IntStream.range(0, NUM_DOC_TOPICS).mapToObj(i -> Resource.newTopic()).collect(Collectors.toList());
        docTopics.forEach(topic -> {
            udm.save(topic);
            udm.save(Relation.newEmergesIn(topic.getUri(),domain.getUri()));
            words.forEach(word -> udm.save(Relation.newMentionsFromTopic(topic.getUri(),word.getUri())));

        });

        List<Topic> itemTopics = IntStream.range(0, NUM_ITEM_TOPICS).mapToObj(i -> Resource.newTopic()).collect(Collectors.toList());
        itemTopics.forEach(topic -> {
            udm.save(topic);
            udm.save(Relation.newEmergesIn(topic.getUri(),domain.getUri()));
            words.forEach(word -> udm.save(Relation.newMentionsFromTopic(topic.getUri(),word.getUri())));

        });

        List<Topic> partTopics = IntStream.range(0, NUM_PART_TOPICS).mapToObj(i -> Resource.newTopic()).collect(Collectors.toList());
        partTopics.forEach(topic -> {
            udm.save(topic);
            udm.save(Relation.newEmergesIn(topic.getUri(),domain.getUri()));
            words.forEach(word -> udm.save(Relation.newMentionsFromTopic(topic.getUri(),word.getUri())));
        });


        // Documents
        List<Part> parts            = new ArrayList<>();
        List<Item> items            = new ArrayList<>();
        List<Document> documents    = IntStream.range(0, NUM_DOCUMENTS).mapToObj(i -> Resource.newDocument()).collect(Collectors.toList());
        documents.forEach(doc -> {
            udm.save(doc);
            udm.save(Relation.newProvides(source.getUri(),doc.getUri()));
            udm.save(Relation.newContains(domain.getUri(),doc.getUri()));
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

        Period period = new Interval(start, System.currentTimeMillis()).toPeriod();
        System.out.println("Time inserting data: " + period.getMinutes() + " minutes, " + period.getSeconds() + " seconds");


        Assert.assertEquals(NUM_DOCUMENTS, udm.find(Resource.Type.DOCUMENT).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_WORDS, udm.find(Resource.Type.WORD).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_TERMS, udm.find(Resource.Type.TERM).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_DOCUMENTS*NUM_ITEMS, udm.find(Resource.Type.ITEM).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_DOCUMENTS*NUM_ITEMS*NUM_PARTS, udm.find(Resource.Type.PART).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_DOC_TOPICS+NUM_ITEM_TOPICS+NUM_PART_TOPICS, udm.find(Resource.Type.TOPIC).in(Resource.Type.DOMAIN,domain.getUri()).size());

        System.out.println("Delete Topics");
        udm.find(Resource.Type.TOPIC).in(Resource.Type.DOMAIN,domain.getUri()).stream().forEach(topic -> udm.delete(Resource.Type.TOPIC).byUri(topic));

        Assert.assertEquals(NUM_DOCUMENTS, udm.find(Resource.Type.DOCUMENT).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_TERMS, udm.find(Resource.Type.TERM).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_WORDS, udm.find(Resource.Type.WORD).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_DOCUMENTS*NUM_ITEMS, udm.find(Resource.Type.ITEM).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_DOCUMENTS*NUM_ITEMS*NUM_PARTS, udm.find(Resource.Type.PART).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(0, udm.find(Resource.Type.TOPIC).in(Resource.Type.DOMAIN,domain.getUri()).size());

        System.out.println("Delete PAIR_WITH");
        udm.delete(Relation.Type.PAIRS_WITH).in(Resource.Type.DOMAIN,domain.getUri());

        Assert.assertEquals(NUM_DOCUMENTS, udm.find(Resource.Type.DOCUMENT).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_TERMS, udm.find(Resource.Type.TERM).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_WORDS, udm.find(Resource.Type.WORD).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_DOCUMENTS*NUM_ITEMS, udm.find(Resource.Type.ITEM).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_DOCUMENTS*NUM_ITEMS*NUM_PARTS, udm.find(Resource.Type.PART).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(0, udm.find(Resource.Type.TOPIC).in(Resource.Type.DOMAIN,domain.getUri()).size());

        System.out.println("Delete SIMILAR_TO DOCUMENT");
        udm.delete(Relation.Type.SIMILAR_TO_DOCUMENTS).in(Resource.Type.DOMAIN,domain.getUri());

        Assert.assertEquals(NUM_DOCUMENTS, udm.find(Resource.Type.DOCUMENT).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_TERMS, udm.find(Resource.Type.TERM).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_WORDS, udm.find(Resource.Type.WORD).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_DOCUMENTS*NUM_ITEMS, udm.find(Resource.Type.ITEM).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_DOCUMENTS*NUM_ITEMS*NUM_PARTS, udm.find(Resource.Type.PART).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(0, udm.find(Resource.Type.TOPIC).in(Resource.Type.DOMAIN,domain.getUri()).size());

        System.out.println("Delete SIMILAR_TO ITEMS");
        udm.delete(Relation.Type.SIMILAR_TO_ITEMS).in(Resource.Type.DOMAIN,domain.getUri());

        Assert.assertEquals(NUM_DOCUMENTS, udm.find(Resource.Type.DOCUMENT).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_TERMS, udm.find(Resource.Type.TERM).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_WORDS, udm.find(Resource.Type.WORD).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_DOCUMENTS*NUM_ITEMS, udm.find(Resource.Type.ITEM).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_DOCUMENTS*NUM_ITEMS*NUM_PARTS, udm.find(Resource.Type.PART).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(0, udm.find(Resource.Type.TOPIC).in(Resource.Type.DOMAIN,domain.getUri()).size());

        System.out.println("Delete SIMILAR_TO PARTS");
        udm.delete(Relation.Type.SIMILAR_TO_PARTS).in(Resource.Type.DOMAIN,domain.getUri());

        Assert.assertEquals(NUM_DOCUMENTS, udm.find(Resource.Type.DOCUMENT).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_TERMS, udm.find(Resource.Type.TERM).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_WORDS, udm.find(Resource.Type.WORD).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_DOCUMENTS*NUM_ITEMS, udm.find(Resource.Type.ITEM).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(NUM_DOCUMENTS*NUM_ITEMS*NUM_PARTS, udm.find(Resource.Type.PART).in(Resource.Type.DOMAIN,domain.getUri()).size());
        Assert.assertEquals(0, udm.find(Resource.Type.TOPIC).in(Resource.Type.DOMAIN,domain.getUri()).size());




    }

}
