package org.epnoi.storage.actions;

import org.epnoi.model.Event;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.modules.RoutingKey;
import org.epnoi.storage.Helper;
import org.epnoi.storage.session.UnifiedTransaction;
import org.epnoi.storage.system.graph.domain.edges.Edge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 04/02/16.
 */
public class SaveRelationAction {

    private static final Logger LOG = LoggerFactory.getLogger(SaveRelationAction.class);


    public SaveRelationAction(Helper helper, Relation relation){
        try{
            LOG.debug("trying to save :" + relation);

            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            // initialize URI
            if (!relation.hasUri()){
                relation.setUri(helper.getUriGenerator().newFor(relation.getType()));
            }

            helper.getUnifiedEdgeGraphRepository().save(relation);

            transaction.commit();

            LOG.info("Relation Saved: " + relation);
            //Publish the event
            helper.getEventBus().post(Event.from(relation), RoutingKey.of(relation.getType(), Relation.State.CREATED));
        }catch (Exception e){
            LOG.error("Unexpected error while saving relation: "+relation,e);
        }
    }

}
