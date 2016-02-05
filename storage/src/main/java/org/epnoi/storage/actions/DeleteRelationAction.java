package org.epnoi.storage.actions;

import org.epnoi.model.domain.Relation;
import org.epnoi.model.domain.Resource;
import org.epnoi.storage.Helper;
import org.epnoi.storage.session.UnifiedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 04/02/16.
 */
public class DeleteRelationAction {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteRelationAction.class);

    private final Helper helper;
    private final Relation.Type type;

    public DeleteRelationAction(Helper helper, Relation.Type type){
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

            helper.getUnifiedEdgeGraphRepository().deleteAll(type);

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

            helper.getUnifiedEdgeGraphRepository().delete(type,uri);

            transaction.commit();

            LOG.info("Deleted: "+type.name()+"[" + uri+"]");

            //Publish the event
            //TODO

        }catch (Exception e){
            LOG.error("Unexpected error during delete of '"+uri,e);
        }
    }

    public void in(Resource.Type refType, String uri){
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            Iterable<Relation> pairs = helper.getUnifiedEdgeGraphRepository().findIn(type,refType, uri);
            if (pairs != null){
                for (Relation pair : pairs) {
                    helper.getUnifiedNodeGraphRepository().detach(pair.getStart().getUri(),pair.getEnd().getUri(),type);
                }
            }

            transaction.commit();

            LOG.info("Deleted: "+type.name()+" in " + refType + "[" + uri+"]");

            //Publish the event
            //TODO
        }catch (Exception e){
            LOG.error("Unexpected error during delete of relations '"+ type + " in " + type +" by uri "+uri,e);
        }
    }

}
