package org.epnoi.api.services;

import org.apache.commons.beanutils.BeanUtils;
import org.epnoi.api.model.relations.*;
import org.epnoi.api.model.resources.DocumentI;
import org.epnoi.model.domain.relations.DealsWithFromDocument;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.relations.SimilarToDocuments;
import org.epnoi.model.domain.resources.Document;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class DocumentService extends AbstractResourceService<Document> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentService.class);

    public DocumentService() {
        super(Resource.Type.DOCUMENT);
    }


    public Document create(DocumentI resource) throws Exception {
        LOG.info("Trying to create: " + resource);
        Document document = Resource.newDocument();
        BeanUtils.copyProperties(document,resource);

        document.setFormat("json");
        document.setRetrievedFrom("api");
        document.setRetrievedOn(TimeUtils.asISO());
        udm.save(document);
        return document;
    }

    // BUNDLES -> Item
    public List<String> listItems(String id){
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        return udm.find(Resource.Type.ITEM).in(Resource.Type.DOCUMENT, uri);
    }

    public void removeItems(String id){
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        udm.delete(Relation.Type.BUNDLES).in(Resource.Type.DOCUMENT,uri);
    }

    public RelationI getItems(String startId, String endId){
        String startUri     = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String endUri       = uriGenerator.from(Resource.Type.ITEM, endId);
        Optional<RelationI> result = udm.find(Relation.Type.BUNDLES).btw(startUri, endUri).stream().map(relation -> new RelationI(relation.getUri(), relation.getCreationTime())).findFirst();
        return (result.isPresent())? result.get() : new RelationI();
    }

    public void addItems(String startId, String endId){
        String startUri     = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String endUri       = uriGenerator.from(Resource.Type.ITEM, endId);
        udm.save(Relation.newBundles(startUri,endUri));
    }

    public void removeItems(String startId, String endId){
        String duri = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String iuri = uriGenerator.from(Resource.Type.ITEM, endId);
        udm.find(Relation.Type.BUNDLES).btw(startId, endId).forEach(relation -> udm.delete(Relation.Type.BUNDLES).byUri(relation.getUri()));
    }


    // SIMILAR_TO -> Documents
    public List<String> listDocuments(String id){
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        return udm.find(Resource.Type.DOCUMENT).in(Resource.Type.DOCUMENT, uri);
    }

    public void removeDocuments(String id){
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        udm.delete(Relation.Type.SIMILAR_TO_DOCUMENTS).in(Resource.Type.DOCUMENT,uri);
    }

    public SimilarI getDocuments(String startId, String endId){
        String startUri     = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String endUri       = uriGenerator.from(Resource.Type.DOCUMENT, endId);
        Optional<SimilarI> result = udm.find(Relation.Type.SIMILAR_TO_DOCUMENTS).btw(startUri, endUri).stream().map(relation -> new SimilarI(relation.getUri(), relation.getCreationTime(),relation.getWeight(), ((SimilarToDocuments)relation).getDomain())).findFirst();
        return (result.isPresent())? result.get() : new SimilarI();
    }

    public void addDocuments(String startId, String endId, WeightDomainI rel){
        String startUri     = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String endUri       = uriGenerator.from(Resource.Type.DOCUMENT, endId);
        SimilarToDocuments relation = Relation.newSimilarToDocuments(startUri, endUri);
        relation.setDomain(rel.getDomain());
        relation.setWeight(rel.getWeight());
        udm.save(relation);
    }

    public void removeDocuments(String startId, String endId){
        String duri = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String iuri = uriGenerator.from(Resource.Type.DOCUMENT, endId);
        udm.find(Relation.Type.SIMILAR_TO_DOCUMENTS).btw(startId, endId).forEach(relation -> udm.delete(Relation.Type.SIMILAR_TO_DOCUMENTS).byUri(relation.getUri()));
    }

    // DEALS_WITH -> Topic
    public List<String> listTopics(String id){
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        return udm.find(Resource.Type.TOPIC).in(Resource.Type.DOCUMENT, uri);
    }

    public void removeTopics(String id){
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        udm.delete(Relation.Type.DEALS_WITH_FROM_DOCUMENT).in(Resource.Type.DOCUMENT,uri);
    }

    public DealsI getTopics(String startId, String endId){
        String startUri     = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String endUri       = uriGenerator.from(Resource.Type.TOPIC, endId);
        Optional<DealsI> result = udm.find(Relation.Type.DEALS_WITH_FROM_DOCUMENT).btw(startUri, endUri).stream().map(relation -> new DealsI(relation.getUri(), relation.getCreationTime(),relation.getWeight())).findFirst();
        return (result.isPresent())? result.get() : new DealsI();
    }

    public void addTopics(String startId, String endId, WeightI weightI){
        String startUri     = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String endUri       = uriGenerator.from(Resource.Type.TOPIC, endId);
        DealsWithFromDocument relation = Relation.newDealsWithFromDocument(startUri, endUri);
        relation.setWeight(weightI.getWeight());
        udm.save(relation);
    }

    public void removeTopics(String startId, String endId){
        String duri = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String iuri = uriGenerator.from(Resource.Type.TOPIC, endId);
        udm.find(Relation.Type.DEALS_WITH_FROM_DOCUMENT).btw(startId, endId).forEach(relation -> udm.delete(Relation.Type.DEALS_WITH_FROM_DOCUMENT).byUri(relation.getUri()));
    }

}
