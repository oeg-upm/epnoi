package org.epnoi.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.relations.MentionsFromTopic;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.system.graph.domain.nodes.*;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="MENTIONS")
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@ToString(of = {"uri"},callSuper = true)
public class MentionsFromTopicEdge extends Edge<TopicNode,WordNode> {

    @Property
    private Long times;

    @Override
    public Resource.Type getStartType() {
        return Resource.Type.TOPIC;
    }

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.WORD;
    }
}
