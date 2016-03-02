package org.epnoi.api.services;

import org.epnoi.api.model.relations.*;
import org.epnoi.model.domain.relations.DealsWithFromItem;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.relations.SimilarTo;
import org.epnoi.model.domain.relations.SimilarToItems;
import org.epnoi.model.domain.resources.Item;
import org.epnoi.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class ItemService extends AbstractResourceService<Item> {

    public ItemService() {
        super(Resource.Type.ITEM);
    }

    // DEALS_WITH -> Topic
    public List<String> listTopics(String id){
        String uri = uriGenerator.from(Resource.Type.ITEM, id);
        return udm.find(Resource.Type.TOPIC).in(Resource.Type.ITEM, uri);
    }

    public void removeTopics(String id){
        String uri = uriGenerator.from(Resource.Type.ITEM, id);
        udm.delete(Relation.Type.DEALS_WITH_FROM_ITEM).in(Resource.Type.ITEM,uri);
    }

    public DealsI getTopics(String startId, String endId){
        String startUri     = uriGenerator.from(Resource.Type.ITEM, startId);
        String endUri       = uriGenerator.from(Resource.Type.TOPIC, endId);
        Optional<DealsI> result = udm.find(Relation.Type.DEALS_WITH_FROM_ITEM).btw(startUri, endUri).stream().map(relation -> new DealsI(relation.getUri(), relation.getCreationTime(), relation.getWeight())).findFirst();
        return (result.isPresent())? result.get() : null;
    }

    public void addTopics(String startId, String endId, WeightI weightI){
        String startUri     = uriGenerator.from(Resource.Type.ITEM, startId);
        String endUri       = uriGenerator.from(Resource.Type.TOPIC, endId);
        DealsWithFromItem relation = Relation.newDealsWithFromItem(startUri, endUri);
        relation.setWeight(weightI.getWeight());
        udm.save(relation);
    }

    public void removeTopics(String startId, String endId){
        String duri = uriGenerator.from(Resource.Type.ITEM, startId);
        String iuri = uriGenerator.from(Resource.Type.TOPIC, endId);
        udm.find(Relation.Type.DEALS_WITH_FROM_ITEM).btw(startId, endId).forEach(relation -> udm.delete(Relation.Type.DEALS_WITH_FROM_ITEM).byUri(relation.getUri()));
    }


    // SIMILAR_TO -> Item
    public List<String> listItems(String id){
        String uri = uriGenerator.from(Resource.Type.ITEM, id);
        return udm.find(Resource.Type.ITEM).in(Resource.Type.ITEM, uri);
    }

    public void removeItems(String id){
        String uri = uriGenerator.from(Resource.Type.ITEM, id);
        udm.delete(Relation.Type.SIMILAR_TO_ITEMS).in(Resource.Type.ITEM,uri);
    }

    public SimilarI getItems(String startId, String endId){
        String startUri     = uriGenerator.from(Resource.Type.ITEM, startId);
        String endUri       = uriGenerator.from(Resource.Type.ITEM, endId);
        Optional<SimilarI> result = udm.find(Relation.Type.SIMILAR_TO_ITEMS).btw(startUri, endUri).stream().map(relation -> new SimilarI(relation.getUri(), relation.getCreationTime(), relation.getWeight(), ((SimilarTo)relation).getDomain())).findFirst();
        return (result.isPresent())? result.get() : null;
    }

    public void addItems(String startId, String endId, WeightDomainI weightI){
        String startUri     = uriGenerator.from(Resource.Type.ITEM, startId);
        String endUri       = uriGenerator.from(Resource.Type.ITEM, endId);
        SimilarToItems relation = Relation.newSimilarToItems(startUri, endUri);
        relation.setWeight(weightI.getWeight());
        relation.setDomain(weightI.getDomain());
        udm.save(relation);
    }

    public void removeItems(String startId, String endId){
        String duri = uriGenerator.from(Resource.Type.ITEM, startId);
        String iuri = uriGenerator.from(Resource.Type.ITEM, endId);
        udm.find(Relation.Type.SIMILAR_TO_ITEMS).btw(startId, endId).forEach(relation -> udm.delete(Relation.Type.SIMILAR_TO_ITEMS).byUri(relation.getUri()));
    }
}
