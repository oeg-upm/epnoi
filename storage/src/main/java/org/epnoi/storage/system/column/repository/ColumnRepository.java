package org.epnoi.storage.system.column.repository;

import org.epnoi.model.domain.Resource;
import org.epnoi.storage.system.Repository;
import org.epnoi.model.domain.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.repository.support.BasicMapId;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

/**
 * Created by cbadenes on 02/02/16.
 */
@Component
public class ColumnRepository implements Repository{

    @Autowired
    ColumnRepositoryFactory factory;

    private static final Logger LOG = LoggerFactory.getLogger(ColumnRepository.class);

    public void save(Resource resource, Resource.Type type){
        try{
            factory.repositoryOf(type).save(ResourceUtils.map(resource, factory.mappingOf(type)));
            LOG.debug("Resource: " + resource + " saved");
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }

    public Boolean exists(String uri, Resource.Type type){

        try{
            return factory.repositoryOf(type).exists(BasicMapId.id(ResourceUtils.URI, uri));
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
        return false;
    }

    public Optional<Resource> read(String uri, Resource.Type type){
        Optional<Resource> result = Optional.empty();
        try{
            Resource column = (Resource) factory.repositoryOf(type).findOne(BasicMapId.id(ResourceUtils.URI, uri));
            if (column != null) result = Optional.of((Resource) ResourceUtils.map(column, type.classOf()));
            LOG.debug("Resource read: " + column );
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
        return result;
    }

    public Iterable<Resource> findAll(Resource.Type type){
        try{
            return factory.repositoryOf(type).findAll();
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
            return Collections.EMPTY_LIST;
        }
    }

    public void delete(String uri, Resource.Type type){
        try{
            factory.repositoryOf(type).delete(BasicMapId.id(ResourceUtils.URI, uri));
            LOG.debug("Resource: " + uri + " deleted");
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }

    public void deleteAll(Resource.Type type){
        try{
            factory.repositoryOf(type).deleteAll();
            LOG.debug(type.name() + "s deleted");
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }







}
