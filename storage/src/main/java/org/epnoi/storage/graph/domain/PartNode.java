package org.epnoi.storage.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.storage.graph.domain.relationships.ItemDescribedByPart;
import org.epnoi.storage.graph.domain.relationships.SimilarPart;
import org.epnoi.storage.graph.domain.relationships.TopicDealtByPart;
import org.epnoi.storage.graph.domain.relationships.WordMentionedByPart;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Part")
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@ToString(of={"uri"}, callSuper = true)
public class PartNode extends Node {

    private String sense;

    @Relationship(type = "SIMILAR_TO", direction="UNDIRECTED")
    private Set<SimilarPart> parts = new HashSet<>();

    @Relationship(type = "DEALS_WITH", direction="OUTGOING")
    private Set<TopicDealtByPart> topics = new HashSet<>();

    @Relationship(type = "MENTIONS", direction="OUTGOING")
    private Set<WordMentionedByPart> words = new HashSet<>();

    @Relationship(type = "DESCRIBES", direction="OUTGOING")
    private Set<ItemDescribedByPart> items = new HashSet<>();


    public void addSimilarPart(SimilarPart similarPart){
        parts.add(similarPart);
    }

    public void addTopicDealtByPart(TopicDealtByPart topicDealtByPart){
        topics.add(topicDealtByPart);
    }

    public void addWordMentionedByPart(WordMentionedByPart wordMentionedByPart){
        words.add(wordMentionedByPart);
    }

    public void addItemDescribedByPart(ItemDescribedByPart itemDescribedByPart){
        items.add(itemDescribedByPart);
    }

}
