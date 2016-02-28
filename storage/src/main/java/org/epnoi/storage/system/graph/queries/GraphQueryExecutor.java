package org.epnoi.storage.system.graph.queries;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class GraphQueryExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(GraphQueryExecutor.class);

    @Autowired
    Session session;

    Neo4jTemplate template;

    @PostConstruct
    public void setup(){
        this.template = new Neo4jTemplate(session);
    }


    public Result execute(String query, Map<String, ?> parameters){
        return template.query(query,parameters);
    }
}
