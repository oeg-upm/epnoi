package org.epnoi.storage.system.graph.template;

import org.epnoi.model.domain.relations.DealsWithFromDocument;
import org.epnoi.model.domain.relations.Relation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class DealsDocTemplateGraph extends TemplateGraph<DealsWithFromDocument> {

    public DealsDocTemplateGraph() {
        super(Relation.Type.DEALS_WITH_FROM_DOCUMENT);
    }

    @Override
    protected String simplePath() {
        return "(s:Document{uri:{0}})-[r:DEALS_WITH]->(e:Topic{uri:{1}})";
    }

    @Override
    protected String byDomainPath() {
        return "(domain:Domain{uri:{0}})-[c:CONTAINS]->(s:Document)-[r:DEALS_WITH]->(e:Topic)";
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation);
    }
}
