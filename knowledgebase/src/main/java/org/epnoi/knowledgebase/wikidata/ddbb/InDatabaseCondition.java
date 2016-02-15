package org.epnoi.knowledgebase.wikidata.ddbb;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by cbadenes on 15/02/16.
 */
public class InDatabaseCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return !Boolean.valueOf(conditionContext.getEnvironment().getProperty("epnoi.knowledgeBase.wikidata.inMemory"));
    }
}
