package org.epnoi.storage.system.graph.queries;

import org.apache.commons.beanutils.BeanUtils;
import org.epnoi.model.domain.relations.EmbeddedIn;
import org.epnoi.model.domain.relations.PairsWith;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
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
public class EmbeddedInGraphQuery implements GraphQuery<EmbeddedIn> {

    private static final Logger LOG = LoggerFactory.getLogger(EmbeddedInGraphQuery.class);

    @Autowired
    GraphQueryExecutor executor;

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
                Map values = (Map) it.next().get("r");
                EmbeddedIn pairsWith = new EmbeddedIn();
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
}
