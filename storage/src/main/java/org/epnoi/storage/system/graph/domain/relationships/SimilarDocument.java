package org.epnoi.storage.system.graph.domain.relationships;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.Relation;
import org.epnoi.model.domain.Resource;
import org.epnoi.storage.system.graph.domain.DocumentNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="SIMILAR_TO")
@Data
@EqualsAndHashCode(of={"x","y"},callSuper = false)
@ToString(of = {"id"},callSuper = true)
public class SimilarDocument extends Relation {
    @GraphId
    private Long id;

    @StartNode
    private DocumentNode x;

    @EndNode
    private DocumentNode y;

    @Property
    private Double weight;

    @Property
    private String domain;

    @Override
    public void setStart(Resource start) {
        this.x = (DocumentNode) start;
    }

    @Override
    public void setEnd(Resource end) {
        this.y = (DocumentNode) end;
    }

    @Override
    public Resource getStart() {
        return x;
    }

    @Override
    public Resource getEnd() {
        return y;
    }

    @Override
    public void setProperties(Properties properties) {
        this.weight = properties.getWeight();
        this.domain = properties.getDomain();
    }
}
