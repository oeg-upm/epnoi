package org.epnoi.storage.system.graph.repository;

import org.apache.commons.lang.WordUtils;
import org.epnoi.model.commons.StringUtils;
import org.epnoi.model.domain.Relation;
import org.epnoi.model.domain.Resource;
import org.epnoi.model.domain.ResourceUtils;
import org.epnoi.storage.system.Repository;
import org.epnoi.storage.system.graph.domain.Node;
import org.epnoi.storage.system.graph.domain.relationships.*;
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
public class GraphRepository implements Repository{

    @Autowired
    GraphRepositoryFactory factory;

    private static final Logger LOG = LoggerFactory.getLogger(GraphRepository.class);

    public void save(Resource resource, Resource.Type type){
        try{
            factory.repositoryOf(type).save(ResourceUtils.map(resource, factory.mappingOf(type)));
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }

    @Override
    public Boolean exists(String uri, Resource.Type type) {
        try{
            return factory.repositoryOf(type).findOneByUri(uri) != null;
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
        return Boolean.FALSE;
    }

    @Override
    public Optional<Resource> read(String uri, Resource.Type type) {
        Optional<Resource> result = Optional.empty();
        try{
            Node node = (Node) factory.repositoryOf(type).findOneByUri(uri);
            if (node != null) result = Optional.of((Resource) ResourceUtils.map(node, type.classOf()));
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

    public Iterable<Resource> findBy(Resource.Type result,String field, String value) {
        return find("findBy",result,value,field);
    }

    public Iterable<Resource> findIn(Resource.Type result,Resource.Type referenceType,String referenceURI) {
        return find("findBy",result,referenceURI,referenceType.key());
    }

    private Iterable<Resource> find(String prefix, Resource.Type result,String uri,String reference) {
        try{
            BaseGraphRepository repository = factory.repositoryOf(result);

            String methodName = prefix+WordUtils.capitalize(reference.toLowerCase());
            Method method = repository.getClass().getMethod(methodName, String.class);
            Iterable<Resource> resources = (Iterable<Resource>) method.invoke(repository, uri);
            return resources;
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOG.warn("No such method to find: " + e.getMessage());
        }
        return Collections.EMPTY_LIST;
    }


    public void delete(String uri, Resource.Type type){
        try{
            Resource resource = factory.repositoryOf(type).findOneByUri(uri);
            if (resource != null) factory.repositoryOf(type).delete( factory.mappingOf(type).cast(resource) );
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

    public Resource relate(String uri1, String uri2, Relation.Type type, Relation.Properties properties){

        Node startingNode   = (Node) factory.repositoryOf(type.getStart()).findOneByUri(uri1);
        Node endingNode     = (Node) factory.repositoryOf(type.getEnd()).findOneByUri(uri2);

        Relation relation = relationOf(type);
        relation.setStart(startingNode);
        relation.setEnd(endingNode);
        relation.setProperties(properties);

        startingNode.add(relation,type);
        LOG.debug("Saving relation: " + relation);

        Node node = (Node) factory.repositoryOf(type.getStart()).save(startingNode);
        LOG.info("Related: "+type.getStart().name()+"[" + uri1 + "] to "+type.getEnd().name()+"[" + uri2 + "] -> id:"+node.getId()+" with: " + properties);
        return node;
    }

    public Resource unrelate(String uri1, String uri2, Relation.Type type){

        Node startingNode   = (Node) factory.repositoryOf(type.getStart()).findOneByUri(uri1);
        Node endingNode     = (Node) factory.repositoryOf(type.getEnd()).findOneByUri(uri2);

        Relation relation = relationOf(type);
        relation.setStart(startingNode);
        relation.setEnd(endingNode);

        startingNode.remove(relation,type);
        LOG.debug("Removing relation: " + type);

        Node node = (Node) factory.repositoryOf(type.getStart()).save(startingNode);
        LOG.info("Unrelated: "+type.getStart().name()+"[" + uri1 + "] and "+type.getEnd().name()+"[" + uri2 + "] -> id:"+node.getId());
        return node;
    }

    private Relation relationOf(Relation.Type type){
        switch (type){
            case DOCUMENT_BUNDLES_ITEM: return new ItemBundledByDocument();
            case DOCUMENT_DEALS_WITH_TOPIC: return new TopicDealtByDocument();
            case DOCUMENT_SIMILAR_TO_DOCUMENT: return new SimilarDocument();
            case DOMAIN_CONTAINS_DOCUMENT: return new ContainedDocument();
            case ITEM_DEALS_WITH_TOPIC: return new TopicDealtByItem();
            case ITEM_SIMILAR_TO_ITEM: return new SimilarItem();
            case PART_DEALS_WITH_TOPIC: return new TopicDealtByPart();
            case PART_DESCRIBES_ITEM: return new ItemDescribedByPart();
            case PART_SIMILAR_TO_PART: return new SimilarPart();
            case SOURCE_COMPOSES_DOMAIN: return new DomainComposedBySource();
            case SOURCE_PROVIDES_DOCUMENT: return new DocumentProvidedBySource();
            case TOPIC_EMERGES_IN_DOMAIN: return new DomainInTopic();
            case TOPIC_MENTIONS_WORD: return new WordMentionedByTopic();
            case WORD_EMBEDDED_IN_DOMAIN: return new DomainInWord();
            case WORD_PAIRS_WITH_WORD: return new PairedWord();
            default: throw new RuntimeException("No relation found for type: " + type);
        }

    }



}
