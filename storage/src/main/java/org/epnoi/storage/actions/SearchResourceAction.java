package org.epnoi.storage.actions;

import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.Helper;
import org.epnoi.storage.session.UnifiedTransaction;
import org.neo4j.ogm.session.result.ResultProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cbadenes on 04/02/16.
 */
public class SearchResourceAction {

    private static final Logger LOG = LoggerFactory.getLogger(SearchResourceAction.class);

    private final Helper helper;
    private final Resource.Type type;

    public SearchResourceAction(Helper helper, Resource.Type type){
        this.helper = helper;
        this.type = type;
    }

    /**
     * Find all resources
     */
    public List<String> all(){
        LOG.debug("Finding " + type.name() + "s");
        List<String> uris = new ArrayList<>();
        try{
            helper.getUnifiedNodeGraphRepository().findAll(type).forEach(x -> uris.add(x.getUri()));
            LOG.info(type.name() + "s: " + uris);

        }catch (ResultProcessingException e){
            LOG.warn("getting all " + type,e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error while getting all " + type,e);
        }
        return uris;
    }

    /**
     * Find resources attached to other resource (directly or indirectly)
     * @param referenceType
     * @param referenceURI
     * @return
     */
    public List<String> in(Resource.Type referenceType, String referenceURI){
        LOG.debug("Finding " + type.name() + "s in " + referenceType + ": " + referenceURI);
        List<String> uris = new ArrayList<>();
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            helper.getUnifiedNodeGraphRepository().findIn(type, referenceType,referenceURI).forEach(x -> uris.add(x.getUri()));

            transaction.commit();
            LOG.info("In "+referenceType+": " + referenceURI + " found: ["+type + "]: " + uris);
        }catch (ResultProcessingException e){
            LOG.warn("exception while finding " + type +"s in " + referenceType + ": " + referenceURI + ":: " + e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error while finding " + type +"s in " + referenceType + ": " + referenceURI,e);
        }
        return uris;
    }

    /**
     * Find resources by a field value
     * @param field
     * @param value
     * @return
     */
    public List<String> by(String field, String value){
        LOG.debug("Finding " + type.name() + "s");
        List<String> uris = new ArrayList<>();
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            helper.getUnifiedColumnRepository().findBy(type, field,value).forEach(x -> uris.add(x.getUri()));

            transaction.commit();
            LOG.info("By "+field+": '" + value+ "' found: ["+type + "]: " + uris);
        }catch (ResultProcessingException e){
            LOG.warn("getting all " + type,e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error while getting all " + type,e);
        }
        return uris;
    }

}
