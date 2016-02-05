package org.epnoi.storage.system.graph.repository.nodes;

import org.epnoi.model.domain.Relation;
import org.epnoi.model.domain.Resource;
import org.epnoi.storage.system.graph.domain.edges.*;
import org.epnoi.storage.system.graph.domain.nodes.*;
import org.epnoi.storage.system.graph.repository.edges.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 03/02/16.
 */
@Component
public class UnifiedNodeGraphRepositoryFactory {

    @Autowired
    DocumentGraphRepository documentGraphRepository;

    @Autowired
    DomainGraphRepository domainGraphRepository;

    @Autowired
    ItemGraphRepository itemGraphRepository;

    @Autowired
    PartGraphRepository partGraphRepository;

    @Autowired
    SourceGraphRepository sourceGraphRepository;

    @Autowired
    TopicGraphRepository topicGraphRepository;

    @Autowired
    WordGraphRepository wordGraphRepository;

    @Autowired
    DocumentDealsWithTopicGraphRepository documentDealsWithTopicGraphRepository;

    @Autowired
    DocumentBundlesItemGraphRepository documentBundlesItemGraphRepository;

    @Autowired
    DocumentSimilarToDocumentGraphRepository documentSimilarToDocumentGraphRepository;

    @Autowired
    DomainContainsDocumentGraphRepository domainContainsDocumentGraphRepository;

    @Autowired
    ItemDealsWithTopicGraphRepository itemDealsWithTopicGraphRepository;

    @Autowired
    ItemSimilarToItemGraphRepository itemSimilarToItemGraphRepository;

    @Autowired
    PartDealsWithTopicGraphRepository partDealsWithTopicGraphRepository;

    @Autowired
    PartDescribesItemGraphRepository partDescribesItemGraphRepository;

    @Autowired
    PartSimilarToPartGraphRepository partSimilarToPartGraphRepository;

    @Autowired
    RelationGraphRepository relationGraphRepository;

    @Autowired
    SourceComposesDomainGraphRepository sourceComposesDomainGraphRepository;

    @Autowired
    SourceProvidesDocumentGraphRepository sourceProvidesDocumentGraphRepository;

    @Autowired
    TopicEmergesInDomainGraphRepository topicEmergesInDomainGraphRepository;

    @Autowired
    TopicMentionsWordGraphRepository topicMentionsWordGraphRepository;

    @Autowired
    WordEmbeddedInDomainGraphRepository wordEmbeddedInDomainGraphRepository;

    @Autowired
    WordPairsWithWordGraphRepository wordPairsWithWordGraphRepository;


    public ResourceGraphRepository repositoryOf(Resource.Type type){
        switch (type){
            case DOCUMENT: return documentGraphRepository;
            case DOMAIN: return domainGraphRepository;
            case ITEM: return itemGraphRepository;
            case PART: return partGraphRepository;
            case SOURCE: return sourceGraphRepository;
            case TOPIC: return topicGraphRepository;
            case WORD: return wordGraphRepository;
        }
        throw new RuntimeException("Repository not found for " + type);
    }

    public RelationGraphRepository repositoryOf(Relation.Type type){
        switch (type){
            case DOCUMENT_BUNDLES_ITEM: return documentBundlesItemGraphRepository;
            case DOCUMENT_DEALS_WITH_TOPIC: return documentDealsWithTopicGraphRepository;
            case DOCUMENT_SIMILAR_TO_DOCUMENT: return documentSimilarToDocumentGraphRepository;
            case DOMAIN_CONTAINS_DOCUMENT: return domainContainsDocumentGraphRepository;
            case ITEM_DEALS_WITH_TOPIC: return itemDealsWithTopicGraphRepository;
            case ITEM_SIMILAR_TO_ITEM: return itemSimilarToItemGraphRepository;
            case PART_DEALS_WITH_TOPIC: return partDealsWithTopicGraphRepository;
            case PART_DESCRIBES_ITEM: return partDescribesItemGraphRepository;
            case PART_SIMILAR_TO_PART: return partSimilarToPartGraphRepository;
            case SOURCE_COMPOSES_DOMAIN: return sourceComposesDomainGraphRepository;
            case SOURCE_PROVIDES_DOCUMENT: return sourceProvidesDocumentGraphRepository;
            case TOPIC_EMERGES_IN_DOMAIN: return topicEmergesInDomainGraphRepository;
            case TOPIC_MENTIONS_WORD: return topicMentionsWordGraphRepository;
            case WORD_EMBEDDED_IN_DOMAIN: return wordEmbeddedInDomainGraphRepository;
            case WORD_PAIRS_WITH_WORD: return wordPairsWithWordGraphRepository;
        }
        throw new RuntimeException("Repository not found for " + type);
    }

