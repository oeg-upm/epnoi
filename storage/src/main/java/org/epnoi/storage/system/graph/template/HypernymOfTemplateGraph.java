package org.epnoi.storage.system.graph.template;

import org.epnoi.model.domain.relations.HypernymOf;
import org.epnoi.model.domain.relations.Relation;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class HypernymOfTemplateGraph extends TemplateGraph<HypernymOf> {

    public HypernymOfTemplateGraph() {
        super(Relation.Type.HYPERNYM_OF);
    }

    @Override
    protected String simplePath() {
        return "(s:Term{uri:{0}})-[r:HYPERNYM_OF]->(e:Term{uri:{1}})";
    }

    @Override
    protected String byDomainPath() {
        return "(d:Domain{uri:{0}})<-[:APPEARED_IN]-(e:Term)<-[r:HYPERNYM_OF]-(s:TERM)-[:APPEARED_IN]->(d:Domain{uri:{0}})";
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation).
                add("domain",relation.asHypernymOf().getDomain());
    }


}
