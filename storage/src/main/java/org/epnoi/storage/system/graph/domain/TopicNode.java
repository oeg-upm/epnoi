package org.epnoi.storage.system.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.Relation;
import org.epnoi.storage.system.graph.domain.relationships.DomainInTopic;
import org.epnoi.storage.system.graph.domain.relationships.WordMentionedByTopic;
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
    private Set<DomainInTopic> domains = new HashSet<>();

    @Relationship(type = "MENTIONS", direction="OUTGOING")
    private Set<WordMentionedByTopic> words = new HashSet<>();

    public void addDomainInTopic(DomainInTopic domainInTopic){
        domains.add(domainInTopic);
    }

    public void addWordMentionedByTopic(WordMentionedByTopic wordMentionedByTopic){
        words.add(wordMentionedByTopic);
    }

    @Override
    public void add(Relation relation, Relation.Type type) {
        switch(type){
            case TOPIC_EMERGES_IN_DOMAIN:
                domains.add((DomainInTopic) relation);
                break;
            case TOPIC_MENTIONS_WORD:
                words.add((WordMentionedByTopic) relation);
                break;
            default: throw new RuntimeException( "Relation " + type + " not handled from Topic Node");
        }
    }

    @Override
    public void remove(Relation relation, Relation.Type type) {
        switch(type){
            case TOPIC_EMERGES_IN_DOMAIN:
                domains.remove((DomainInTopic) relation);
                break;
            case TOPIC_MENTIONS_WORD:
                words.remove((WordMentionedByTopic) relation);
                break;
            default: throw new RuntimeException( "Relation " + type + " not handled from Topic Node");
        }
    }
}
