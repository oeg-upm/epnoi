package org.epnoi.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.relations.Composes;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.system.graph.domain.nodes.*;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="COMPOSES")
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@ToString(of = {"id","uri"},callSuper = true)
public class ComposesEdge extends Edge<SourceNode,DomainNode> {

    @Override
    public Resource.Type getStartType() {
        return Resource.Type.SOURCE;
    }

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.DOMAIN;
    }
}
