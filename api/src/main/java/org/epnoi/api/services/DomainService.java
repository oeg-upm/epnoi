package org.epnoi.api.services;

import org.epnoi.api.model.relations.RelationI;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Domain;
import org.epnoi.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class DomainService extends AbstractResourceService<Domain> {

    public DomainService() {
        super(Resource.Type.DOMAIN);
    }


    public void add(String sId, String  rId){
        udm.save(Relation.newContains(uriGenerator.from(Resource.Type.DOMAIN,sId),uriGenerator.from(Resource.Type.DOCUMENT,rId)));
    }

    // CONTAINS -> Document
    public List<String> listDocuments(String id){
        String uri = uriGenerator.from(Resource.Type.DOMAIN, id);
        return udm.find(Resource.Type.DOCUMENT).in(Resource.Type.DOMAIN, uri);
    }

    public void removeDocuments(String id){
        String uri = uriGenerator.from(Resource.Type.DOMAIN, id);
        udm.delete(Relation.Type.CONTAINS).in(Resource.Type.DOMAIN,uri);
    }

    public RelationI getDocuments(String startId, String endId){
        String startUri     = uriGenerator.from(Resource.Type.DOMAIN, startId);
        String endUri       = uriGenerator.from(Resource.Type.DOCUMENT, endId);
        Optional<RelationI> result = udm.find(Relation.Type.CONTAINS).btw(startUri, endUri).stream().map(relation -> new RelationI(relation.getUri(), relation.getCreationTime())).findFirst();
        return (result.isPresent())? result.get() : new RelationI();
    }

    public void addDocuments(String startId, String endId){
        String startUri     = uriGenerator.from(Resource.Type.DOMAIN, startId);
        String endUri       = uriGenerator.from(Resource.Type.DOCUMENT, endId);
        udm.save(Relation.newContains(startUri,endUri));
    }

    public void removeDocuments(String startId, String endId){
        String duri = uriGenerator.from(Resource.Type.DOMAIN, startId);
        String iuri = uriGenerator.from(Resource.Type.DOCUMENT, endId);
        udm.find(Relation.Type.CONTAINS).btw(startId, endId).forEach(relation -> udm.delete(Relation.Type.CONTAINS).byUri(relation.getUri()));
    }


}
