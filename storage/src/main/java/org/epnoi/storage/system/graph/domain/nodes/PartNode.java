package org.epnoi.storage.system.graph.domain.nodes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.Relation;
import org.epnoi.storage.system.graph.domain.edges.PartDescribesItem;
import org.epnoi.storage.system.graph.domain.edges.PartSimilarToPart;
import org.epnoi.storage.system.graph.domain.edges.PartDealsWithTopic;
import org.epnoi.storage.system.graph.domain.edges.WordMentionedByPart;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Part")
@Data
@EqualsAndHashCode(of = "uri",callSuper = true)
@ToString(callSuper = true)
public class PartNode extends Node {

    private String sense;

    @Relationship(type = "SIMILAR_TO", direction="UNDIRECTED")
    private Set<PartSimilarToPart> parts = new HashSet<>();

    @Relationship(type = "DEALS_WITH", direction="OUTGOING")
    private Set<PartDealsWithTopic> topics = new HashSet<>();

    @Relationship(type = "MENTIONS", direction="OUTGOING")
    private Set<WordMentionedByPart> words = new HashSet<>();

    @Relationship(type = "DESCRIBES", direction="OUTGOING")
    private Set<PartDescribesItem> items = new HashSet<>();


    public void addSimilarPart(PartSimilarToPart similarPart){
        parts.add(similarPart);
    }

    public void addTopicDealtByPart(PartDealsWithTopic topicDealtByPart){
        topics.add(topicDealtByPart);
    }

    public void addWordMentionedByPart(WordMentionedByPart wordMentionedByPart){
        words.add(wordMentionedByPart);
    }

    public void addItemDescribedByPart(PartDescribesItem itemDescribedByPart){
        items.add(itemDescribedByPart);
    }

    @Override
    public void add(Relation relation, Relation.Type type) {
        switch(type){
            case PART_DEALS_WITH_TOPIC:
                topics.add((PartDealsWithTopic) relation);
                break;
            case PART_DESCRIBES_ITEM:
                items.add((PartDescribesItem) relation);
                break;
            case PART_SIMILAR_TO_PART:
                parts.add((PartSimilarToPart) relation);
                break;
            default: throw new RuntimeException( "Relation " + type + " not handled from Part Node");
        }
    }

    @Override
    public void remove(Relation relation, Relation.Type type) {
        switch(type){
            case PART_DEALS_WITH_TOPIC:
                topics.remove((PartDealsWithTopic) relation);
                break;
            case PART_DESCRIBES_ITEM:
                items.remove((PartDescribesItem) relation);
                break;
            case PART_SIMILAR_TO_PART:
                parts.remove((PartSimilarToPart) relation);
                break;
            default: throw new RuntimeException( "Relation " + type + " not handled from Part Node");
        }
    }
}
