package org.epnoi.storage.system.graph.template;

import org.epnoi.model.domain.relations.EmbeddedIn;
import org.epnoi.model.domain.relations.EmergesIn;
import org.epnoi.model.domain.relations.Relation;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class EmergesInTemplateGraph extends TemplateGraph<EmergesIn> {

    public EmergesInTemplateGraph() {
        super(Relation.Type.EMERGES_IN);
    }

    @Override
    protected String simplePath() {
        return "(s:Topic{uri:{0}})-[r:EMERGES_IN]->(e:Domain{uri:{1}})";
    }

    @Override
    protected String byDomainPath() {
        return "(e:Domain{uri:{0}})<-[r:EMERGES_IN]-(s:Topic)";
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation).add("analysis",relation.asEmergesIn().getAnalysis());
    }


}
