package org.epnoi.storage.actions;

import org.epnoi.model.Event;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.modules.RoutingKey;
import org.epnoi.storage.Helper;
import org.epnoi.storage.session.UnifiedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 04/02/16.
 */
public class SaveResourceAction {

    private static final Logger LOG = LoggerFactory.getLogger(SaveResourceAction.class);


    public SaveResourceAction(Helper helper, Resource resource){
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            LOG.debug("trying to save: " + resource);

            // initialize URI
            if (!resource.hasUri()){
                resource.setUri(helper.getUriGenerator().newFor(resource.getResourceType()));
            }

            // Checking if exists
            if (helper.getUnifiedColumnRepository().exists(resource.getResourceType(),resource.getUri())){
                LOG.warn("Resource already exists: " + resource);
                return;
            }

            // column
            helper.getUnifiedColumnRepository().save(resource);
            // document
            helper.getUnifiedDocumentRepository().save(resource);
            // graph
            helper.getUnifiedNodeGraphRepository().save(resource);

            transaction.commit();

            LOG.debug("Resource Saved: " + resource);
            //Publish the event
            helper.getEventBus().post(Event.from(resource), RoutingKey.of(resource.getResourceType(), Resource.State.CREATED));
        }catch (Exception e){
            LOG.error("Unexpected error while saving resource: "+resource,e);
        }
    }

}
