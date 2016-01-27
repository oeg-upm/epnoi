package org.epnoi.storage.graph.domain.relationships;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.epnoi.storage.graph.domain.DocumentNode;
import org.epnoi.storage.graph.domain.DomainNode;
import org.epnoi.storage.graph.domain.SourceNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="COMPOSES")
@Data
@EqualsAndHashCode(exclude={"id"})
public class DomainComposedBySource {
    @GraphId
    private Long id;

    @StartNode
    private SourceNode source;

    @EndNode
    private DomainNode domain;

    @Property
    private String date;
}
