package org.epnoi.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.RelationProperties;
import org.epnoi.model.domain.Resource;
import org.epnoi.storage.system.graph.domain.nodes.ItemNode;
import org.epnoi.storage.system.graph.domain.nodes.TopicNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="DEALS_WITH")
@Data
@EqualsAndHashCode(of={"item","topic"},callSuper = false)
@ToString(of = {"id"},callSuper = true)
public class ItemDealsWithTopic extends Edge {
    @GraphId
    private Long id;

    @StartNode
    private ItemNode item;

    @EndNode
    private TopicNode topic;

    @Property
    private Double weight;

    @Override
    public void setStart(Resource start) {
        this.item = (ItemNode) start;
    }

    @Override
    public void setEnd(Resource end) {
        this.topic = (TopicNode) end;
    }

    @Override
    public Resource getStart() {
        return item;
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