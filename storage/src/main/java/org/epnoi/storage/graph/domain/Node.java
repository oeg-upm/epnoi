package org.epnoi.storage.graph.domain;

import lombok.Data;
import org.epnoi.model.Resource;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;

/**
 * Created by cbadenes on 02/02/16.
 */
@Data
public class Node extends Resource {

    @GraphId
    Long id;

    @Index(unique = true)
    String uri;

}
