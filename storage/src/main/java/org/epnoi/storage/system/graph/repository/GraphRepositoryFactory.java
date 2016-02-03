package org.epnoi.storage.system.graph.repository;

import org.epnoi.model.domain.Relation;
import org.epnoi.model.domain.Resource;
import org.epnoi.storage.system.graph.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 03/02/16.
 */
@Component
public class GraphRepositoryFactory {

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


    public BaseGraphRepository repositoryOf(Resource.Type type){
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

    public BaseGraphRepository repositoryOf(Relation.Type type){
        switch (type){
            case DOCUMENT_BUNDLES_ITEM: return documentGraphRepository;
            case DOCUMENT_DEALS_WITH_TOPIC: return documentGraphRepository;
            case DOCUMENT_SIMILAR_TO_DOCUMENT: return documentGraphRepository;
            case DOMAIN_CONTAINS_DOCUMENT: return domainGraphRepository;
            case ITEM_DEALS_WITH_TOPIC: return itemGraphRepository;
            case ITEM_SIMILAR_TO_ITEM: return itemGraphRepository;
            case PART_DEALS_WITH_TOPIC: return partGraphRepository;
            case PART_DESCRIBES_ITEM: return partGraphRepository;
            case PART_SIMILAR_TO_PART: return partGraphRepository;
            case SOURCE_COMPOSES_DOMAIN: return sourceGraphRepository;
            case SOURCE_PROVIDES_DOCUMENT: return sourceGraphRepository;
            case TOPIC_EMERGES_IN_DOMAIN: return topicGraphRepository;
            case TOPIC_MENTIONS_WORD: return topicGraphRepository;
            case WORD_EMBEDDED_IN_DOMAIN: return wordGraphRepository;
            case WORD_PAIRS_WITH_WORD: return wordGraphRepository;
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

}
