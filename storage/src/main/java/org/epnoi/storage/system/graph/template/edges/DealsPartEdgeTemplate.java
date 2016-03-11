package org.epnoi.storage.system.graph.template.edges;

import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class DealsPartEdgeTemplate extends EdgeTemplate {

    public DealsPartEdgeTemplate() {
        super(Relation.Type.DEALS_WITH_FROM_PART);
    }

    @Override
    protected String pathBy(Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Part)-[r:DEALS_WITH]->(e:Topic)";
            case DOMAIN:        return "(domain:Domain{uri:{0}})-[c:CONTAINS]->(d:Document)-[:BUNDLES]->(i:Item)<-[:DESCRIBES]-(s:Part)-[r:DEALS_WITH]->(e:Topic)";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(Relation.Type type) {
        switch (type){
            case DEALS_WITH_FROM_PART:      return "(s:Part{uri:{0}})-[r:DEALS_WITH]->(e:Topic{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation);
    }

}
