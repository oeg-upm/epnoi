package org.epnoi.storage.system.graph.edge;

import com.google.common.collect.Lists;
import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.domain.relations.AppearedIn;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Document;
import org.epnoi.model.domain.resources.Domain;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.domain.resources.Term;
import org.epnoi.storage.Config;
import org.epnoi.storage.UDM;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cbadenes on 22/12/15.
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
public class RelationTest {

    @Autowired
    UDM udm;

    @Test
    public void insertAppearedIn(){

        Domain domain = Resource.newDomain();
        udm.save(domain);

        List<String> res = udm.find(Resource.Type.TERM).in(Resource.Type.DOMAIN, domain.getUri());
        Assert.assertTrue(res.isEmpty());

        Term term = Resource.newTerm();
        udm.save(term);

        AppearedIn appearedIn = Relation.newAppearedIn(term.getUri(), domain.getUri());
        udm.save(appearedIn);

        res = udm.find(Resource.Type.TERM).in(Resource.Type.DOMAIN, domain.getUri());
        Assert.assertFalse(res.isEmpty());
        Assert.assertEquals(1,res.size());
        Assert.assertEquals(term.getUri(),res.get(0));

        Iterable<Relation> rel = udm.find(Relation.Type.APPEARED_IN).in(Resource.Type.DOMAIN, domain.getUri());
        Assert.assertFalse(rel == null);

        List<Relation> rels = Lists.newArrayList(rel);
        Assert.assertEquals(1,rels.size());
        Assert.assertEquals(appearedIn,rels.get(0));

        udm.delete(Relation.Type.APPEARED_IN).byUri(appearedIn.getUri());

        res = udm.find(Resource.Type.TERM).in(Resource.Type.DOMAIN, domain.getUri());
        Assert.assertTrue(res.isEmpty());

    }


    @Test
    public void insertSimilarTo(){

        Domain domain = Resource.newDomain();
        udm.save(domain);

        Document doc1 = Resource.newDocument();
        udm.save(doc1);
        udm.save(Relation.newContains(domain.getUri(),doc1.getUri()));

        Document doc2 = Resource.newDocument();
        udm.save(doc2);
        udm.save(Relation.newContains(domain.getUri(),doc2.getUri()));

        Document doc3 = Resource.newDocument();
        udm.save(doc3);
        udm.save(Relation.newContains(domain.getUri(),doc3.getUri()));


        List<Relation> rels = udm.find(Relation.Type.SIMILAR_TO_DOCUMENTS).in(Resource.Type.DOMAIN, domain.getUri());
        Assert.assertTrue(rels.isEmpty());

        udm.save(Relation.newSimilarToDocuments(doc1.getUri(),doc2.getUri()));
        rels = udm.find(Relation.Type.SIMILAR_TO_DOCUMENTS).in(Resource.Type.DOMAIN, domain.getUri());
        Assert.assertFalse(rels.isEmpty());
        Assert.assertEquals(1,rels.size());

        Assert.assertEquals(doc1.getUri(), rels.get(0).getStartUri());
        Assert.assertEquals(doc2.getUri(), rels.get(0).getEndUri());

        udm.delete(Relation.Type.SIMILAR_TO_DOCUMENTS).in(Resource.Type.DOMAIN,domain.getUri());
        rels = udm.find(Relation.Type.SIMILAR_TO_DOCUMENTS).in(Resource.Type.DOMAIN, domain.getUri());
        Assert.assertTrue(rels.isEmpty());
    }

}
