package org.epnoi.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.relations.PairsWith;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.system.graph.domain.nodes.Node;
import org.epnoi.storage.system.graph.domain.nodes.TermNode;
import org.epnoi.storage.system.graph.domain.nodes.WordNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="PAIRS_WITH")
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@ToString(of = {"uri"},callSuper = true)
public class PairsWithEdge extends Edge<WordNode,WordNode> {

    @Property
    private String domain;

    @Override
    public Resource.Type getStartType() {
        return Resource.Type.WORD;
    }

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.WORD;
    }
}
