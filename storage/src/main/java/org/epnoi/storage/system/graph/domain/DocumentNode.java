package org.epnoi.storage.system.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.Relation;
import org.epnoi.storage.system.graph.domain.relationships.ItemBundledByDocument;
import org.epnoi.storage.system.graph.domain.relationships.SimilarDocument;
import org.epnoi.storage.system.graph.domain.relationships.TopicDealtByDocument;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Document")
@Data
@EqualsAndHashCode(of = "uri",callSuper = true)
@ToString(callSuper = true)
public class DocumentNode extends Node {

    @Relationship(type = "SIMILAR_TO", direction="UNDIRECTED")
    private Set<SimilarDocument> documents = new HashSet<>();

    @Relationship(type = "DEALS_WITH", direction="OUTGOING")
    private Set<TopicDealtByDocument> topics = new HashSet<>();

    @Relationship(type = "BUNDLES", direction="OUTGOING")
    private Set<ItemBundledByDocument> items = new HashSet<>();


    public void addSimilarDocument(SimilarDocument similarDocument){
        documents.add(similarDocument);
    }

    public void addTopicDealtByDocument(TopicDealtByDocument topicDealtByDocument){
        topics.add(topicDealtByDocument);
    }

    public void addItemBundledByDocument(ItemBundledByDocument itemBundledByDocument){
        items.add(itemBundledByDocument);
    }

    @Override
    public void add(Relation relation, Relation.Type type) {
        switch (type){
            case DOCUMENT_BUNDLES_ITEM:
                items.add((ItemBundledByDocument) relation);
                break;
            case DOCUMENT_DEALS_WITH_TOPIC:
                topics.add((TopicDealtByDocument) relation);
                break;
            case DOCUMENT_SIMILAR_TO_DOCUMENT:
                documents.add((SimilarDocument) relation);
                break;
            default: throw new RuntimeException( "Relation " + type + " not handled from Document Node");
        }
    }

    @Override
    public void remove(Relation relation, Relation.Type type) {
        switch (type){
            case DOCUMENT_BUNDLES_ITEM:
                items.remove((ItemBundledByDocument) relation);
                break;
            case DOCUMENT_DEALS_WITH_TOPIC:
                topics.remove((TopicDealtByDocument) relation);
                break;
            case DOCUMENT_SIMILAR_TO_DOCUMENT:
                documents.remove((SimilarDocument) relation);
                break;
            default: throw new RuntimeException( "Relation " + type + " not handled from Document Node");
        }
    }
}
