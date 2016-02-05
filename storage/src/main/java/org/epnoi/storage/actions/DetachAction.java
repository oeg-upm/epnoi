package org.epnoi.storage.actions;

import org.epnoi.model.Event;
import org.epnoi.model.domain.Relation;
import org.epnoi.model.domain.Resource;
import org.epnoi.model.domain.ResourceUtils;
import org.epnoi.model.modules.RoutingKey;
import org.epnoi.storage.Helper;
import org.epnoi.storage.session.UnifiedTransaction;
import org.neo4j.ogm.session.result.ResultProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 04/02/16.
 */
public class DetachAction {

    private static final Logger LOG = LoggerFactory.getLogger(DetachAction.class);

    private final Helper helper;

    private String from;
    private String to;

    public DetachAction(Helper helper, String uri){
        this.helper = helper;
        this.from = uri;
    }


    /**
     * Resource to be attached
     * @param uri
     * @return
     */
    public DetachAction to(String uri){
        this.to = uri;
        return this;
    }


    /**
     * Relation to be detached between the resources
     * @param type
     * @return
     */
    public void by(Relation.Type type){
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            Resource resource = helper.getUnifiedNodeGraphRepository().detach(from,to,type);
            transaction.commit();

            //Publish the event
            helper.getEventBus().post(Event.from(ResourceUtils.map(resource,type.getStart().classOf())), RoutingKey.of(type.getStart(), Resource.State.UPDATED));
        }catch (ResultProcessingException e){
            LOG.warn("Creating relation between:["+from+"-"+to+"]",e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error during relation " + type + " between '"+from +"' and '"+to+"'",e);
        }
    }

}
