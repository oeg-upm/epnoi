package org.epnoi.storage.system.graph.domain.relationships;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.Relation;
import org.epnoi.model.domain.Resource;
import org.epnoi.storage.system.graph.domain.DomainNode;
import org.epnoi.storage.system.graph.domain.WordNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="EMBEDDED_IN")
@Data
@EqualsAndHashCode(of={"word","domain"},callSuper = false)
@ToString(of = {"id"},callSuper = true)
public class DomainInWord extends Relation {
    @GraphId
    private Long id;

    @StartNode
    private WordNode word;

    @EndNode
    private DomainNode domain;

    @Property
    private String vector;

    @Override
    public void setStart(Resource start) {
        this.word = (WordNode) start;
    }

    @Override
    public void setEnd(Resource end) {
        this.domain = (DomainNode) end;
    }

    @Override
    public Resource getStart() {
        return word;
    }

    @Override
    public Resource getEnd() {
        return domain;
    }

    @Override
    public void setProperties(Properties properties) {
        this.vector = properties.getDescription();
    }
}
