package org.epnoi.storage.system.graph.template;

import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.relations.SimilarToParts;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class SimilarPartTemplateGraph extends TemplateGraph<SimilarToParts> {

    public SimilarPartTemplateGraph() {
        super(Relation.Type.SIMILAR_TO_PARTS);
    }

    @Override
    protected String simplePath() {
        return "(s:Part{uri:{0}})-[r:SIMILAR_TO]->(e:Part{uri:{1}})";
    }

    @Override
    protected String byDomainPath() {
        return "(d:Domain{uri:{0}})-[:CONTAINS]->(d:Document)-[:BUNDLES]->(i:Item)<-[:DESCRIBES]-(s:Part)-[r:SIMILAR_TO]->(e:Part)";
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation).add("domain",relation.asSimilarToParts().getDomain());
    }

}
