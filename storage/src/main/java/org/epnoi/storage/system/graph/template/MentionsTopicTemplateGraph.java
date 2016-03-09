package org.epnoi.storage.system.graph.template;

import org.epnoi.model.domain.relations.MentionsFromTopic;
import org.epnoi.model.domain.relations.Relation;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class MentionsTopicTemplateGraph extends TemplateGraph<MentionsFromTopic> {

    public MentionsTopicTemplateGraph() {
        super(Relation.Type.MENTIONS_FROM_TOPIC);
    }

    @Override
    protected String simplePath() {
        return "(s:Topic{uri:{0}})-[r:MENTIONS]->(e:Word{uri:{1}})";
    }

    @Override
    protected String byDomainPath() {
        return "(d:Domain{uri:{0}})<-[:EMERGES_IN]-(s:Topic)-[r:MENTIONS]->(e:Word)";
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation).add("times",relation.asMentionsFromTopic().getTimes());
    }
}
