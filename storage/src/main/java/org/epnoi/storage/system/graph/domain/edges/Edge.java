package org.epnoi.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.Relation;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;

/**
 * Created by cbadenes on 02/02/16.
 */
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = false)
@ToString(of={"uri"}, callSuper = true)
public abstract class Edge extends Relation {

    @GraphId
    Long id;

    @Index(unique = true)
    String uri;

}
