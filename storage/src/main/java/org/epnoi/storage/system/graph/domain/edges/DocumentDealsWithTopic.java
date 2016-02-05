package org.epnoi.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.RelationProperties;
import org.epnoi.model.domain.Resource;
import org.epnoi.storage.system.graph.domain.nodes.DocumentNode;
import org.epnoi.storage.system.graph.domain.nodes.TopicNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="DEALS_WITH")
@Data
@EqualsAndHashCode(of={"document","topic"},callSuper = false)
@ToString(of = {"id"},callSuper = true)
public class DocumentDealsWithTopic extends Edge {
    @GraphId
    private Long id;

    @StartNode
    private DocumentNode document;

    @EndNode
    private TopicNode topic;

    @Property
    private Double weight;

    @Override
    public void setStart(Resource start) {
        this.document = (DocumentNode) start;
    }

    @Override
    public void setEnd(Resource end) {
        this.topic = (TopicNode) end;
    }

    @Override
    public Resource getStart() {
        return document;
    }

    @Override
    public Resource getEnd() {
        return topic;
    }

    @Override
    public void setProperties(RelationProperties properties) {
        this.weight = properties.getWeight();
    }

    @Override
    public Double getWeight() {
        return weight;
    }
}
