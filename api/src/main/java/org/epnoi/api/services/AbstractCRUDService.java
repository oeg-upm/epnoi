package org.epnoi.api.services;

import com.google.common.base.Strings;
import org.epnoi.model.domain.Resource;
import org.epnoi.storage.generator.TimeGenerator;
import org.epnoi.storage.UDM;
import org.epnoi.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

/**
 * Created by cbadenes on 18/01/16.
 */

public abstract class  AbstractCRUDService<T extends Resource> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCRUDService.class);

    private final Resource.Type type;

    @Autowired
    protected UDM udm;

    @Autowired
    URIGenerator uriGenerator;

    @Autowired
    TimeGenerator timeGenerator;

    public AbstractCRUDService(Resource.Type type){
        this.type = type;
    }


    protected abstract T save(T resource);

    protected abstract Optional<T> read(String uri);

    protected abstract void delete(String uri);

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

    public T update(String id,T resource){
        String uri = uriGenerator.from(type,id);
        LOG.debug("updating by uri: " + uri);
        Optional<T> result = read(uri);
        if (!result.isPresent()){
            throw new RuntimeException("Resource does not exist with uri: " + uri);
        }
        T original = result.get();
        BeanUtils.copyProperties(resource,original);
        return save(original);
    }


    public void remove(String id){
        String uri = uriGenerator.from(type,id);
        LOG.debug("removing by uri: " + uri);
        delete(uri);
    }

    public void removeAll(){
        deleteAll();
    }

    public List<String> list(){
        return findAll();
    }


    public T get(String id){
        String uri = uriGenerator.from(type,id);
        LOG.debug("getting by uri: " + uri);
        Optional<T> result = read(uri);
        if (!result.isPresent())
            return null; //TODO Handle empty result
        return result.get();
    }


}
