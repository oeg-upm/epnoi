package org.epnoi.storage.system.graph.domain.relationships;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.Relation;
import org.epnoi.model.domain.Resource;
import org.epnoi.storage.system.graph.domain.TopicNode;
import org.epnoi.storage.system.graph.domain.WordNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="MENTIONS")
@Data
@EqualsAndHashCode(of={"topic","word"},callSuper = false)
@ToString(of = {"id"},callSuper = true)
public class WordMentionedByTopic extends Relation {
    @GraphId
    private Long id;

    @StartNode
    private TopicNode topic;

    @EndNode
    private WordNode word;

    @Property
    private Double weight;

    @Override
    public void setStart(Resource start) {
        this.topic = (TopicNode) start;
    }

    @Override
    public void setEnd(Resource end) {
        this.word = (WordNode) end;
    }

    @Override
    public Resource getStart() {
        return topic;
    }

    @Override
    public Resource getEnd() {
        return word;
    }

    @Override
    public void setProperties(Properties properties) {
        this.weight = properties.getWeight();
    }
}
