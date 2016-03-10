package org.epnoi.storage.system.graph.template;

import org.epnoi.model.domain.relations.Contains;
import org.epnoi.model.domain.relations.Describes;
import org.epnoi.model.domain.relations.Relation;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class ContainsTemplateGraph extends TemplateGraph<Contains> {

    public ContainsTemplateGraph() {
        super(Relation.Type.CONTAINS);
    }

    @Override
    protected String simplePath() {
        return "(s:Domain{uri:{0}})-[r:CONTAINS]->(e:Document{uri:{1}})";
    }

    @Override
    protected String byDomainPath() {
        return "(s:Domain{uri:{0}})-[r:CONTAINS]->(e:Document)";
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation);
    }


}
