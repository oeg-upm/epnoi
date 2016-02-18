package org.epnoi.storage.actions;

import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.utils.ResourceUtils;
import org.epnoi.storage.Helper;
import org.epnoi.storage.session.UnifiedTransaction;
import org.neo4j.ogm.session.result.ResultProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public List<Relation> all(){
        LOG.debug("Finding " + type.name() + "s");
        List<Relation> relations = new ArrayList<>();
        try{
            helper.getUnifiedEdgeGraphRepository().findAll(type).forEach(x -> relations.add(Relation.class.cast(x)));
            LOG.info(type.name() + "s: " + relations);

        }catch (ResultProcessingException e){
            LOG.warn("getting all " + type,e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error while getting all " + type,e);
        }
        return relations;
    }

    /**
     * Find relation attached to other resource (directly or indirectly)
     * @param referenceType
     * @param referenceURI
     * @return
     */
    public List<Relation> in(Resource.Type referenceType, String referenceURI){
        LOG.debug("Finding " + type.name() + "s in " + referenceType + ": " + referenceURI);
        List<Relation> relations = new ArrayList<>();
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            helper.getUnifiedEdgeGraphRepository().findIn(type, referenceType, referenceURI).forEach(x -> relations.add((Relation) ResourceUtils.map(x,Relation.classOf(type))));

            transaction.commit();
            return relations;
        }catch (ResultProcessingException e){
            LOG.warn("exception while finding " + type +"s in " + referenceType + ": " + referenceURI + ":: " + e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error while finding " + type +"s in " + referenceType + ": " + referenceURI,e);
        }
        return relations;
    }

    /**
     * Find resources by a field value
     * @param field
     * @param value
     * @return
     */
    public List<Relation> by(String field, String value){
        LOG.debug("Finding " + type.name() + "s");
        List<Relation> relations = new ArrayList<>();
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            helper.getUnifiedEdgeGraphRepository().findBy(type, field, value).forEach(x -> relations.add((Relation) ResourceUtils.map(x,Relation.classOf(type))));

            transaction.commit();
            return relations;
        }catch (ResultProcessingException e){
            LOG.warn("getting all " + type,e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error while getting all " + type,e);
        }
        return relations;
    }

}