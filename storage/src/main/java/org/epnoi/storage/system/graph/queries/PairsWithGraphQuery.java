package org.epnoi.storage.system.graph.queries;

import org.apache.commons.beanutils.BeanUtils;
import org.epnoi.model.domain.relations.PairsWith;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.system.graph.domain.nodes.WordNode;
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
public class PairsWithGraphQuery implements GraphQuery<PairsWith> {

    private static final Logger LOG = LoggerFactory.getLogger(PairsWithGraphQuery.class);

    @Autowired
    GraphQueryExecutor executor;

    @Override
    public Relation.Type accept() {
        return Relation.Type.PAIRS_WITH;
    }

    @Override
    public List<PairsWith> query(String startUri, String endUri) {
        Map<String,String> params = new HashMap<>();
        params.put("0",startUri);
        params.put("1",endUri);
        Result result = executor.query("match (node1:Word{uri:{0}})-[r:PAIRS_WITH]->(node2:Word{uri:{1}}) return r", params);

        List<PairsWith> similars = new ArrayList<>();

        Iterator<Map<String, Object>> it = result.queryResults().iterator();
        while(it.hasNext()){
            try {
                Map values = (Map) it.next().get("r");
                PairsWith pairsWith = new PairsWith();
                BeanUtils.populate(pairsWith,values);
                pairsWith.setStartUri(startUri);
                pairsWith.setEndUri(endUri);
                similars.add(pairsWith);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.error("Error getting similar_to relation between: " + startUri +" and " + endUri,e);
            }
        }
        return similars;
    }

    @Override
    public List<PairsWith> inDomain(String uri) {
        Map<String,String> params = new HashMap<>();
        params.put("0",uri);
        Result result = executor.query("match (d:Domain{uri:{0}})<-[:EMBEDDED_IN]-(w1:Word)-[r:PAIRS_WITH]->(w2:Word) return r,w1,w2", params);

        List<PairsWith> relations = new ArrayList<>();

        Iterator<Map<String, Object>> it = result.queryResults().iterator();
        while(it.hasNext()){
            try {
                Map<String, Object> map = it.next();

                Map rValues = (Map) map.get("r");
                PairsWith relation = new PairsWith();
                BeanUtils.populate(relation,rValues);

                Map sValues = (Map) map.get("w1");
                WordNode sNode = new WordNode();
                BeanUtils.populate(sNode,sValues);

                Map eValues = (Map) map.get("w2");
                WordNode eNode = new WordNode();
                BeanUtils.populate(eNode,eValues);

                relation.setStartUri(sNode.getUri());
                relation.setEndUri(eNode.getUri());

                relations.add(relation);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.error("Error getting pairs_with relations from: " + uri ,e);
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
                query = "match (domain{uri:{0}})<-[c:EMBEDDED_IN]-(w1:Word)-[r:PAIRS_WITH]->(w2:Word) delete r";
                break;
            default: query = "";
        }

        QueryStatistics result = executor.execute(query, params);
        LOG.info("Result of query execution ["+ query + "] is: " + result);
    }

    @Override
    public List<String> findAll() {
        Result result = executor.query("match (node1:Word)-[r:PAIRS_WITH]->(node2:Word) return r", new HashMap<>());

        List<String> relations = new ArrayList<>();

        Iterator<Map<String, Object>> it = result.queryResults().iterator();
        while(it.hasNext()){
            try {
                Map values = (Map) it.next().get("r");
                PairsWith relation = new PairsWith();
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
        PairsWith pairsWith = relation.asPairsWith();


        Map<String,Object> params = new HashMap<>();
        params.put("0",pairsWith.getStartUri());
        params.put("1",pairsWith.getEndUri());
        params.put("2",pairsWith.getUri());
        params.put("3",pairsWith.getCreationTime());
        params.put("4",pairsWith.getDomain());
        params.put("5",pairsWith.getWeight());

        Result result = executor.query("MATCH (a:Word),(b:Word) WHERE a.uri = {0} AND b.uri = {1} CREATE (a)-[r:PAIRS_WITH { uri : {2}, creationTime : {3}, domain : {4}, weight: {5}}]->(b) RETURN r", params);
        LOG.debug("Relation inserted",result.iterator().next());
    }
}
