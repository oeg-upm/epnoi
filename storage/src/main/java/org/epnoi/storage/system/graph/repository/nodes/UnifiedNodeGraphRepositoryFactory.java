package org.epnoi.storage.system.graph.repository.nodes;

import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.exception.RepositoryNotFound;
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

}
