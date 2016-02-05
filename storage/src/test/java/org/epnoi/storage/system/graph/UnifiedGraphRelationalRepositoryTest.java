package org.epnoi.storage.system.graph;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.domain.*;
import org.epnoi.storage.system.graph.repository.nodes.UnifiedNodeGraphRepository;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
    UnifiedNodeGraphRepository unifiedNodeGraphRepository;

    @Test
    public void sourceProvidesDocument(){

        unifiedNodeGraphRepository.deleteAll(Resource.Type.SOURCE);
        unifiedNodeGraphRepository.deleteAll(Resource.Type.DOCUMENT);

        Source source = new Source();
        source.setUri("sources/01");
        unifiedNodeGraphRepository.save(source,Resource.Type.SOURCE);

        Document document = new Document();
        document.setUri("documents/01");
        unifiedNodeGraphRepository.save(document,Resource.Type.DOCUMENT);

        unifiedNodeGraphRepository.attach(source.getUri(),document.getUri(),Relation.Type.SOURCE_PROVIDES_DOCUMENT,RelationProperties.builder().date("2017").build());

        unifiedNodeGraphRepository.detach(source.getUri(),document.getUri(),Relation.Type.SOURCE_PROVIDES_DOCUMENT);


    }

    @Test
    public void findIn(){

        unifiedNodeGraphRepository.deleteAll(Resource.Type.SOURCE);
        unifiedNodeGraphRepository.deleteAll(Resource.Type.DOCUMENT);

        Source source = new Source();
        source.setUri("sources/01");
        unifiedNodeGraphRepository.save(source,Resource.Type.SOURCE);

        Document document = new Document();
        document.setUri("documents/01");
        unifiedNodeGraphRepository.save(document,Resource.Type.DOCUMENT);

        unifiedNodeGraphRepository.attach(source.getUri(),document.getUri(),Relation.Type.SOURCE_PROVIDES_DOCUMENT,RelationProperties.builder().date("2017").build());

        Iterable<Resource> result = unifiedNodeGraphRepository.findIn(Resource.Type.DOCUMENT,Resource.Type.SOURCE,source.getUri());
        System.out.println("Documents: " + result);

    }

}
