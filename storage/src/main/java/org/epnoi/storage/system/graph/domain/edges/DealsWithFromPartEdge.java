package org.epnoi.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.relations.DealsWithFromPart;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.system.graph.domain.nodes.*;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="DEALS_WITH")
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@ToString(of = {"uri"},callSuper = true)
public class DealsWithFromPartEdge extends Edge<PartNode,TopicNode> {


    @Override
    public Resource.Type getStartType() {
        return Resource.Type.PART;
    }

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.TOPIC;
    }
}
