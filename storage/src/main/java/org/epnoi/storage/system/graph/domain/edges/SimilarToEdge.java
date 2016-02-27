package org.epnoi.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.system.graph.domain.nodes.ItemNode;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="SIMILAR_TO")
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@ToString(of = {"id","uri"},callSuper = true)
public class SimilarToEdge extends Edge<ItemNode,ItemNode> {

    @Property
    private String domain;


    @Override
    public Resource.Type getStartType() {
        return Resource.Type.ANY;
    }

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.ANY;
    }
}
