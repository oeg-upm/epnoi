package org.epnoi.storage.system.graph.template;

import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.relations.SimilarToDocuments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class SimilarDocTemplateGraph extends TemplateGraph<SimilarToDocuments> {

    public SimilarDocTemplateGraph() {
        super(Relation.Type.SIMILAR_TO_DOCUMENTS);
    }

    @Override
    protected String simplePath() {
        return "(s:Document{uri:{0}})-[r:SIMILAR_TO]->(e:Document{uri:{1}})";
    }

    @Override
    protected String byDomainPath() {
        return "(d:Domain{uri:{0}})-[:CONTAINS]->(s:Document)-[r:SIMILAR_TO]->(e:Document)";
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation).add("domain",relation.asSimilarToDocuments().getDomain());
    }
}
