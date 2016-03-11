package org.epnoi.storage.system.graph.template.edges;

import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class SimilarItemEdgeTemplate extends EdgeTemplate {

    public SimilarItemEdgeTemplate() {
        super(Relation.Type.SIMILAR_TO_ITEMS);
    }

    @Override
    protected String pathBy(Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Item)-[r:SIMILAR_TO]->(e:Item)";
            case DOMAIN:        return "(dom:Domain{uri:{0}})-[:CONTAINS]->(doc:Document)-[:BUNDLES]->(s:Item)-[r:SIMILAR_TO]->(e:Item)";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(Relation.Type type) {
        switch (type){
            case SIMILAR_TO_ITEMS:      return "(s:Item{uri:{0}})-[r:SIMILAR_TO]->(e:Item{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation).add("domain",relation.asSimilarToItems().getDomain());
    }
}
