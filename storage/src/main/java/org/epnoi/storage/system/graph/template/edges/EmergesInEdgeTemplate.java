package org.epnoi.storage.system.graph.template.edges;

import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class EmergesInEdgeTemplate extends EdgeTemplate {

    public EmergesInEdgeTemplate() {
        super(Relation.Type.EMERGES_IN);
    }

    @Override
    protected String pathBy(Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Topic)-[r:EMERGES_IN]->(e:Domain)";
            case DOMAIN:        return "(e:Domain{uri:{0}})<-[r:EMERGES_IN]-(s:Topic)";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(Relation.Type type) {
        switch (type){
            case EMERGES_IN:      return "(s:Topic{uri:{0}})-[r:EMERGES_IN]->(e:Domain{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation).add("analysis",relation.asEmergesIn().getAnalysis());
    }


}
