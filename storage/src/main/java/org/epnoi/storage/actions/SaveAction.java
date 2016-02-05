package org.epnoi.storage.actions;

import org.epnoi.model.Event;
import org.epnoi.model.domain.Resource;
import org.epnoi.model.modules.RoutingKey;
import org.epnoi.storage.Helper;
import org.epnoi.storage.session.UnifiedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 04/02/16.
 */
public class SaveAction {

    private static final Logger LOG = LoggerFactory.getLogger(SaveAction.class);

    private final Helper helper;
    private final Resource.Type type;

    public SaveAction(Helper helper, Resource.Type type){
        this.helper = helper;
        this.type = type;
    }

    /**
     * Save a resource
     */
    public void with(Resource resource){
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            LOG.debug("trying to save :" + resource);
            // column
            helper.getUnifiedColumnRepository().save(resource,type);
            // document
            helper.getUnifiedDocumentRepository().save(resource,type);
            // graph
            helper.getUnifiedNodeGraphRepository().save(resource, type);

            transaction.commit();

            LOG.info("Resource Saved: " + resource);
            //Publish the event
            helper.getEventBus().post(Event.from(resource), RoutingKey.of(type, Resource.State.CREATED));
        }catch (Exception e){
            LOG.error("Unexpected error while saving resource: "+resource,e);
        }
    }

}
