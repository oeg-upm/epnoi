package org.epnoi.storage.system.graph.domain.nodes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.Relation;
import org.epnoi.model.domain.Resource;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;

/**
 * Created by cbadenes on 02/02/16.
 */
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = false)
@ToString(of={"uri"}, callSuper = true)
public abstract class Node extends Resource {

    @GraphId
    Long id;

    @Index(unique = true)
    String uri;

    public abstract void add(Relation relation, Relation.Type type);

    public abstract void remove(Relation relation, Relation.Type type);

}
