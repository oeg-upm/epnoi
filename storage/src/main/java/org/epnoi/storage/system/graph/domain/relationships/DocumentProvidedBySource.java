package org.epnoi.storage.system.graph.domain.relationships;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.Relation;
import org.epnoi.model.domain.Resource;
import org.epnoi.storage.system.graph.domain.DocumentNode;
import org.epnoi.storage.system.graph.domain.SourceNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="PROVIDES")
@Data
@EqualsAndHashCode(of={"source","document"},callSuper = false)
@ToString(of = {"id"},callSuper = true)
public class DocumentProvidedBySource extends Relation {
    @GraphId
    private Long id;

    @StartNode
    private SourceNode source;

    @EndNode
    private DocumentNode document;

    @Property
    private String date;

    @Override
    public void setStart(Resource start) {
        this.source = (SourceNode) start;
    }

    @Override
    public void setEnd(Resource end) {
        this.document = (DocumentNode) end;
    }

    @Override
    public Resource getStart() {
        return source;
    }

    @Override
    public Resource getEnd() {
        return document;
    }

    @Override
    public void setProperties(Properties properties) {
        this.date = properties.getDate();
    }
}
