package org.epnoi.storage.system.graph.template;

import org.epnoi.model.domain.relations.DealsWithFromPart;
import org.epnoi.model.domain.relations.Relation;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class DealsPartTemplateGraph extends TemplateGraph<DealsWithFromPart> {

    public DealsPartTemplateGraph() {
        super(Relation.Type.DEALS_WITH_FROM_PART);
    }

    @Override
    protected String simplePath() {
        return "(s:Part{uri:{0}})-[r:DEALS_WITH]->(e:Topic{uri:{1}})";
    }

    @Override
    protected String byDomainPath() {
        return "(domain:Domain{uri:{0}})-[c:CONTAINS]->(d:Document)-[:BUNDLES]->(i:Item)<-[:DESCRIBES]-(s:Part)-[r:DEALS_WITH]->(e:Topic)";
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation);
    }

}
