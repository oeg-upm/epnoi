package org.epnoi.storage.system.graph.repository.nodes;

import org.apache.commons.lang.WordUtils;
import org.epnoi.model.domain.Relation;
import org.epnoi.model.domain.RelationProperties;
import org.epnoi.model.domain.Resource;
import org.epnoi.model.domain.ResourceUtils;
import org.epnoi.storage.system.Repository;
import org.epnoi.storage.system.graph.domain.nodes.Node;
import org.epnoi.storage.actions.RepeatableActionExecutor;
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
    public void save(Resource resource, Resource.Type type){
        performRetries(0,"saving a " + type, () ->
                factory.repositoryOf(type).save(ResourceUtils.map(resource, factory.mappingOf(type))));
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
            Node node = (Node) factory.repositoryOf(type).findOneByUri(uri);
            if (node != null) resource = Optional.of((Resource) ResourceUtils.map(node, type.classOf()));
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
            Resource resource = factory.repositoryOf(type).findOneByUri(uri);
            if (resource != null) factory.repositoryOf(type).delete( factory.mappingOf(type).cast(resource) );
            return 1;
        });
    }


    public Resource attach(String uri1, String uri2, Relation.Type type, RelationProperties properties){
        Optional<Object> result = performRetries(0, "attach " + type + " btw [" + uri1 + "] and [" + uri2 + "]", () -> {
            Node startingNode = (Node)  factory.repositoryOf(type.getStart()).findOneByUri(uri1);
            Node endingNode = (Node) factory.repositoryOf(type.getEnd()).findOneByUri(uri2);

            Relation relation = factory.relationOf(type);
            relation.setStart(startingNode);
            relation.setEnd(endingNode);
            relation.setProperties(properties);

            startingNode.add(relation, type);
            LOG.debug("Saving relation: " + relation);

            Node node = (Node) factory.repositoryOf(type.getStart()).save(startingNode);
            LOG.info("Related: " + type.getStart().name() + "[" + uri1 + "] to " + type.getEnd().name() + "[" + uri2 + "] -> id:" + node.getId() + " with: " + properties);
            return node;
        });
        return (result.isPresent())? (Resource) result.get() : new Resource();
    }

    public Resource detach(String uri1, String uri2, Relation.Type type){
        Optional<Object> result = performRetries(0, "detach " + type + " btw [" + uri1 + "] and [" + uri2 + "]", () -> {
            Node startingNode = (Node) factory.repositoryOf(type.getStart()).findOneByUri(uri1);
            Node endingNode = (Node) factory.repositoryOf(type.getEnd()).findOneByUri(uri2);

            Relation relation = factory.relationOf(type);
            relation.setStart(startingNode);
            relation.setEnd(endingNode);

            startingNode.remove(relation, type);
            LOG.debug("Removing relation: " + type);

            Node node = (Node) factory.repositoryOf(type.getStart()).save(startingNode);
            LOG.info("Unrelated: " + type.getStart().name() + "[" + uri1 + "] and " + type.getEnd().name() + "[" + uri2 + "] -> id:" + node.getId());
            return node;
        });
        return (result.isPresent())? (Resource) result.get() : new Resource();
    }
}
