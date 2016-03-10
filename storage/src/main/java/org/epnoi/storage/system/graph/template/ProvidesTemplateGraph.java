package org.epnoi.storage.system.graph.template;

import org.epnoi.model.domain.relations.Composes;
import org.epnoi.model.domain.relations.Provides;
import org.epnoi.model.domain.relations.Relation;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class ProvidesTemplateGraph extends TemplateGraph<Provides> {

    public ProvidesTemplateGraph() {
        super(Relation.Type.PROVIDES);
    }

    @Override
    protected String simplePath() {
        return "(s:Source{uri:{0}})-[r:PROVIDES]->(e:Document{uri:{1}})";
    }

    @Override
    protected String byDomainPath() {
        return "(d:Domain{uri:{0}})-[:CONTAINS]->(e:Document)<-[r:PROVIDES]-(s:Source)";
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation);
    }


}
