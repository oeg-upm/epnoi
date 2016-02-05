package org.epnoi.storage.system.document;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.domain.*;
import org.epnoi.storage.system.document.repository.UnifiedDocumentRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

/**
 * Created by cbadenes on 03/02/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DocumentConfig.class)
@TestPropertySource(properties = {
        "epnoi.elasticsearch.contactpoints = drinventor.dia.fi.upm.es",
        "epnoi.elasticsearch.port = 5021" })
public class UnifiedUnifiedDocumentRepositoryTest {

    private static final Logger LOG = LoggerFactory.getLogger(UnifiedUnifiedDocumentRepositoryTest.class);

    @Autowired
    UnifiedDocumentRepository repository;


    @Test
    public void source(){

        Source source = new Source();
        source.setUri("sources/01");
        source.setName("test01");
        source.setDescription("testing purposes");
        test(source,Resource.Type.SOURCE);
    }

    @Test
    public void domain(){

        Domain domain = new Domain();
        domain.setUri("domains/01");
        domain.setName("test01");
        domain.setDescription("testing purposes");
        test(domain,Resource.Type.DOMAIN);
    }

    @Test
    public void document() {

        Document document = new Document();
        document.setUri("documents/01");
        document.setAuthoredBy("me");
        document.setAuthoredOn("20151210");
        document.setDescription("testing purposes");
        test(document, Resource.Type.DOCUMENT);
    }

    @Test
    public void item() {

        Item resource = new Item();
        resource.setUri("items/01");
        resource.setAuthoredBy("me");
        resource.setAuthoredOn("20151210");
        resource.setDescription("testing purposes");
        test(resource, Resource.Type.ITEM);
    }

    @Test
    public void part() {

        Part resource = new Part();
        resource.setUri("items/01");
        resource.setSense("nosense");
        resource.setContent("sampling");
        resource.setTokens("sample");
        test(resource, Resource.Type.PART);
    }

    @Test
    public void topic() {

        Topic resource = new Topic();
        resource.setUri("items/01");
        resource.setAnalysis("analyses/01");
        resource.setContent("sampling");
        test(resource, Resource.Type.TOPIC);
    }

    @Test
    public void word() {

        Word resource = new Word();
        resource.setUri("items/01");
        resource.setLemma("house");
        resource.setContent("house");
        resource.setPos("nn");
        test(resource, Resource.Type.WORD);
    }


    private void test(Resource resource, Resource.Type type){

        LOG.info("####################### " + type.name());

        repository.deleteAll(type);

        Assert.assertFalse(repository.exists(type,resource.getUri()));

        repository.save(resource, type);

        Assert.assertTrue(repository.exists(type,resource.getUri()));
        Optional<Resource> result = repository.read(type,resource.getUri());
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(resource,result.get());

        repository.delete(type,resource.getUri());

        Assert.assertFalse(repository.exists(type,resource.getUri()));
    }

}
