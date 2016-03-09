package org.epnoi.storage.system.graph.template;

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
    List<TemplateGraph> graphQueries;

    Map<Relation.Type, TemplateGraph> queryTable;

    @PostConstruct
    public void setup(){
        queryTable = new HashMap<>();
        for (TemplateGraph templateGraph : graphQueries){
            queryTable.put(templateGraph.accept(), templateGraph);
        }
    }

    public TemplateGraph of(Relation.Type type){
        return queryTable.get(type);
    }

    public boolean handle(Relation.Type type){
        return queryTable.containsKey(type);
    }
}
