package org.epnoi.storage.system.graph.repository.edges;

import org.epnoi.model.domain.Relation;
import org.epnoi.storage.exception.RepositoryNotFound;
import org.epnoi.storage.system.graph.domain.edges.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 03/02/16.
 */
@Component
public class UnifiedEdgeGraphRepositoryFactory {

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

    @Autowired
    TermAppearedInDomainGraphRepository termAppearedInDomainGraphRepository;

    @Autowired
    TermMentionsWordGraphRepository termMentionsWordGraphRepository;


    public RelationGraphRepository repositoryOf(Relation.Type type) throws RepositoryNotFound{
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
            case TERM_APPEARED_IN_DOMAIN: return termAppearedInDomainGraphRepository;
            case TERM_MENTIONS_WORD: return termMentionsWordGraphRepository;
        }
        throw new RepositoryNotFound("Graph Repository not found for " + type);
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
            case TERM_APPEARED_IN_DOMAIN: return TermAppearedInDomain.class;
            case TERM_MENTIONS_WORD: return TermMentionsWord.class;
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
            case TERM_APPEARED_IN_DOMAIN: return new TermAppearedInDomain();
            case TERM_MENTIONS_WORD: return new TermMentionsWord();
            default: throw new RuntimeException("No relation found for type: " + type);
        }

    }

}
