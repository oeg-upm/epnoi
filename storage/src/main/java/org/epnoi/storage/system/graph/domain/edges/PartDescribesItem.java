package org.epnoi.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.RelationProperties;
import org.epnoi.model.domain.Resource;
import org.epnoi.storage.system.graph.domain.nodes.ItemNode;
import org.epnoi.storage.system.graph.domain.nodes.PartNode;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="DESCRIBES")
@Data
@EqualsAndHashCode(of={"part","item"},callSuper = false)
@ToString(of = {"id"},callSuper = true)
public class PartDescribesItem extends Edge {
    @GraphId
    private Long id;

    @StartNode
    private PartNode part;

    @EndNode
    private ItemNode item;

    private String date;

    @Override
    public void setStart(Resource start) {
       this.part = (PartNode) start;
    }

    @Override
    public void setEnd(Resource end) {
        this.item = (ItemNode) end;
    }

    @Override
    public Resource getStart() {
        return part;
    }

    @Override
    public Resource getEnd() {
        return item;
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
