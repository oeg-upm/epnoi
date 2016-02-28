package org.epnoi.storage.system.graph.queries;

import org.epnoi.model.domain.relations.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class GraphQueryFactory {

    @Autowired
    List<GraphQuery> graphQueries;

    Map<Relation.Type, GraphQuery> queryTable;

    @PostConstruct
    public void setup(){
        queryTable = new HashMap<>();
        for (GraphQuery graphQuery : graphQueries){
            queryTable.put(graphQuery.accept(),graphQuery);
        }
    }

    public GraphQuery of(Relation.Type type){
        return queryTable.get(type);
    }

    public boolean handle(Relation.Type type){
        return queryTable.containsKey(type);
    }
}
