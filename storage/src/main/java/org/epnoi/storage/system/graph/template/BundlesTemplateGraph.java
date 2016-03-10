package org.epnoi.storage.system.graph.template;

import org.epnoi.model.domain.relations.Bundles;
import org.epnoi.model.domain.relations.Relation;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class BundlesTemplateGraph extends TemplateGraph<Bundles> {

    public BundlesTemplateGraph() {
        super(Relation.Type.BUNDLES);
    }

    @Override
    protected String simplePath() {
        return "(s:Document{uri:{0}})-[r:BUNDLES]->(e:Item{uri:{1}})";
    }

    @Override
    protected String byDomainPath() {
        return "(d:Domain{uri:{0}})-[:CONTAINS]->(s:Document)-[r:BUNDLES]->(e:Word)";
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation);
    }


}
