package org.epnoi.storage.generator;

import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.utils.UriUtils;
import org.epnoi.storage.UDM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by cbadenes on 04/01/16.
 */
@Component
public class URIGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(URIGenerator.class);

    private static final String SEPARATOR = "/";

    @Value("${epnoi.uri.base}")
    String base;

    @Autowired
    UDM udm;

    public String basedOnContent(Resource.Type resource, String content){
        return new StringBuilder(base).append(resource.route()).append(SEPARATOR).append(UriUtils.getMD5(content)).toString();
    }

    public String from(Resource.Type resource, String id){
        return new StringBuilder(base).append(resource.route()).append(SEPARATOR).append(id).toString();
    }

    public String from(Relation.Type resource, String id){
        return new StringBuilder(base).append(resource.route()).append(SEPARATOR).append(id).toString();
    }

    public String newFor(Resource.Type type){
        String uri;
        do {
            uri = from(type,getUUID()).toString();
        } while (udm.exists(type).withUri(uri));
        return uri;
    }

    public String newFor(Relation.Type type){
        String uri;
        do {
            uri = from(type,getUUID()).toString();
        } while (udm.exists(type).withUri(uri));
        return uri;
    }

    private String getUUID(){
        return UUID.randomUUID().toString();
    }
}
