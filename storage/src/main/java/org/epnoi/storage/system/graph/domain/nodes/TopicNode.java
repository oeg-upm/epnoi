package org.epnoi.storage.system.graph.domain.nodes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.storage.system.graph.domain.edges.Edge;
import org.epnoi.storage.system.graph.domain.edges.EmergesInEdge;
import org.epnoi.storage.system.graph.domain.edges.MentionsFromTopicEdge;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Topic")
@Data
@EqualsAndHashCode(of = "uri",callSuper = true)
@ToString(callSuper = true)
public class TopicNode extends Node {

    @Relationship(type = "EMERGES_IN", direction="OUTGOING")
    private Set<EmergesInEdge> domains = new HashSet<>();

    @Relationship(type = "MENTIONS", direction="OUTGOING")
    private Set<MentionsFromTopicEdge> words = new HashSet<>();

    @Override
    public void add(Edge edge) {
        switch(edge.getType()){
            case EMERGES_IN:
                domains.add((EmergesInEdge) edge);
                break;
            case MENTIONS_FROM_TOPIC:
                words.add((MentionsFromTopicEdge) edge);
                break;
            default: throw new RuntimeException( "Relation " + edge + " not handled from Topic Node");
        }
    }

    @Override
    public void remove(Edge edge) {
        switch(edge.getType()){
            case EMERGES_IN:
                domains.remove((EmergesInEdge) edge);
                break;
            case MENTIONS_FROM_TOPIC:
                words.remove((MentionsFromTopicEdge) edge);
                break;
            default: throw new RuntimeException( "Relation " + edge + " not handled from Topic Node");
        }
    }
}
