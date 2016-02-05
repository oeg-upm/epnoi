package org.epnoi.storage.actions;

import org.epnoi.model.domain.Relation;
import org.epnoi.model.domain.Resource;
import org.epnoi.storage.Helper;
import org.epnoi.storage.session.UnifiedTransaction;
import org.neo4j.ogm.session.result.ResultProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

/**
 * Created by cbadenes on 04/02/16.
 */
public class SearchRelationAction {

    private static final Logger LOG = LoggerFactory.getLogger(SearchRelationAction.class);

    private final Helper helper;
    private final Relation.Type type;

    public SearchRelationAction(Helper helper, Relation.Type type){
        this.helper = helper;
        this.type = type;
    }

    /**
     * Find all relations
     */
    public Iterable<Relation> all(){
        LOG.debug("Finding " + type.name() + "s");
        try{
            return helper.getUnifiedEdgeGraphRepository().findAll(type);

        }catch (ResultProcessingException e){
            LOG.warn("getting all " + type,e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error while getting all " + type,e);
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * Find resources attached to other resource (directly or indirectly)
     * @param referenceType
     * @param referenceURI
     * @return
     */
    public Iterable<Relation> in(Resource.Type referenceType, String referenceURI){
        LOG.debug("Finding " + type.name() + "s in " + referenceType + ": " + referenceURI);
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            Iterable<Relation> relations = helper.getUnifiedEdgeGraphRepository().findIn(type, referenceType, referenceURI);

            transaction.commit();
            return relations;
        }catch (ResultProcessingException e){
            LOG.warn("exception while finding " + type +"s in " + referenceType + ": " + referenceURI + ":: " + e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error while finding " + type +"s in " + referenceType + ": " + referenceURI,e);
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * Find resources by a field value
     * @param field
     * @param value
     * @return
     */
    public Iterable<Relation> by(String field, String value){
        LOG.debug("Finding " + type.name() + "s");
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            Iterable<Relation> relations = helper.getUnifiedEdgeGraphRepository().findBy(type, field, value);

            transaction.commit();
            return relations;
        }catch (ResultProcessingException e){
            LOG.warn("getting all " + type,e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error while getting all " + type,e);
        }
        return Collections.EMPTY_LIST;
    }

}
