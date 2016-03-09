package org.epnoi.storage.system.graph.template;

import org.epnoi.model.domain.relations.EmbeddedIn;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.utils.ResourceUtils;
import org.epnoi.storage.system.graph.GraphIdFactory;
import org.epnoi.storage.system.graph.repository.edges.UnifiedEdgeGraphRepositoryFactory;
import org.neo4j.ogm.model.QueryStatistics;
import org.neo4j.ogm.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class EmbeddedInTemplateGraph extends TemplateGraph<EmbeddedIn> {

    public EmbeddedInTemplateGraph() {
        super(Relation.Type.EMBEDDED_IN);
    }

    @Override
    protected String simplePath() {
        return "(s:Word{uri:{0}})-[r:EMBEDDED_IN]->(e:Domain{uri:{1}})";
    }

    @Override
    protected String byDomainPath() {
        return "(e:Domain{uri:{0}})<-[r:EMBEDDED_IN]-(s:Word)";
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation).add("vector",relation.asEmbeddedIn().getVector());
    }


}
