package org.epnoi.storage.system.graph.queries;

import org.epnoi.storage.actions.RepeatableActionExecutor;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.result.QueryStatistics;
import org.neo4j.ogm.session.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jTemplate;
import org.springframework.stereotype.Component;

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


    public QueryStatistics execute(String query, Map<String, Object> parameters){
        Optional<Object> result = performRetries(0, query, () -> template.execute(query, parameters));
        return (result.isPresent())? (QueryStatistics) result.get() : null;
    }

}
