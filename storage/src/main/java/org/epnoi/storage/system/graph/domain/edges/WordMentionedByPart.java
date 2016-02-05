package org.epnoi.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.RelationProperties;
import org.epnoi.model.domain.Resource;
import org.epnoi.storage.system.graph.domain.nodes.PartNode;
import org.epnoi.storage.system.graph.domain.nodes.WordNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="MENTIONS")
@Data
@EqualsAndHashCode(of={"part","word"},callSuper = false)
@ToString(of = {"id"},callSuper = true)
public class WordMentionedByPart extends Edge {
    @GraphId
    private Long id;

    @StartNode
    private PartNode part;

    @EndNode
    private WordNode word;

    @Property
    private Long times = 0L;

    @Override
    public void setStart(Resource start) {
        this.part = (PartNode) start;
    }

    @Override
    public void setEnd(Resource end) {
        this.word = (WordNode) end;
    }

    @Override
    public Resource getStart() {
        return this.part;
    }

    @Override
    public Resource getEnd() {
        return this.word;
    }

    @Override
    public void setProperties(RelationProperties properties) {
        this.times = properties.getTimes();
    }

    @Override
    public Double getWeight() {
        return times.doubleValue();
    }
}
