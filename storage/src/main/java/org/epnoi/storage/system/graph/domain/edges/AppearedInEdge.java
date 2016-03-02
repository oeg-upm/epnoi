package org.epnoi.storage.system.graph.domain.edges;

import lombok.*;
import org.epnoi.model.domain.relations.AppearedIn;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.system.graph.domain.nodes.DomainNode;
import org.epnoi.storage.system.graph.domain.nodes.Node;
import org.epnoi.storage.system.graph.domain.nodes.TermNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="APPEARED_IN")
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@ToString(of = {"uri"},callSuper = true)
public class AppearedInEdge extends Edge<TermNode,DomainNode> {

    @Property
    private Long times;

    @Property
    private long subtermOf;

    @Property
    private long supertermOf;

    @Property
    private double cvalue;

    @Property
    private double consensus;

    @Property
    private double pertinence;

    @Property
    private double probability;

    @Property
    private double termhood;




    @Override
    public Resource.Type getStartType() {
        return Resource.Type.TERM;
    }

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.DOMAIN;
    }
}
