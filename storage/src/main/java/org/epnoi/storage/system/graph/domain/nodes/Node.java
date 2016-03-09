package org.epnoi.storage.system.graph.domain.nodes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.system.graph.domain.edges.Edge;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Property;

/**
 * Created by cbadenes on 02/02/16.
 */
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = false)
@ToString(of={"uri"}, callSuper = true)
public abstract class Node extends Resource {

    @GraphId
    Long id;

    @Property
    String uri;

    @Property
    String creationTime;


    public abstract void add(Edge  edge);

    public abstract void remove(Edge edge);

    public Resource.Type getResourceType(){
        return Type.ANY;
    }

}
