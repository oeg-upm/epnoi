package org.epnoi.storage.system.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.Relation;
import org.epnoi.storage.system.graph.domain.relationships.SimilarItem;
import org.epnoi.storage.system.graph.domain.relationships.TopicDealtByItem;
import org.epnoi.storage.system.graph.domain.relationships.WordMentionedByItem;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Item")
@Data
@EqualsAndHashCode(of = "uri",callSuper = true)
@ToString(callSuper = true)
public class ItemNode extends Node {

    @Relationship(type = "SIMILAR_TO", direction="UNDIRECTED")
    private Set<SimilarItem> items = new HashSet<>();

    @Relationship(type = "DEALS_WITH", direction="OUTGOING")
    private Set<TopicDealtByItem> topics =  new HashSet<>();

    @Relationship(type = "MENTIONS", direction="OUTGOING")
    private Set<WordMentionedByItem> words = new HashSet<>();

    public void addSimilarItem(SimilarItem similarItem){
        items.add(similarItem);
    }

    public void addTopicDealtByItem(TopicDealtByItem topicDealtByItem){
        topics.add(topicDealtByItem);
    }

    public void addWordMentionedByItem(WordMentionedByItem wordMentionedByItem){
        words.add(wordMentionedByItem);
    }

    @Override
    public void add(Relation relation, Relation.Type type) {
        switch (type){
            case ITEM_DEALS_WITH_TOPIC:
                topics.add((TopicDealtByItem) relation);
                break;
            case ITEM_SIMILAR_TO_ITEM:
                items.add((SimilarItem) relation);
                break;
            default: throw new RuntimeException( "Relation " + type + " not handled from Item Node");
        }
    }

    @Override
    public void remove(Relation relation, Relation.Type type) {
        switch (type){
            case ITEM_DEALS_WITH_TOPIC:
                topics.remove((TopicDealtByItem) relation);
                break;
            case ITEM_SIMILAR_TO_ITEM:
                items.remove((SimilarItem) relation);
                break;
            default: throw new RuntimeException( "Relation " + type + " not handled from Item Node");
        }
    }
}
