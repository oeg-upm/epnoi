package org.epnoi.storage.system.graph.template;

import org.epnoi.model.domain.relations.PairsWith;
import org.epnoi.model.domain.relations.Relation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class PairsWithTemplateGraph extends TemplateGraph<PairsWith> {

    public PairsWithTemplateGraph() {
        super(Relation.Type.PAIRS_WITH);
    }

    @Override
    protected String simplePath() {
        return "(s:Word{uri:{0}})-[r:PAIRS_WITH]->(e:Word{uri:{1}})";
    }

    @Override
    protected String byDomainPath() {
        return "(d:Domain{uri:{0}})<-[:EMBEDDED_IN]-(s:Word)-[r:PAIRS_WITH]->(e:Word)";
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation);
    }

}
