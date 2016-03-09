package org.epnoi.storage.system.graph.queries;

import org.apache.commons.beanutils.BeanUtils;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.relations.SimilarToDocuments;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.system.graph.domain.nodes.DocumentNode;
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
public class SimilarDocGraphQuery implements GraphQuery<SimilarToDocuments> {

    private static final Logger LOG = LoggerFactory.getLogger(SimilarDocGraphQuery.class);

    @Autowired
    GraphQueryExecutor executor;

    @Override
    public Relation.Type accept() {
        return Relation.Type.SIMILAR_TO_DOCUMENTS;
    }

    @Override
    public List<SimilarToDocuments> query(String startUri, String endUri) {
        Map<String,String> params = new HashMap<>();
        params.put("0",startUri);
        params.put("1",endUri);
        Result result = executor.query("match (node1:Document{uri:{0}})-[r:SIMILAR_TO]->(node2:Document{uri:{1}}) return r", params);

        List<SimilarToDocuments> similars = new ArrayList<>();

        Iterator<Map<String, Object>> it = result.queryResults().iterator();
        while(it.hasNext()){
            try {
                Map values = (Map) it.next().get("r");
                SimilarToDocuments similarTo = new SimilarToDocuments();
                BeanUtils.populate(similarTo,values);
                similarTo.setStartUri(startUri);
                similarTo.setEndUri(endUri);
                similars.add(similarTo);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.error("Error getting similar_to relation between: " + startUri +" and " + endUri,e);
            }
        }
        return similars;
    }

    @Override
    public List<SimilarToDocuments> inDomain(String uri) {
        Map<String,String> params = new HashMap<>();
        params.put("0",uri);
        Result result = executor.query("match (d:Domain{uri:{0}})-[:CONTAINS]->(d1:Document)-[r:SIMILAR_TO]->(d2:Document) return r,d1,d2", params);

        List<SimilarToDocuments> relations = new ArrayList<>();

        Iterator<Map<String, Object>> it = result.queryResults().iterator();
        while(it.hasNext()){
            try {
                Map<String, Object> map = it.next();

                Map rValues = (Map) map.get("r");
                SimilarToDocuments relation = new SimilarToDocuments();
                BeanUtils.populate(relation,rValues);

                Map sValues = (Map) map.get("d1");
                DocumentNode sNode = new DocumentNode();
                BeanUtils.populate(sNode,sValues);

                Map eValues = (Map) map.get("d2");
                DocumentNode eNode = new DocumentNode();
                BeanUtils.populate(eNode,eValues);

                relation.setStartUri(sNode.getUri());
                relation.setEndUri(eNode.getUri());

                relations.add(relation);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.error("Error getting similar_to relations btw documents from: " + uri ,e);
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
                query = "match (domain{uri:{0}})-[c:CONTAINS]->(d1:Document)-[r:SIMILAR_TO]->(d2:Document) delete r";
                break;
            default: query = "";
        }

        QueryStatistics result = executor.execute(query, params);
        LOG.info("Result of query execution ["+ query + "] is: " + result);
    }

    @Override
    public List<String> findAll() {
        Result result = executor.query("match (node1:Document)-[r:SIMILAR_TO]->(node2:Document) return r", new HashMap<>());

        List<String> relations = new ArrayList<>();

        Iterator<Map<String, Object>> it = result.queryResults().iterator();
        while(it.hasNext()){
            try {
                Map values = (Map) it.next().get("r");
                SimilarToDocuments relation = new SimilarToDocuments();
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
        LOG.error("Not implemented yet");
    }
}
