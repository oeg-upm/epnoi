package org.epnoi.storage.system.graph;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.domain.*;
import org.epnoi.storage.system.graph.repository.DomainGraphRepository;
import org.epnoi.storage.system.graph.repository.GraphRepository;
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
@ContextConfiguration(classes = GraphConfig.class)
@TestPropertySource(properties = {
        "epnoi.neo4j.contactpoints = drinventor.dia.fi.upm.es",
        "epnoi.neo4j.port = 5030" })
public class UnifiedGraphRelationalRepositoryTest {

    private static final Logger LOG = LoggerFactory.getLogger(UnifiedGraphRelationalRepositoryTest.class);

    @Autowired
    GraphRepository repository;

    @Autowired
    DomainGraphRepository domainGraphRepository;

    @Test
    public void sourceProvidesDocument(){

        repository.deleteAll(Resource.Type.SOURCE);
        repository.deleteAll(Resource.Type.DOCUMENT);

        Source source = new Source();
        source.setUri("sources/01");
        repository.save(source,Resource.Type.SOURCE);

        Document document = new Document();
        document.setUri("documents/01");
        repository.save(document,Resource.Type.DOCUMENT);

        Relation.Properties properties = new Relation.Properties();
        properties.setDate("2017");
        repository.relate(source.getUri(),document.getUri(),Relation.Type.SOURCE_PROVIDES_DOCUMENT,properties);

        repository.unrelate(source.getUri(),document.getUri(),Relation.Type.SOURCE_PROVIDES_DOCUMENT);


    }


}
