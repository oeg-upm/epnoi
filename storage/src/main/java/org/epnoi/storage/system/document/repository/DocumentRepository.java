package org.epnoi.storage.system.document.repository;

import org.epnoi.model.domain.Resource;
import org.epnoi.storage.system.Repository;
import org.epnoi.model.domain.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

/**
 * Created by cbadenes on 02/02/16.
 */
@Component
public class DocumentRepository implements Repository {

    @Autowired
    DocumentRepositoryFactory factory;

    private static final Logger LOG = LoggerFactory.getLogger(DocumentRepository.class);

    public Boolean exists(String uri, Resource.Type type){
        try{
            return factory.repositoryOf(type).exists(uri);
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<Resource> read(String uri, Resource.Type type) {
        Optional<Resource> result = Optional.empty();
        try{
            Resource document = (Resource) factory.repositoryOf(type).findOne(uri);
            if (document != null) result = Optional.of((Resource) ResourceUtils.map(document, type.classOf()));
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
        return result;
    }

    @Override
    public Iterable<Resource> findAll(Resource.Type type) {
        try{
            return factory.repositoryOf(type).findAll();
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
            return Collections.EMPTY_LIST;
        }
    }

    public void save(Resource resource, Resource.Type type){
        try{
            factory.repositoryOf(type).save(ResourceUtils.map(resource, factory.mappingOf(type)));
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }

    public void delete(String uri, Resource.Type type){
        try{
            factory.repositoryOf(type).delete(uri);
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }

    @Override
    public void deleteAll(Resource.Type type) {
        try{
            factory.repositoryOf(type).deleteAll();
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }


}
