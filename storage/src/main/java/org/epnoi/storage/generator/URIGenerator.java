package org.epnoi.storage.generator;

import org.epnoi.model.domain.Resource;
import org.epnoi.storage.UDM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by cbadenes on 04/01/16.
 */
@Component
public class URIGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(URIGenerator.class);

    private static final String BASE = "http://epnoi.org/";

    @Autowired
    UDM udm;

    public String from(Resource.Type resource, String id){
        return new StringBuilder(BASE).append(resource.plural()).append("/").append(id).toString();
    }

    public String newFor(Resource.Type type){
        String uri;
        do {
            uri = from(type,UUID.randomUUID().toString()).toString();
        } while (udm.exists(type).withUri(uri));
        return uri;
    }

}
