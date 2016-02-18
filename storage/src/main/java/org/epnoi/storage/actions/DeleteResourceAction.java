package org.epnoi.storage.actions;

import org.epnoi.model.Event;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.modules.RoutingKey;
import org.epnoi.storage.Helper;
import org.epnoi.storage.session.UnifiedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cbadenes on 04/02/16.
 */
public class DeleteResourceAction {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteResourceAction.class);

    private final Helper helper;
    private final Resource.Type type;

    public DeleteResourceAction(Helper helper, Resource.Type type){
        this.helper = helper;
        this.type = type;
    }

    /**
     * Delete all resources
     */
    public void all(){
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            List<Resource.Type> types = (type.equals(Resource.Type.ANY)) ? Arrays.asList(Resource.Type.values()) : Arrays.asList(new Resource.Type[]{type});

            types.stream().filter(x -> !x.equals(Resource.Type.ANY)).forEach(t ->{
                helper.getUnifiedColumnRepository().deleteAll(t);
                helper.getUnifiedDocumentRepository().deleteAll(t);
                helper.getUnifiedNodeGraphRepository().deleteAll(t);
            });


            transaction.commit();

            LOG.info("Deleted All: "+type.name());

            //Publish the event
            // TODO
        }catch (Exception e){
            LOG.error("Unexpected error during delete all '"+type,e);
        }
    }

    /**
     * Delete resource identified by 'uri'
     * @param uri
     */
    public void byUri(String uri){
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            helper.getUnifiedColumnRepository().delete(type,uri);
            helper.getUnifiedDocumentRepository().delete(type,uri);
            helper.getUnifiedNodeGraphRepository().delete(type,uri);

            transaction.commit();

            LOG.info("Deleted: "+type.name()+"[" + uri+"]");

            //Publish the event

            //TODO
            //helper.getEventBus().post(Event.from(resource), RoutingKey.of(type, Resource.State.DELETED));

        }catch (Exception e){
            LOG.error("Unexpected error during delete of '"+uri,e);
        }
    }

}
