package org.epnoi.storage.system.graph.queries;

import org.apache.http.client.HttpResponseException;
import org.epnoi.storage.actions.RepeatableActionExecutor;
import org.neo4j.ogm.exception.ResultProcessingException;
import org.neo4j.ogm.model.QueryStatistics;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class GraphQueryExecutor extends RepeatableActionExecutor{

    private static final Logger LOG = LoggerFactory.getLogger(GraphQueryExecutor.class);

    @Autowired
    Session session;

    Neo4jTemplate template;

    @PostConstruct
    public void setup(){
        this.template = new Neo4jTemplate(session);
    }


    public Result query(String query, Map<String, ?> parameters){
        Optional<Object> result = performRetries(0, query, () -> template.query(query, parameters));
        return (result.isPresent())? (Result) result.get() : null;
    }


//    @Transactional
    public QueryStatistics execute(String query, Map<String, Object> parameters){
        Optional<Object> result = performRetries(0, query, () -> {
            template.clear();
            QueryStatistics res = template.execute(query, parameters);
            if (!res.containsUpdates()) {
                LOG.error("No operation done!");
                throw new ResultProcessingException("No contains updates", new HttpResponseException(404,"Not found"));
            }
            return res;
//            try {
//            }catch (Exception e){
//                LOG.error("Error on execution: " + query,e);
//                throw e;
//            }
        });
        if (!result.isPresent()) throw new RuntimeException("Operation not completed");
        return (QueryStatistics) result.get();
    }

}
