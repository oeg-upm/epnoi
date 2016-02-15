package org.epnoi.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.RelationProperties;
import org.epnoi.model.domain.Resource;
import org.epnoi.storage.system.graph.domain.nodes.DomainNode;
import org.epnoi.storage.system.graph.domain.nodes.TermNode;
import org.epnoi.storage.system.graph.domain.nodes.WordNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="APPEARED_IN")
@Data
@EqualsAndHashCode(of={"term","domain"},callSuper = false)
@ToString(of = {"id"},callSuper = true)
public class TermAppearedInDomain extends Edge {
    @GraphId
    private Long id;

    @StartNode
    private TermNode term;

    @EndNode
    private DomainNode domain;

    @Property
    private Double weight;

    @Property
    private Long times;

    @Override
    public void setStart(Resource start) {
        this.term = (TermNode) start;
    }

    @Override
    public void setEnd(Resource end) {
        this.domain = (DomainNode) end;
    }

    @Override
    public Resource getStart() {
        return term;
    }

    @Override
    public Resource getEnd() {
        return domain;
    }

    @Override
    public void setProperties(RelationProperties properties) {
        this.weight = properties.getWeight();
        this.times = properties.getTimes();
    }
}
