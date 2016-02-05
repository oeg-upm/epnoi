package org.epnoi.storage;

import org.epnoi.model.domain.Relation;
import org.epnoi.model.domain.Resource;
import org.epnoi.storage.actions.*;
import org.epnoi.storage.session.UnifiedSession;
import org.epnoi.storage.system.column.repository.UnifiedColumnRepository;
import org.epnoi.storage.system.document.repository.UnifiedDocumentRepository;
import org.epnoi.storage.system.graph.repository.nodes.UnifiedNodeGraphRepository;
import org.epnoi.storage.system.graph.repository.nodes.DocumentGraphRepository;
import org.epnoi.storage.system.graph.repository.nodes.ItemGraphRepository;
import org.epnoi.storage.system.graph.repository.nodes.PartGraphRepository;
import org.epnoi.storage.system.graph.repository.nodes.WordGraphRepository;
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

    @Autowired
    UnifiedSession session;

    @Autowired
    UnifiedColumnRepository unifiedColumnRepository;

    @Autowired
    UnifiedDocumentRepository unifiedDocumentRepository;

    @Autowired
    UnifiedNodeGraphRepository unifiedNodeGraphRepository;

    /**
     * Save a resource
     * @param type
     */
    public SaveAction save(Resource.Type type){
        return new SaveAction(helper,type);
    }


    /**
     * Check if a 'type' resource identified by 'uri' exists
     * @param type
     * @return
     */
    public ExistsAction exists(Resource.Type type){
        return new ExistsAction(helper,type);
    }

    /**
     * Read the 'type' resource identified by 'uri'
     * @param type
     * @return
     */
    public ReadAction read(Resource.Type type){
        return new ReadAction(helper,type);
    }

    /**
     * Create a new attachment from resource with 'uri'
     * @param uri
     * @return
     */
    public AttachAction attachFrom(String uri){
        return new AttachAction(helper,uri);
    }

    /**
     * Delete an existing attachment from resource with 'uri'
     * @param uri
     * @return
     */
    public DetachAction detachFrom(String uri){
        return new DetachAction(helper,uri);
    }

    /**
     * Search 'type' resources
     * @param type
     * @return
     */
    public SearchResourceAction find(Resource.Type type){
        return new SearchResourceAction(helper,type);
    }

    /**
     * Search 'type' relations
     * @param type
     * @return
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
