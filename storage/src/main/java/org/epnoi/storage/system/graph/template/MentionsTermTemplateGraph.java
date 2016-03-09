package org.epnoi.storage.system.graph.template;

import org.epnoi.model.domain.relations.MentionsFromTerm;
import org.epnoi.model.domain.relations.Relation;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class MentionsTermTemplateGraph extends TemplateGraph<MentionsFromTerm> {

    public MentionsTermTemplateGraph() {
        super(Relation.Type.MENTIONS_FROM_TERM);
    }

    @Override
    protected String simplePath() {
        return "(s:Term{uri:{0}})-[r:MENTIONS]->(e:Word{uri:{1}})";
    }

    @Override
    protected String byDomainPath() {
        return "(d:Domain{uri:{0}})<-[:APPEARED_IN]-(s:Term)-[r:MENTIONS]->(e:Word)";
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation).add("times",relation.asMentionsFromTerm().getTimes());
    }

}
