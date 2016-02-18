package org.epnoi.storage.system.graph.node;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.domain.relations.Provides;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Document;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.domain.resources.Source;
import org.epnoi.storage.system.graph.GraphConfig;
import org.epnoi.storage.system.graph.repository.edges.UnifiedEdgeGraphRepository;
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

    @Autowired
    UnifiedEdgeGraphRepository unifiedEdgeGraphRepository;

    @Test
    public void sourceProvidesDocument(){

        unifiedNodeGraphRepository.deleteAll(Resource.Type.SOURCE);
        unifiedNodeGraphRepository.deleteAll(Resource.Type.DOCUMENT);

        Source source = Resource.newSource();
        source.setUri("sources/01");
        unifiedNodeGraphRepository.save(source);

        Document document = Resource.newDocument();
        document.setUri("documents/01");
        unifiedNodeGraphRepository.save(document);


        Provides provides = Relation.newProvides(source.getUri(), document.getUri());
        provides.setUri("provision/1");
        unifiedEdgeGraphRepository.save(provides);

        unifiedEdgeGraphRepository.delete(Relation.Type.PROVIDES,provides.getUri());

    }

    @Test
    public void findIn(){

        unifiedNodeGraphRepository.deleteAll(Resource.Type.SOURCE);
        unifiedNodeGraphRepository.deleteAll(Resource.Type.DOCUMENT);

        Source source = Resource.newSource();
        source.setUri("sources/01");
        unifiedNodeGraphRepository.save(source);

        Document document = Resource.newDocument();
        document.setUri("documents/01");
        unifiedNodeGraphRepository.save(document);

        Provides provides = Relation.newProvides(source.getUri(), document.getUri());
        provides.setUri("provision/1");
        unifiedEdgeGraphRepository.save(provides);

        Iterable<Resource> result = unifiedNodeGraphRepository.findIn(Resource.Type.DOCUMENT,Resource.Type.SOURCE,source.getUri());
        System.out.println("Documents: " + result);

    }

}
