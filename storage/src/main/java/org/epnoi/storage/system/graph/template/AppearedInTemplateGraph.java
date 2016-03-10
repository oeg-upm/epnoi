package org.epnoi.storage.system.graph.template;

import org.epnoi.model.domain.relations.AppearedIn;
import org.epnoi.model.domain.relations.EmbeddedIn;
import org.epnoi.model.domain.relations.Relation;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class AppearedInTemplateGraph extends TemplateGraph<AppearedIn> {

    public AppearedInTemplateGraph() {
        super(Relation.Type.APPEARED_IN);
    }

    @Override
    protected String simplePath() {
        return "(s:Term{uri:{0}})-[r:APPEARED_IN]->(e:Domain{uri:{1}})";
    }

    @Override
    protected String byDomainPath() {
        return "(e:Domain{uri:{0}})<-[r:APPEARED_IN]-(s:Term)";
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation).
                add("consensus",relation.asAppearedIn().getConsensus()).
                add("cvalue",relation.asAppearedIn().getCvalue()).
                add("endType",relation.asAppearedIn().getEndType()).
                add("pertinence",relation.asAppearedIn().getPertinence()).
                add("probability",relation.asAppearedIn().getProbability()).
                add("subtermOf",relation.asAppearedIn().getSubtermOf()).
                add("supertermOf",relation.asAppearedIn().getSupertermOf()).
                add("termhood",relation.asAppearedIn().getTermhood()).
                add("times",relation.asAppearedIn().getTimes())
                ;
    }


}
