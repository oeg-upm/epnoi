package org.epnoi.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.RelationProperties;
import org.epnoi.model.domain.Resource;
import org.epnoi.storage.system.graph.domain.nodes.PartNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="SIMILAR_TO")
@Data
@EqualsAndHashCode(of={"x","y"},callSuper = false)
@ToString(of = {"id"},callSuper = true)
public class PartSimilarToPart extends Edge {
    @GraphId
    private Long id;

    @StartNode
    private PartNode x;

    @EndNode
    private PartNode y;

    @Property
    private Double weight;

    @Property
    private String domain;

    @Override
    public void setStart(Resource start) {
        this.x = (PartNode) start;
    }

    @Override
    public void setEnd(Resource end) {
        this.y = (PartNode) end;
    }

    @Override
    public Resource getStart() {
        return x;
    }

    @Override
    public Resource getEnd() {
        return y;
    }

    @Override
    public void setProperties(RelationProperties properties) {
        this.weight = properties.getWeight();
        this.domain = properties.getDomain();
    }

    @Override
    public Double getWeight() {
        return weight;
    }
}
