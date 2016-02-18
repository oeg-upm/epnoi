package org.epnoi.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.system.graph.domain.nodes.ItemNode;
import org.epnoi.storage.system.graph.domain.nodes.PartNode;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.util.List;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="EMBEDDED_IN")
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@ToString(of = {"id","uri"},callSuper = true)
public class EmbeddedInEdge extends Edge<PartNode,ItemNode> {

    @Property
    private float[] vector;

    @Override
    public Resource.Type getStartType() {
        return Resource.Type.PART;
    }

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.ITEM;
    }
}
