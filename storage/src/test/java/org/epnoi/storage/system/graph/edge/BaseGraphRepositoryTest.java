package org.epnoi.storage.system.graph.edge;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.system.graph.GraphConfig;
import org.epnoi.storage.system.graph.domain.edges.Edge;
import org.epnoi.storage.system.graph.repository.edges.RelationGraphRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by cbadenes on 21/12/15.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GraphConfig.class)
@TestPropertySource(properties = { "epnoi.eventbus.host = local","epnoi.neo4j.contactpoints = drinventor.dia.fi.upm.es", "epnoi.neo4j.port = 5030" })
public abstract class BaseGraphRepositoryTest<T extends Edge> {

    public abstract RelationGraphRepository<T> getRepository();

    public abstract T getEntity();

    public abstract void setup();

    public abstract void shutdown();

    @Before
    public void prepare(){
        setup();
    }

    @After
    public void destroy(){
        shutdown();
    }

    @Test
    public void crud(){
        long count1 = getRepository().count();

        T entity = getEntity();
        T column = getRepository().save(entity);
        Assert.assertEquals(entity, column);

        long count2 = getRepository().count();
        Assert.assertEquals(count1 + 1l, count2);

        getRepository().delete(entity);

        long count3 = getRepository().count();
        Assert.assertEquals(count1, count3);
    }


    @Test
    public void findOne(){
        T entity = getEntity();

        T found = getRepository().findOneByUri(entity.getUri());
        Assert.assertNull(found);

        getRepository().save(entity);

        found = getRepository().findOneByUri(entity.getUri());
        Assert.assertNotNull(found);

        getRepository().delete(found);
    }


}
