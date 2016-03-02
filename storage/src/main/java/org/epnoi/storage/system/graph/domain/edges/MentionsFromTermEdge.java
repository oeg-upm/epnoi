package org.epnoi.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.system.graph.domain.nodes.TermNode;
import org.epnoi.storage.system.graph.domain.nodes.WordNode;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="MENTIONS")
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@ToString(of = {"uri"},callSuper = true)
public class MentionsFromTermEdge extends Edge<TermNode,WordNode> {

    @Property
    private Long times;

    @Override
    public Resource.Type getStartType() {
        return Resource.Type.TERM;
    }

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.WORD;
    }
}
