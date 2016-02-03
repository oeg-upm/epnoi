package org.epnoi.storage.system.graph.domain.relationships;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.Relation;
import org.epnoi.model.domain.Resource;
import org.epnoi.storage.system.graph.domain.DocumentNode;
import org.epnoi.storage.system.graph.domain.DomainNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="CONTAINS")
@Data
@EqualsAndHashCode(of={"domain","document"},callSuper = false)
@ToString(of = {"id"},callSuper = true)
public class ContainedDocument extends Relation{
    @GraphId
    private Long id;

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
    public void setProperties(Properties properties) {
        this.date = properties.getDate();
    }
}
