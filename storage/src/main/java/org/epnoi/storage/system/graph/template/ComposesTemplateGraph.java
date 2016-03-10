package org.epnoi.storage.system.graph.template;

import org.epnoi.model.domain.relations.Composes;
import org.epnoi.model.domain.relations.Contains;
import org.epnoi.model.domain.relations.Relation;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class ComposesTemplateGraph extends TemplateGraph<Composes> {

    public ComposesTemplateGraph() {
        super(Relation.Type.COMPOSES);
    }

    @Override
    protected String simplePath() {
        return "(s:Source{uri:{0}})-[r:COMPOSES]->(e:Domain{uri:{1}})";
    }

    @Override
    protected String byDomainPath() {
        return "(e:Domain{uri:{0}})<-[r:COMPOSES]-(s:Source)";
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation);
    }


}
