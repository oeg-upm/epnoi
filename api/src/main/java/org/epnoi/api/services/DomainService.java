package org.epnoi.api.services;

import org.epnoi.api.model.ResourceI;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Domain;
import org.epnoi.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

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

}
