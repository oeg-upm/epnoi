package org.epnoi.storage.system.graph.node;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.system.graph.GraphConfig;
import org.epnoi.storage.system.graph.domain.nodes.Node;
import org.epnoi.storage.system.graph.repository.nodes.ResourceGraphRepository;
import org.junit.Assert;
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
@TestPropertySource(properties = { "epnoi.neo4j.contactpoints = drinventor.dia.fi.upm.es", "epnoi.neo4j.port = 5030" })
public abstract class BaseGraphRepositoryTest<T extends Node> {

    public abstract ResourceGraphRepository<T> getRepository();

    public abstract T getEntity();

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
