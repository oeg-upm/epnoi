package org.epnoi.storage.system.graph.queries;

import org.apache.commons.beanutils.BeanUtils;
import org.epnoi.model.domain.relations.EmbeddedIn;
import org.epnoi.model.domain.relations.PairsWith;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.system.graph.GraphIdFactory;
import org.epnoi.storage.system.graph.domain.edges.EmbeddedInEdge;
import org.epnoi.storage.system.graph.domain.nodes.DomainNode;
import org.epnoi.storage.system.graph.domain.nodes.WordNode;
import org.neo4j.helpers.TransactionTemplate;
import org.neo4j.ogm.model.QueryStatistics;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.response.model.RelationshipModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class EmbeddedInGraphQuery implements GraphQuery<EmbeddedIn> {

    private static final Logger LOG = LoggerFactory.getLogger(EmbeddedInGraphQuery.class);

    @Autowired
    GraphQueryExecutor executor;

    @Autowired
    GraphIdFactory graphIdFactory;

    @Override
    public Relation.Type accept() {
        return Relation.Type.EMBEDDED_IN;
    }

    @Override
    public List<EmbeddedIn> query(String startUri, String endUri) {
        Map<String,String> params = new HashMap<>();
        params.put("0",startUri);
        params.put("1",endUri);
        Result result = executor.query("match (node1:Word{uri:{0}})-[r:EMBEDDED_IN]->(node2:Domain{uri:{1}}) return r", params);

        List<EmbeddedIn> similars = new ArrayList<>();

        Iterator<Map<String, Object>> it = result.queryResults().iterator();
        while(it.hasNext()){
            try {

                EmbeddedInEdge edge = (EmbeddedInEdge) it.next().get("r");
                EmbeddedIn embeddedIn = new EmbeddedIn();
                BeanUtils.copyProperties(embeddedIn,edge);
                similars.add(embeddedIn);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.error("Error getting similar_to relation between: " + startUri +" and " + endUri,e);
            }
        }
        return similars;
    }

    @Override
    public List<EmbeddedIn> inDomain(String uri) {
        Map<String,String> params = new HashMap<>();
        params.put("0",uri);
        Result result = executor.query("match (d:Domain{uri:{0}})<-[r:EMBEDDED_IN]-(w:Word) return r,w,d", params);

        List<EmbeddedIn> relations = new ArrayList<>();

        Iterator<Map<String, Object>> it = result.queryResults().iterator();
        while(it.hasNext()){
            try {
                Map<String, Object> map = it.next();

                Map rValues = (Map) map.get("r");
                EmbeddedIn relation = new EmbeddedIn();
                BeanUtils.populate(relation,rValues);

                Map sValues = (Map) map.get("w");
                WordNode sNode = new WordNode();
                BeanUtils.populate(sNode,sValues);

                Map eValues = (Map) map.get("d");
                DomainNode eNode = new DomainNode();
                BeanUtils.populate(eNode,eValues);

                relation.setStartUri(sNode.getUri());
                relation.setEndUri(eNode.getUri());

                relations.add(relation);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.error("Error getting embedded_in relations from: " + uri ,e);
            }
        }
        return relations;
    }

    @Override
    public void deleteIn(Resource.Type type, String uri){

        Map<String,Object> params = new HashMap<>();
        params.put("0",uri);

        String query = "";
        switch (type){
            case DOMAIN:
                query = "match (domain{uri:{0}})<-[r:EMBEDDED_IN]-(w1:Word) delete r";
                break;
            default: query = "";
        }

        QueryStatistics result = executor.execute(query, params);
        LOG.info("Result of query execution ["+ query + "] is: " + result);
    }

    @Override
    public List<String> findAll() {
        Result result = executor.query("match (node1:Word)-[r:EMBEDDED_IN]->(node2:Domain) return r", new HashMap<>());

        List<String> relations = new ArrayList<>();

        Iterator<Map<String, Object>> it = result.queryResults().iterator();
        while(it.hasNext()){
            try {
                Map values = (Map) it.next().get("r");
                EmbeddedIn relation = new EmbeddedIn();
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
        EmbeddedIn edge = relation.asEmbeddedIn();


        Map<String,Object> params = new HashMap<>();
        params.put("0",edge.getStartUri());
        params.put("1",edge.getEndUri());
        params.put("2",edge.getUri());
        params.put("3",edge.getCreationTime());
        params.put("4",edge.getVector());
        params.put("5",edge.getWeight());

        LOG.trace("Trying to create EMBEDDED_IN relation");
        try{
            QueryStatistics result = executor.execute("MATCH (a:Word),(b:Domain) WHERE a.uri = {0} AND b.uri = {1} CREATE (a)-[r:EMBEDDED_IN { uri : {2}, creationTime : {3}, vector : {4}, weight: {5}}]->(b) RETURN r", params);
            LOG.debug("created EMBEDDED_IN relation: -> " +result.getRelationshipsCreated());
        }catch (Exception e){
            LOG.error("Error on EMBEDDED_IN",e);
        }
    }

}
