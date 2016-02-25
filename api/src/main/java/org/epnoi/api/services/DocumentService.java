package org.epnoi.api.services;

import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Document;
import org.epnoi.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class DocumentService extends AbstractResourceService<Document> {

    public DocumentService() {
        super(Resource.Type.DOCUMENT);
    }


    // BUNDLES Items
    public List<String> listItems(String id){
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        return udm.find(Resource.Type.ITEM).in(Resource.Type.DOCUMENT,uri);
    }

    public void removeItems(String id){
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        udm.delete(Relation.Type.BUNDLES).in(Resource.Type.DOCUMENT,uri);
    }

    public void addItem(String startId, String endId){
        String startUri     = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String endUri       = uriGenerator.from(Resource.Type.ITEM, endId);
        udm.save(Relation.newBundles(startUri,endUri));
    }


    // SIMILAR_TO Documents
    public List<String> listDocuments(String id){
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        return udm.find(Resource.Type.DOCUMENT).in(Resource.Type.DOCUMENT,uri);
    }

    public void removeDocuments(String id){
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        udm.delete(Relation.Type.SIMILAR_TO_DOCUMENTS).in(Resource.Type.DOCUMENT,uri);
    }

    public void addDocument(String startId, String endId){
        String startUri     = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String endUri       = uriGenerator.from(Resource.Type.DOCUMENT, endId);
        udm.save(Relation.newSimilarToDocuments(startUri,endUri));
    }


    // DEALS_WITH Topic
    public List<String> listTopics(String id){
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        return udm.find(Relation.Type.DEALS_WITH_FROM_DOCUMENT).in(Resource.Type.DOCUMENT,uri).stream().map(relation -> relation.getEndUri()).collect(Collectors.toList());
    }

    public void removeTopics(String id){
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        udm.delete(Relation.Type.DEALS_WITH_FROM_DOCUMENT).in(Resource.Type.DOCUMENT,uri);
    }

    public void addTopic(String startId, String endId){
        String startUri     = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String endUri       = uriGenerator.from(Resource.Type.DOCUMENT, endId);
        udm.save(Relation.newDealsWithFromDocument(startUri,endUri));
    }


}
