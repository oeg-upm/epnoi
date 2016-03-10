package org.epnoi.storage.system.graph.template;

import org.epnoi.model.domain.relations.Bundles;
import org.epnoi.model.domain.relations.Describes;
import org.epnoi.model.domain.relations.Relation;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class DescribesTemplateGraph extends TemplateGraph<Describes> {

    public DescribesTemplateGraph() {
        super(Relation.Type.DESCRIBES);
    }

    @Override
    protected String simplePath() {
        return "(s:Part{uri:{0}})-[r:DESCRIBES]->(e:Item{uri:{1}})";
    }

    @Override
    protected String byDomainPath() {
        return "(d:Domain{uri:{0}})-[:CONTAINS]->(doc:Document)-[:BUNDLES]->(e:Item)<-[r:DESCRIBES]-(s:part)";
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation);
    }


}
