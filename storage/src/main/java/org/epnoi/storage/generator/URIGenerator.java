package org.epnoi.storage.generator;

import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.UDM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by cbadenes on 04/01/16.
 */
@Component
public class URIGenerator {

    private static final String BASE = "http://epnoi.org/";

    @Autowired
    UDM udm;

    public String from(Resource.Type resource, String id){
        return new StringBuilder(BASE).append(resource.route()).append("/").append(id).toString();
    }

    public String from(Relation.Type resource, String id){
        return new StringBuilder(BASE).append(resource.route()).append("/").append(id).toString();
    }

    public String newFor(Resource.Type type){
        String uri;
        do {
            uri = from(type,UUID.randomUUID().toString()).toString();
        } while (udm.exists(type).withUri(uri));
        return uri;
    }

    public String newFor(Relation.Type type){
        String uri;
        do {
            uri = from(type,UUID.randomUUID().toString()).toString();
        } while (udm.exists(type).withUri(uri));
        return uri;
    }

}
