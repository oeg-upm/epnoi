package org.epnoi.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.system.graph.domain.nodes.ItemNode;
import org.epnoi.storage.system.graph.domain.nodes.PartNode;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="DESCRIBES")
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@ToString(of = {"uri"},callSuper = true)
public class DescribesEdge extends Edge<PartNode,ItemNode> {


    @Override
    public Resource.Type getStartType() {
        return Resource.Type.PART;
    }

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.ITEM;
    }
}
