package org.epnoi.storage.system.graph.repository.nodes;

import org.epnoi.model.domain.Relation;
import org.epnoi.model.domain.Resource;
import org.epnoi.storage.exception.RepositoryNotFound;
import org.epnoi.storage.system.graph.domain.edges.*;
import org.epnoi.storage.system.graph.domain.nodes.*;
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
    TermGraphRepository termGraphRepository;


    public ResourceGraphRepository repositoryOf(Resource.Type type) throws RepositoryNotFound {
        switch (type){
            case DOCUMENT: return documentGraphRepository;
            case DOMAIN: return domainGraphRepository;
            case ITEM: return itemGraphRepository;
            case PART: return partGraphRepository;
            case SOURCE: return sourceGraphRepository;
            case TOPIC: return topicGraphRepository;
            case WORD: return wordGraphRepository;
            case TERM: return termGraphRepository;
        }
        throw new RepositoryNotFound("Graph Repository not found for " + type);
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
            case TERM: return TermNode.class;
        }
        throw new RuntimeException("Mapping not found for " + type);
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
