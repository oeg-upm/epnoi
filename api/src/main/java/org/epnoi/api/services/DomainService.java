package org.epnoi.api.services;

import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Domain;
import org.epnoi.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

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

    //PROVIDES
    public List<String> listDocuments(String id){
        String uri = uriGenerator.from(Resource.Type.DOMAIN, id);
        return udm.find(Resource.Type.DOCUMENT).in(Resource.Type.DOMAIN,uri);
    }

    public void removeDocuments(String id){
        String uri = uriGenerator.from(Resource.Type.DOMAIN, id);
        udm.delete(Relation.Type.CONTAINS).in(Resource.Type.DOMAIN,uri);
    }

    public void addDocument(String sourceId, String documentId){
        String sourceUri    = uriGenerator.from(Resource.Type.DOMAIN, sourceId);
        String documentUri  = uriGenerator.from(Resource.Type.DOCUMENT, documentId);
        udm.save(Relation.newContains(sourceUri,documentUri));
    }


}
