package org.epnoi.storage.system.graph.repository.nodes;

import org.apache.commons.lang.WordUtils;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.utils.ResourceUtils;
import org.epnoi.storage.actions.RepeatableActionExecutor;
import org.epnoi.storage.system.graph.repository.Repository;
import org.epnoi.storage.system.graph.domain.nodes.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Optional;

/**
 * Created by cbadenes on 02/02/16.
 */
@Component
public class UnifiedNodeGraphRepository extends RepeatableActionExecutor implements Repository<Resource,Resource.Type> {

    @Autowired
    UnifiedNodeGraphRepositoryFactory factory;

    private static final Logger LOG = LoggerFactory.getLogger(UnifiedNodeGraphRepository.class);


    @Override
    public void save(Resource resource){
        performRetries(0,"saving a " + resource.getResourceType(), () ->
                factory.repositoryOf(resource.getResourceType()).save(ResourceUtils.map(resource, factory.mappingOf(resource.getResourceType()))));
    }

    @Override
    public Boolean exists(Resource.Type type, String uri) {
        Optional<Object> result = performRetries(0, "exists " + type + "[" + uri + "]", () ->
                factory.repositoryOf(type).findOneByUri(uri) != null);
        return (result.isPresent())? (Boolean) result.get() : Boolean.FALSE;
    }

    @Override
    public Optional<Resource> read(Resource.Type type, String uri) {
        Optional<Object> result = performRetries(0, "read " + type + "[" + uri + "]", () -> {
            Optional<Resource> resource = Optional.empty();
            Resource node = factory.repositoryOf(type).findOneByUri(uri);
            if (node != null) resource = Optional.of((Resource) ResourceUtils.map(node, Resource.classOf(type)));
            return resource;
        });
        return (result.isPresent())? (Optional<Resource>) result.get() : Optional.empty();
    }

    @Override
    public Iterable<Resource> findAll(Resource.Type type) {
        Optional<Object> result = performRetries(0, "findAll " + type, () ->
                factory.repositoryOf(type).findAll());
        return (result.isPresent())? (Iterable<Resource>) result.get() : Collections.EMPTY_LIST;
    }

    @Override
    public Iterable<Resource> findBy(Resource.Type result,String field, String value) {
        return find("findBy",result,value,field);
    }

    @Override
    public Iterable<Resource> findIn(Resource.Type result,Resource.Type referenceType,String referenceURI) {
        return find("findBy",result,referenceURI,referenceType.key());
    }

    private Iterable<Resource> find(String prefix, Resource.Type resultType,String uri,String reference) {
        Optional<Object> result = performRetries(0, prefix + " " + resultType + "[" + uri + "] and ref: " + reference, () -> {
            ResourceGraphRepository repository = factory.repositoryOf(resultType);
            String methodName = prefix + WordUtils.capitalize(reference.toLowerCase());
            Method method = repository.getClass().getMethod(methodName, String.class);
            Iterable<Resource> resources = (Iterable<Resource>) method.invoke(repository, uri);
            return resources;
        });
        return (result.isPresent())? (Iterable<Resource>) result.get() : Collections.EMPTY_LIST;
    }

    @Override
    public void deleteAll(Resource.Type type) {
        performRetries(0, "delete all " + type, () -> {
            factory.repositoryOf(type).deleteAll();
            return 1;

        });
    }

    public void delete(Resource.Type type, String uri){
        performRetries(0,"delete " + type + "["+uri+"]", () -> {
            Node resource = factory.repositoryOf(type).findOneByUri(uri);
            //if (resource != null) factory.repositoryOf(type).delete( factory.mappingOf(type).cast(resource) );
            if (resource != null) factory.repositoryOf(type).delete( resource.getId());
            return 1;
        });
    }
}
