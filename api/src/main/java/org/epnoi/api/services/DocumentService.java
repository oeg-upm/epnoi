package org.epnoi.api.services;

import org.apache.commons.beanutils.BeanUtils;
import org.epnoi.api.model.RelationI;
import org.epnoi.api.model.WeightI;
import org.epnoi.api.model.DocumentI;
import org.epnoi.api.model.SimilarI;
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
import java.util.stream.Collectors;

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

    // BUNDLES Items
    public List<RelationI> listItems(String id){
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        return udm.find(Relation.Type.BUNDLES).in(Resource.Type.DOCUMENT, uri).stream().map(relation -> new RelationI(relation.getEndUri(),relation.getWeight())).collect(Collectors.toList());
    }

    public void removeItems(String id){
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        udm.delete(Relation.Type.BUNDLES).in(Resource.Type.DOCUMENT,uri);
    }

    public void removeItems(String did,String iid){
        String duri = uriGenerator.from(Resource.Type.DOCUMENT, did);
        String iuri = uriGenerator.from(Resource.Type.ITEM, iid);
        udm.find(Relation.Type.BUNDLES).btw(duri, iuri).stream().forEach(rel -> udm.delete(Relation.Type.BUNDLES).byUri(rel.getUri()));
    }

    public void addItem(String startId, String endId){
        String startUri     = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String endUri       = uriGenerator.from(Resource.Type.ITEM, endId);
        udm.save(Relation.newBundles(startUri,endUri));
    }


    // SIMILAR_TO Documents
    public List<RelationI> listDocuments(String id){
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        return udm.find(Relation.Type.SIMILAR_TO_DOCUMENTS).in(Resource.Type.DOCUMENT, uri).stream().map(relation -> new RelationI(relation.getEndUri(),relation.getWeight())).collect(Collectors.toList());
    }

    public void removeDocuments(String id){
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        udm.delete(Relation.Type.SIMILAR_TO_DOCUMENTS).in(Resource.Type.DOCUMENT,uri);
    }

    public void addDocument(String startId, String endId, SimilarI similarity){
        String startUri     = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String endUri       = uriGenerator.from(Resource.Type.DOCUMENT, endId);
        SimilarToDocuments relation = Relation.newSimilarToDocuments(startUri, endUri);
        relation.setDomain(uriGenerator.from(Resource.Type.DOMAIN,similarity.getDomainId()));
        relation.setWeight(similarity.getWeight());
        udm.save(relation);
    }


    // DEALS_WITH Topic
    public List<RelationI> listTopics(String id){
        LOG.info("List of topics from: "+ id);
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        return udm.find(Relation.Type.DEALS_WITH_FROM_DOCUMENT).in(Resource.Type.DOCUMENT, uri).stream().map(relation -> new RelationI(relation.getEndUri(),relation.getWeight())).collect(Collectors.toList());
    }

    public void removeTopics(String id){
        LOG.info("Removing topic from: "+ id);
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        udm.delete(Relation.Type.DEALS_WITH_FROM_DOCUMENT).in(Resource.Type.DOCUMENT,uri);
    }

    public void addTopic(String startId, String endId, WeightI deals){
        LOG.info("Adding topic from: "+ startId + " to: " + endId + " by: " + deals);
        String startUri     = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String endUri       = uriGenerator.from(Resource.Type.DOCUMENT, endId);
        DealsWithFromDocument relation = Relation.newDealsWithFromDocument(startUri, endUri);
        relation.setWeight(deals.getWeight());
        udm.save(relation);
    }


}
