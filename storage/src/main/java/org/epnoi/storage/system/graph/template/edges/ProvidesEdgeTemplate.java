package org.epnoi.storage.system.graph.template.edges;

import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class ProvidesEdgeTemplate extends EdgeTemplate {

    public ProvidesEdgeTemplate() {
        super(Relation.Type.PROVIDES);
    }

    @Override
    protected String pathBy(Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Source)-[r:PROVIDES]->(e:Document)";
            case DOMAIN:        return "(d:Domain{uri:{0}})-[:CONTAINS]->(e:Document)<-[r:PROVIDES]-(s:Source)";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(Relation.Type type) {
        switch (type){
            case PROVIDES:      return "(s:Source{uri:{0}})-[r:PROVIDES]->(e:Document{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation);
    }


}
