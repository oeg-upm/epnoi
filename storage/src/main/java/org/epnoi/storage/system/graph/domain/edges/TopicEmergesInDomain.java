package org.epnoi.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.RelationProperties;
import org.epnoi.model.domain.Resource;
import org.epnoi.storage.system.graph.domain.nodes.DomainNode;
import org.epnoi.storage.system.graph.domain.nodes.TopicNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="EMERGES_IN")
@Data
@EqualsAndHashCode(of={"topic","domain"},callSuper = false)
@ToString(of = {"id"},callSuper = true)
public class TopicEmergesInDomain extends Edge {
    @GraphId
    private Long id;

    @StartNode
    private TopicNode topic;

    @EndNode
    private DomainNode domain;

    @Property
    private String date;

    @Property
    private String analysis;

    @Override
    public void setStart(Resource start) {
        this.topic = (TopicNode) start;
    }

    @Override
    public void setEnd(Resource end) {
        this.domain = (DomainNode) end;
    }

    @Override
    public Resource getStart() {
        return topic;
    }

    @Override
    public Resource getEnd() {
        return domain;
    }

    @Override
    public void setProperties(RelationProperties properties) {
        this.date = properties.getDate();
        this.analysis = properties.getDescription();
    }

    @Override
    public Double getWeight() {
        return 0.0;
    }
}
