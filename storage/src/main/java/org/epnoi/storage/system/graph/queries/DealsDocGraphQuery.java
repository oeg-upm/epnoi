package org.epnoi.storage.system.graph.queries;

import org.apache.commons.beanutils.BeanUtils;
import org.epnoi.model.domain.relations.DealsWithFromDocument;
import org.epnoi.model.domain.relations.EmbeddedIn;
import org.epnoi.model.domain.relations.PairsWith;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.system.graph.domain.edges.DealsWithFromDocumentEdge;
import org.epnoi.storage.system.graph.domain.nodes.DocumentNode;
import org.epnoi.storage.system.graph.domain.nodes.TopicNode;
import org.neo4j.ogm.model.QueryStatistics;
import org.neo4j.ogm.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class DealsDocGraphQuery implements GraphQuery<DealsWithFromDocument> {

    private static final Logger LOG = LoggerFactory.getLogger(DealsDocGraphQuery.class);

    @Autowired
    GraphQueryExecutor executor;

    @Override
    public Relation.Type accept() {
        return Relation.Type.DEALS_WITH_FROM_DOCUMENT;
    }

    @Override
    public List<DealsWithFromDocument> inDomain(String uri) {
        Map<String,String> params = new HashMap<>();
        params.put("0",uri);
        Result result = executor.query("match (domain:Domain{uri:{0}})-[c:CONTAINS]->(d:Document)-[r:DEALS_WITH]->(t:Topic) return r,d,t", params);

        List<DealsWithFromDocument> similars = new ArrayList<>();

        Iterator<Map<String, Object>> it = result.queryResults().iterator();
        while(it.hasNext()){
            try {
                Map<String, Object> map = it.next();

                Map rValues = (Map) map.get("r");
                DealsWithFromDocument dealsWith = new DealsWithFromDocument();
                BeanUtils.populate(dealsWith,rValues);

                Map dValues = (Map) map.get("d");
                DocumentNode dNode = new DocumentNode();
                BeanUtils.populate(dNode,dValues);

                Map tValues = (Map) map.get("t");
                TopicNode tNode = new TopicNode();
                BeanUtils.populate(tNode,tValues);

                dealsWith.setStartUri(dNode.getUri());
                dealsWith.setEndUri(tNode.getUri());

                similars.add(dealsWith);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.error("Error getting deals_with relation from: " + uri ,e);
            }
        }
        return similars;
    }


    @Override
    public List<DealsWithFromDocument> query(String startUri, String endUri) {
        Map<String,String> params = new HashMap<>();
        params.put("0",startUri);
        params.put("1",endUri);
        Result result = executor.query("match (node1:Document{uri:{0}})-[r:DEALS_WITH]->(node2:Topic{uri:{1}}) return r", params);

        List<DealsWithFromDocument> similars = new ArrayList<>();

        Iterator<Map<String, Object>> it = result.queryResults().iterator();
        while(it.hasNext()){
            try {
                Map values = (Map) it.next().get("r");
                DealsWithFromDocument similarTo = new DealsWithFromDocument();
                BeanUtils.populate(similarTo,values);
                similarTo.setStartUri(startUri);
                similarTo.setEndUri(endUri);
                similars.add(similarTo);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.error("Error getting deals_with relation between: " + startUri +" and " + endUri,e);
            }
        }
        return similars;
    }

    @Override
    public void deleteIn(Resource.Type type, String uri) {
        Map<String,Object> params = new HashMap<>();
        params.put("0",uri);

        String query;
        switch (type){
            case DOMAIN:
                query = "match (domain{uri:{0}})-[c:CONTAINS]->(d1:Document)-[r:DEALS_WITH]->(t:Topic) delete r";
                break;
            default: query = "";
        }

        QueryStatistics result = executor.execute(query, params);
        LOG.info("Result of query execution ["+ query + "] is: " + result);
    }

    @Override
    public List<String> findAll() {
        Result result = executor.query("match (node1:Document)-[r:DEALS_WITH]->(node2:Topic) return r", new HashMap<>());

        List<String> relations = new ArrayList<>();

        Iterator<Map<String, Object>> it = result.queryResults().iterator();
        while(it.hasNext()){
            try {
                Map values = (Map) it.next().get("r");
                DealsWithFromDocument relation = new DealsWithFromDocument();
                BeanUtils.populate(relation,values);
                relations.add(relation.getUri());
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.error("Error getting all relations",e);
            }
        }
        return relations;
    }

    @Override
    public void save(Relation relation) {
        DealsWithFromDocument edge = relation.asDealsWithFromDocument();


        Map<String,Object> params = new HashMap<>();
        params.put("0",edge.getStartUri());
        params.put("1",edge.getEndUri());
        params.put("2",edge.getUri());
        params.put("3",edge.getCreationTime());
        params.put("4",edge.getWeight());

        LOG.trace("Trying to create DEALS_IN_FROM_DOC relation");
        try{
            QueryStatistics result = executor.execute("MATCH (a:Document),(b:Topic) WHERE a.uri = {0} AND b.uri = {1} CREATE (a)-[r:DEALS_WITH { uri : {2}, creationTime : {3}, weight: {4}}]->(b) RETURN r", params);
            LOG.debug("created DEALS_IN_FROM_DOC relation: -> " +result.getRelationshipsCreated());
        }catch (Exception e){
            LOG.error("Error on DEALS_IN_FROM_DOC",e);
        }
    }
}
