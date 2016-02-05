package org.epnoi.storage.system.graph.repository.edges;

import org.apache.commons.lang.WordUtils;
import org.epnoi.model.domain.*;
import org.epnoi.storage.system.Repository;
import org.epnoi.storage.system.graph.domain.edges.Edge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Optional;

/**
 * Created by cbadenes on 02/02/16.
 */
@Component
public class UnifiedEdgeGraphRepository implements Repository<Relation,Relation.Type>  {

    @Autowired
    UnifiedEdgeGraphRepositoryFactory factory;

    private static final Logger LOG = LoggerFactory.getLogger(UnifiedEdgeGraphRepository.class);

    @Override
    public void save(Relation relation, Relation.Type type){
        try{
            factory.repositoryOf(type).save(ResourceUtils.map(relation, factory.mappingOf(type)));
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }

    @Override
    public Boolean exists(Relation.Type type, String uri) {
        try{
            return factory.repositoryOf(type).findOneByUri(uri) != null;
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
        return Boolean.FALSE;
    }

    @Override
    public Optional<Relation> read(Relation.Type type, String uri) {
        Optional<Relation> result = Optional.empty();
        try{
            Edge edge = (Edge) factory.repositoryOf(type).findOneByUri(uri);
            if (edge != null) result = Optional.of((Relation) ResourceUtils.map(edge, factory.mappingOf(type)));
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
        return result;
    }

    @Override
    public Iterable<Relation> findAll(Relation.Type type) {
        try{
            return factory.repositoryOf(type).findAll();
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public Iterable<Relation> findBy(Relation.Type result,String field, String value) {
        return find("findBy",result,value,field);
    }

    @Override
    public Iterable<Relation> findIn(Relation.Type result,Resource.Type referenceType,String referenceURI) {
        return find("findBy",result,referenceURI,referenceType.key());
    }

    private Iterable<Relation> find(String prefix, Relation.Type result,String uri,String reference) {
        try{
            RelationGraphRepository repository = factory.repositoryOf(result);

            String methodName = prefix+WordUtils.capitalize(reference.toLowerCase());
            Method method = repository.getClass().getMethod(methodName, String.class);
            Iterable<Relation> resources = (Iterable<Relation>) method.invoke(repository, uri);
            return resources;
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOG.warn("No such method to find: " + e.getMessage());
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public void deleteAll(Relation.Type type) {
        try{
            factory.repositoryOf(type).deleteAll();
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }

    @Override
    public void delete(Relation.Type type, String uri) {
        try{
            Relation relation = factory.repositoryOf(type).findOneByUri(uri);
            if (relation != null) factory.repositoryOf(type).delete( factory.mappingOf(type).cast(relation) );
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }

    public void delete(Relation.Type type, String startUri, String endUri){
        try{
            Iterable<Relation> resource = factory.repositoryOf(type).findByNodes(startUri,endUri);
            if (resource != null){
                resource.forEach(relation ->factory.repositoryOf(type).delete( factory.mappingOf(type).cast(relation) ) );
            }
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }
}
