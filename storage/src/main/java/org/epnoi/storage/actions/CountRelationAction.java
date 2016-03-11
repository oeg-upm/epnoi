package org.epnoi.storage.actions;

import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.Helper;
import org.epnoi.storage.session.UnifiedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 04/02/16.
 */
public class CountRelationAction {

    private static final Logger LOG = LoggerFactory.getLogger(CountRelationAction.class);

    private final Helper helper;
    private final Relation.Type type;

    public CountRelationAction(Helper helper, Relation.Type type){
        this.helper = helper;
        this.type = type;
    }

    /**
     * Count all relations
     */
    public long all(){
        long size = 0;
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            size = helper.getTemplateFactory().of(type).countAll();

            transaction.commit();

            LOG.debug("Count All: "+type.name());
        }catch (Exception e){
            LOG.error("Unexpected error during counting all '"+type,e);
        }
        return size;
    }

    public long in(Resource.Type refType, String uri){
        long size = 0;
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            size = helper.getTemplateFactory().of(type).countIn(refType,uri);

            transaction.commit();

            LOG.debug("Count: "+type.name()+" in " + refType + "[" + uri+"]");
        }catch (Exception e){
            LOG.error("Unexpected error during counting of relations '"+ type + " in " + refType +" by uri "+uri,e);
        }
        return size;
    }

}
