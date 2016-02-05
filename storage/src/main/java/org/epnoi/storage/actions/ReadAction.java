package org.epnoi.storage.actions;

import org.epnoi.model.domain.Resource;
import org.epnoi.storage.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by cbadenes on 04/02/16.
 */
public class ReadAction {

    private static final Logger LOG = LoggerFactory.getLogger(ReadAction.class);

    private final Helper helper;
    private final Resource.Type type;

    public ReadAction(Helper helper, Resource.Type type){
        this.helper = helper;
        this.type = type;
    }

    /**
     * Save a resource
     */
    public Optional<Resource> byUri(String uri){
        try{
            return helper.getUnifiedColumnRepository().read(type,uri);
        }catch (Exception e){
            LOG.error("Unexpected error while checking resource: "+uri,e);
            return Optional.empty();
        }
    }

}
