package org.epnoi.storage.actions;

import org.epnoi.model.domain.Resource;
import org.epnoi.storage.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 04/02/16.
 */
public class ExistsAction {

    private static final Logger LOG = LoggerFactory.getLogger(ExistsAction.class);

    private final Helper helper;
    private final Resource.Type type;

    public ExistsAction(Helper helper, Resource.Type type){
        this.helper = helper;
        this.type = type;
    }

    /**
     * Save a resource
     */
    public boolean withUri(String uri){
        try{
            return helper.getUnifiedColumnRepository().exists(type,uri);
        }catch (Exception e){
            LOG.error("Unexpected error while checking resource: "+uri,e);
        }
        return false;
    }

}
