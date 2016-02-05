package org.epnoi.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.RelationProperties;
import org.epnoi.model.domain.Resource;
import org.epnoi.storage.system.graph.domain.nodes.DocumentNode;
import org.epnoi.storage.system.graph.domain.nodes.DomainNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="CONTAINS")
@Data
@EqualsAndHashCode(of={"domain","document"},callSuper = false)
@ToString(of = {"id"},callSuper = true)
public class DomainContainsDocument extends Edge{

    @StartNode
    private DomainNode domain;

    @EndNode
    private DocumentNode document;

    @Property
    private String date;

    @Override
    public void setStart(Resource start) {
        this.domain = (DomainNode) start;
    }

    @Override
    public void setEnd(Resource end) {
        this.document = (DocumentNode) end;
    }

    @Override
    public Resource getStart() {
        return this.domain;
    }

    @Override
    public Resource getEnd() {
        return this.document;
    }

    @Override
    public void setProperties(RelationProperties properties) {
        this.date = properties.getDate();
    }

    @Override
    public Double getWeight() {
        return 0.0;
    }
}