    public Class mappingOf(Resource.Type type){
        switch (type){
            case DOCUMENT: return DocumentNode.class;
            case DOMAIN: return DomainNode.class;
            case ITEM: return ItemNode.class;
            case PART: return PartNode.class;
            case SOURCE: return SourceNode.class;
            case TOPIC: return TopicNode.class;
            case WORD: return WordNode.class;
        }
        throw new RuntimeException("Mapping not found for " + type);
    }

    public Class mappingOf(Relation.Type type){
        switch (type){
            case DOCUMENT_BUNDLES_ITEM: return DocumentBundlesItem.class;
            case DOCUMENT_DEALS_WITH_TOPIC: return DocumentDealsWithTopic.class;
            case DOCUMENT_SIMILAR_TO_DOCUMENT: return DocumentSimilarToDocument.class;
            case DOMAIN_CONTAINS_DOCUMENT: return DomainContainsDocument.class;
            case ITEM_DEALS_WITH_TOPIC: return ItemDealsWithTopic.class;
            case ITEM_SIMILAR_TO_ITEM: return ItemSimilarToItem.class;
            case PART_DEALS_WITH_TOPIC: return PartDealsWithTopic.class;
            case PART_DESCRIBES_ITEM: return PartDescribesItem.class;
            case PART_SIMILAR_TO_PART: return PartSimilarToPart.class;
            case SOURCE_COMPOSES_DOMAIN: return SourceComposesDomain.class;
            case SOURCE_PROVIDES_DOCUMENT: return SourceProvidesDocument.class;
            case TOPIC_EMERGES_IN_DOMAIN: return TopicEmergesInDomain.class;
            case TOPIC_MENTIONS_WORD: return TopicMentionsWord.class;
            case WORD_EMBEDDED_IN_DOMAIN: return WordEmbeddedInDomain.class;
            case WORD_PAIRS_WITH_WORD: return WordPairsWithWord.class;
            default: throw new RuntimeException("No relation found for type: " + type);
        }

    }

    public Relation relationOf(Relation.Type type){
        switch (type){
            case DOCUMENT_BUNDLES_ITEM: return new DocumentBundlesItem();
            case DOCUMENT_DEALS_WITH_TOPIC: return new DocumentDealsWithTopic();
            case DOCUMENT_SIMILAR_TO_DOCUMENT: return new DocumentSimilarToDocument();
            case DOMAIN_CONTAINS_DOCUMENT: return new DomainContainsDocument();
            case ITEM_DEALS_WITH_TOPIC: return new ItemDealsWithTopic();
            case ITEM_SIMILAR_TO_ITEM: return new ItemSimilarToItem();
            case PART_DEALS_WITH_TOPIC: return new PartDealsWithTopic();
            case PART_DESCRIBES_ITEM: return new PartDescribesItem();
            case PART_SIMILAR_TO_PART: return new PartSimilarToPart();
            case SOURCE_COMPOSES_DOMAIN: return new SourceComposesDomain();
            case SOURCE_PROVIDES_DOCUMENT: return new SourceProvidesDocument();
            case TOPIC_EMERGES_IN_DOMAIN: return new TopicEmergesInDomain();
            case TOPIC_MENTIONS_WORD: return new TopicMentionsWord();
            case WORD_EMBEDDED_IN_DOMAIN: return new WordEmbeddedInDomain();
            case WORD_PAIRS_WITH_WORD: return new WordPairsWithWord();
            default: throw new RuntimeException("No relation found for type: " + type);
        }

    }

}
