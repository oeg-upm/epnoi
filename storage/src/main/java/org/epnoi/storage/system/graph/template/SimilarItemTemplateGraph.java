package org.epnoi.storage.system.graph.template;

import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.relations.SimilarToItems;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class SimilarItemTemplateGraph extends TemplateGraph<SimilarToItems> {

    public SimilarItemTemplateGraph() {
        super(Relation.Type.SIMILAR_TO_ITEMS);
    }

    @Override
    protected String simplePath() {
        return "(s:Item{uri:{0}})-[r:SIMILAR_TO]->(e:Item{uri:{1}})";
    }

    @Override
    protected String byDomainPath() {
        return "(d:Domain{uri:{0}})-[:CONTAINS]->(d:Document)-[:BUNDLES]->(s:Item)-[r:SIMILAR_TO]->(e:Item)";
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation).add("domain",relation.asSimilarToItems().getDomain());
    }
}
