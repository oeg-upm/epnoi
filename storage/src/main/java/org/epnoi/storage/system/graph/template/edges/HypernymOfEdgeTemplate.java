package org.epnoi.storage.system.graph.template.edges;

import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class HypernymOfEdgeTemplate extends EdgeTemplate {

    public HypernymOfEdgeTemplate() {
        super(Relation.Type.HYPERNYM_OF);
    }

    @Override
    protected String pathBy(Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Term)-[r:HYPERNYM_OF]->(e:Term)";
            case DOMAIN:        return "(dom1:Domain{uri:{0}})<-[app1:APPEARED_IN]-(e:Term)-[r:HYPERNYM_OF]->(s:Term)";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(Relation.Type type) {
        switch (type){
            case HYPERNYM_OF:      return "(s:Term{uri:{0}})-[r:HYPERNYM_OF]->(e:Term{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation).
                add("domain",relation.asHypernymOf().getDomain());
    }


}
