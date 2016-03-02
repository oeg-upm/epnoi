package org.epnoi.storage.system.graph.queries;

import org.apache.commons.beanutils.BeanUtils;
import org.epnoi.model.domain.relations.DealsWithFromItem;
import org.epnoi.model.domain.relations.DealsWithFromPart;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.system.graph.domain.nodes.ItemNode;
import org.epnoi.storage.system.graph.domain.nodes.PartNode;
import org.epnoi.storage.system.graph.domain.nodes.TopicNode;
import org.neo4j.ogm.session.result.QueryStatistics;
import org.neo4j.ogm.session.result.Result;
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
public class DealsPartGraphQuery implements GraphQuery<DealsWithFromPart> {

    private static final Logger LOG = LoggerFactory.getLogger(DealsPartGraphQuery.class);

    @Autowired
    GraphQueryExecutor executor;

    @Override
    public Relation.Type accept() {
        return Relation.Type.DEALS_WITH_FROM_PART;
    }

    @Override
    public List<DealsWithFromPart> query(String startUri, String endUri) {
        Map<String,String> params = new HashMap<>();
        params.put("0",startUri);
        params.put("1",endUri);
        Result result = executor.query("match (node1:Part{uri:{0}})-[r:DEALS_WITH]->(node2:Topic{uri:{1}}) return r", params);

        List<DealsWithFromPart> similars = new ArrayList<>();

        Iterator<Map<String, Object>> it = result.queryResults().iterator();
        while(it.hasNext()){
            try {
                Map values = (Map) it.next().get("r");
                DealsWithFromPart similarTo = new DealsWithFromPart();
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
    public List<DealsWithFromPart> inDomain(String uri) {
        Map<String,String> params = new HashMap<>();
        params.put("0",uri);
        Result result = executor.query("match (domain:Domain{uri:{0}})-[c:CONTAINS]->(d:Document)-[:BUNDLES]->(i:Item)<-[:DESCRIBES]-(p:Part)-[r:DEALS_WITH]->(t:Topic) return r,p,t", params);

        List<DealsWithFromPart> deals = new ArrayList<>();

        Iterator<Map<String, Object>> it = result.queryResults().iterator();
        while(it.hasNext()){
            try {
                Map<String, Object> map = it.next();

                Map rValues = (Map) map.get("r");
                DealsWithFromPart dealsWith = new DealsWithFromPart();
                BeanUtils.populate(dealsWith,rValues);

                Map pValues = (Map) map.get("p");
                PartNode pNode = new PartNode();
                BeanUtils.populate(pNode,pValues);

                Map tValues = (Map) map.get("t");
                TopicNode tNode = new TopicNode();
                BeanUtils.populate(tNode,tValues);

                dealsWith.setStartUri(pNode.getUri());
                dealsWith.setEndUri(tNode.getUri());

                deals.add(dealsWith);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.error("Error getting deals_with for part relation from: " + uri ,e);
            }
        }
        return deals;
    }


    @Override
    public void deleteIn(Resource.Type type, String uri) {
        Map<String,Object> params = new HashMap<>();
        params.put("0",uri);

        String query;
        switch (type){
            case DOMAIN:
                query = "match (domain{uri:{0}})-[c:CONTAINS]->(d:Document)-[b:BUNDLES]->(i:Item)<-[de:DESCRIBES]-(p:Part)-[r:DEALS_WITH]->(t:Topic) delete r";
                break;
            default: query = "";
        }

        QueryStatistics result = executor.execute(query, params);
        LOG.info("Result of query execution ["+ query + "] is: " + result);
    }

    @Override
    public List<String> findAll() {
        Result result = executor.query("match (node1:Part)-[r:DEALS_WITH]->(node2:Topic) return r", new HashMap<>());

        List<String> relations = new ArrayList<>();

        Iterator<Map<String, Object>> it = result.queryResults().iterator();
        while(it.hasNext()){
            try {
                Map values = (Map) it.next().get("r");
                DealsWithFromPart relation = new DealsWithFromPart();
                BeanUtils.populate(relation,values);
                relations.add(relation.getUri());
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.error("Error getting all relations",e);
            }
        }
        return relations;
    }

}
