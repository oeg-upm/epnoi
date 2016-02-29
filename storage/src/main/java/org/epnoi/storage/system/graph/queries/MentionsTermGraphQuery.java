package org.epnoi.storage.system.graph.queries;

import org.apache.commons.beanutils.BeanUtils;
import org.epnoi.model.domain.relations.MentionsFromTerm;
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
public class MentionsTermGraphQuery implements GraphQuery<MentionsFromTerm> {

    private static final Logger LOG = LoggerFactory.getLogger(MentionsTermGraphQuery.class);

    @Autowired
    GraphQueryExecutor executor;

    @Override
    public Relation.Type accept() {
        return Relation.Type.MENTIONS_FROM_TERM;
    }

    @Override
    public List<MentionsFromTerm> query(String startUri, String endUri) {
        Map<String,String> params = new HashMap<>();
        params.put("0",startUri);
        params.put("1",endUri);
        Result result = executor.query("match (node1:Term{uri:{0}})-[r:MENTIONS]->(node2:Word{uri:{1}}) return r", params);

        List<MentionsFromTerm> similars = new ArrayList<>();

        Iterator<Map<String, Object>> it = result.queryResults().iterator();
        while(it.hasNext()){
            try {
                Map values = (Map) it.next().get("r");
                MentionsFromTerm similarTo = new MentionsFromTerm();
                BeanUtils.populate(similarTo,values);
                similarTo.setStartUri(startUri);
                similarTo.setEndUri(endUri);
                similars.add(similarTo);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.error("Error getting mentions relation between: " + startUri +" and " + endUri,e);
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
                query = "match (domain{uri:{0}})<-[:APPEARED_IN]-(t:Term)-[r:MENTIONS]->(w:Word) delete r";
                break;
            default: query = "";
        }

        QueryStatistics result = executor.execute(query, params);
        LOG.info("Result of query execution ["+ query + "] is: " + result);
    }
}
