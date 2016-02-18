package org.epnoi.storage;

import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.actions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 23/12/15.
 */
@Component
public class UDM {

    private static final Logger LOG = LoggerFactory.getLogger(UDM.class);

    @Autowired
    Helper helper;

    /**
     * Save a resource
     * @param resource
     */
    public SaveResourceAction save(Resource resource){return new SaveResourceAction(helper,resource);
    }


    /**
     * Save a relation
     * @param relation
     */
    public SaveRelationAction save(Relation relation){
        return new SaveRelationAction(helper,relation);
    }

    /**
     * Check if a 'type' resource identified by 'uri' exists
     * @param type
     * @return boolean
     */
    public ExistsResourceAction exists(Resource.Type type){
        return new ExistsResourceAction(helper,type);
    }

    /**
     * Check if a 'type' relation identified by 'uri' exists
     * @param type
     * @return boolean
     */
    public ExistsRelationAction exists(Relation.Type type){
        return new ExistsRelationAction(helper,type);
    }

    /**
     * Read the 'type' resource identified by 'uri'
     * @param type
     * @return resource
     */
    public ReadResourceAction read(Resource.Type type){return new ReadResourceAction(helper,type);
    }

    /**
     * Read the 'type' resource identified by 'uri'
     * @param type
     * @return relation
     */
    public ReadRelationAction read(Relation.Type type){
        return new ReadRelationAction(helper,type);
    }


    /**
     * Search 'type' resources
     * @param type
     * @return uris
     */
    public SearchResourceAction find(Resource.Type type){
        return new SearchResourceAction(helper,type);
    }

    /**
     * Search 'type' relations
     * @param type
     * @return uris
     */
    public SearchRelationAction find(Relation.Type type){
        return new SearchRelationAction(helper,type);
    }

    /**
     * Delete 'type' resources
     * @param type
     * @return
     */
    public DeleteResourceAction delete(Resource.Type type){
        return new DeleteResourceAction(helper,type);
    }

    /**
     * Delete 'type' relations
     * @param type
     * @return
     */
    public DeleteRelationAction delete(Relation.Type type){
        return new DeleteRelationAction(helper,type);
    }

}
