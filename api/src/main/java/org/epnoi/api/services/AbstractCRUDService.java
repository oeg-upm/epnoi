package org.epnoi.api.services;

import com.google.common.base.Strings;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.epnoi.storage.TimeGenerator;
import org.epnoi.storage.UDM;
import org.epnoi.storage.URIGenerator;
import org.epnoi.storage.model.Domain;
import org.epnoi.storage.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by cbadenes on 18/01/16.
 */

public abstract class  AbstractCRUDService<T extends Resource> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCRUDService.class);

    @Autowired
    protected UDM udm;

    @Autowired
    URIGenerator uriGenerator;

    @Autowired
    TimeGenerator timeGenerator;

    protected abstract T save(T resource);

    protected abstract Optional<T> read(String uri);

    protected abstract T delete(String uri);

    protected abstract void deleteAll();

    protected abstract List<String> findAll();

    protected abstract String newUri();


    public T create(T resource) throws Exception {

        LOG.info("Trying to create: " + resource);

        if (Strings.isNullOrEmpty(resource.getUri())){
            resource.setUri(newUri());
        }

        if (Strings.isNullOrEmpty(resource.getCreationTime())){
            resource.setCreationTime(timeGenerator.getNowAsISO());
        }

        return save(resource);
    }

    public T update(String uri,T resource){
        try {
            String decodedUri = URIUtil.decode(uri);
            LOG.debug("updating by uri: " + decodedUri);
            Optional<T> result = read(decodedUri);
            if (!result.isPresent()){
                throw new RuntimeException("Resource does not exist with uri: " + decodedUri);
            }
            T original = result.get();
            BeanUtils.copyProperties(resource,original);
            return save(original);
        } catch (URIException e) {
            throw new RuntimeException(e);
        }
    }


    public T remove(String uri){
        try {
            String decodedUri = URIUtil.decode(uri);
            LOG.debug("removing by uri: " + decodedUri);
            return delete(decodedUri);
        } catch (URIException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeAll(){
        deleteAll();
    }

    public List<String> list(){
        return findAll();
    }


    public T get(String uri){
        try {
            String decodedUri = URIUtil.decode(uri);
            LOG.debug("getting by uri: " + decodedUri);
            Optional<T> result = read(decodedUri);
            if (!result.isPresent())
                return null; //TODO Handle empty result
            return result.get();
        } catch (URIException e) {
            throw new RuntimeException(e);
        }
    }


}
