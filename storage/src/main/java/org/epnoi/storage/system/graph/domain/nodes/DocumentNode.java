package org.epnoi.storage.system.graph.domain.nodes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.Relation;
import org.epnoi.storage.system.graph.domain.edges.DocumentBundlesItem;
import org.epnoi.storage.system.graph.domain.edges.DocumentSimilarToDocument;
import org.epnoi.storage.system.graph.domain.edges.DocumentDealsWithTopic;
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
    private Set<DocumentSimilarToDocument> documents = new HashSet<>();

    @Relationship(type = "DEALS_WITH", direction="OUTGOING")
    private Set<DocumentDealsWithTopic> topics = new HashSet<>();

    @Relationship(type = "BUNDLES", direction="OUTGOING")
    private Set<DocumentBundlesItem> items = new HashSet<>();


    public void addSimilarDocument(DocumentSimilarToDocument similarDocument){
        documents.add(similarDocument);
    }

    public void addTopicDealtByDocument(DocumentDealsWithTopic topicDealtByDocument){
        topics.add(topicDealtByDocument);
    }

    public void addItemBundledByDocument(DocumentBundlesItem itemBundledByDocument){
        items.add(itemBundledByDocument);
    }

    @Override
    public void add(Relation relation, Relation.Type type) {
        switch (type){
            case DOCUMENT_BUNDLES_ITEM:
                items.add((DocumentBundlesItem) relation);
                break;
            case DOCUMENT_DEALS_WITH_TOPIC:
                topics.add((DocumentDealsWithTopic) relation);
                break;
            case DOCUMENT_SIMILAR_TO_DOCUMENT:
                documents.add((DocumentSimilarToDocument) relation);
                break;
            default: throw new RuntimeException( "Relation " + type + " not handled from Document Node");
        }
    }

    @Override
    public void remove(Relation relation, Relation.Type type) {
        switch (type){
            case DOCUMENT_BUNDLES_ITEM:
                items.remove((DocumentBundlesItem) relation);
                break;
            case DOCUMENT_DEALS_WITH_TOPIC:
                topics.remove((DocumentDealsWithTopic) relation);
                break;
            case DOCUMENT_SIMILAR_TO_DOCUMENT:
                documents.remove((DocumentSimilarToDocument) relation);
                break;
            default: throw new RuntimeException( "Relation " + type + " not handled from Document Node");
        }
    }
}
