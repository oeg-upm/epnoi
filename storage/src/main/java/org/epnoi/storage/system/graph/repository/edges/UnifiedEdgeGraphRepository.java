package org.epnoi.storage.system.graph.repository.edges;

import org.apache.commons.lang.WordUtils;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.utils.ResourceUtils;
import org.epnoi.storage.actions.RepeatableActionExecutor;
import org.epnoi.storage.system.Repository;
import org.epnoi.storage.system.graph.domain.edges.Edge;
import org.epnoi.storage.system.graph.domain.nodes.Node;
import org.epnoi.storage.system.graph.repository.nodes.UnifiedNodeGraphRepository;
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
public class UnifiedEdgeGraphRepository extends RepeatableActionExecutor implements Repository<Relation,Relation.Type>  {

    @Autowired
    UnifiedNodeGraphRepository nodeRepository;

    @Autowired
    UnifiedEdgeGraphRepositoryFactory factory;

    private static final Logger LOG = LoggerFactory.getLogger(UnifiedEdgeGraphRepository.class);

    @Override
    public void save(Relation relation){

        Optional<Resource> sn = nodeRepository.read(relation.getStartType(), relation.getStartUri());
        if (!sn.isPresent()) throw new RuntimeException("Start Node with uri: " + relation.getStartUri() + " not found");

        Optional<Resource> en = nodeRepository.read(relation.getEndType(), relation.getEndUri());
        if (!en.isPresent()) throw new RuntimeException("End Node with uri: " + relation.getStartUri() + " not found");


        // Build the edge between nodes
        Edge edge = (Edge) ResourceUtils.map(relation, factory.mappingOf(relation.getType()));
        edge.setStart((Node) sn.get());
        edge.setEndNode((Node) en.get());

        performRetries(0, "save " + relation.getType() + "[" + relation + "]", () -> factory.repositoryOf(relation.getType()).save(edge));
    }

    @Override
    public Boolean exists(Relation.Type type, String uri) {
        Optional<Object> result = performRetries(0, "exists " + type + "[" + uri + "]", () ->
                factory.repositoryOf(type).findOneByUri(uri) != null);
        return (result.isPresent())? (Boolean) result.get() : Boolean.FALSE;
    }

    @Override
    public Optional<Relation> read(Relation.Type type, String uri) {
        Optional<Object> result = performRetries(0, "read " + type + "[" + uri + "]", () -> {
            Optional<Relation> relation = Optional.empty();
            Edge edge = (Edge) factory.repositoryOf(type).findOneByUri(uri);
            if (edge != null) relation = Optional.of((Relation) ResourceUtils.map(edge, factory.mappingOf(type)));
            return relation;
        });
        return (result.isPresent())? (Optional<Relation>) result.get() : Optional.empty();
    }

    @Override
    public Iterable<Relation> findAll(Relation.Type type) {
        Optional<Object> result = performRetries(0, "findAll " + type, () ->
                factory.repositoryOf(type).findAll());
        return (result.isPresent())? (Iterable<Relation>) result.get() : Collections.EMPTY_LIST;
    }

    @Override
    public Iterable<Relation> findBy(Relation.Type result,String field, String value) {
        return find("findBy",result,value,field);
    }

    @Override
    public Iterable<Relation> findIn(Relation.Type result,Resource.Type referenceType,String referenceURI) {
        return find("findBy",result,referenceURI,referenceType.key());
    }

    private Iterable<Relation> find(String prefix, Relation.Type resultType,String uri,String reference) {
        Optional<Object> result = performRetries(0, prefix + " " + resultType + "[" + uri + "] and ref: " + reference, () -> {
            RelationGraphRepository repository = factory.repositoryOf(resultType);
            String methodName = prefix + WordUtils.capitalize(reference.toLowerCase());
            Method method = repository.getClass().getMethod(methodName, String.class);
            Iterable<Relation> resources = (Iterable<Relation>) method.invoke(repository, uri);
            return resources;
        });
        return (result.isPresent())? (Iterable<Relation>) result.get() : Collections.EMPTY_LIST;
    }

    @Override
    public void deleteAll(Relation.Type type) {
        performRetries(0,"delete all "+ type, () -> {
            factory.repositoryOf(type).deleteAll();
            return 1;
        });
    }

    @Override
    public void delete(Relation.Type type, String uri) {
        performRetries(0,"delete " + type + "["+uri+"]", () -> {
            Relation relation = factory.repositoryOf(type).findOneByUri(uri);
            if (relation != null) factory.repositoryOf(type).delete(factory.mappingOf(type).cast(relation) );
            return 1;
        });
    }
}
