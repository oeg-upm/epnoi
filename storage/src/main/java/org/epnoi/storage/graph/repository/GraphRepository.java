package org.epnoi.storage.graph.repository;

import org.epnoi.model.Resource;
import org.epnoi.storage.graph.domain.*;
import org.epnoi.storage.model.ResourceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 02/02/16.
 */
@Component
public class GraphRepository {

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

    public Node save(Resource resource, Resource.Type type){

        switch(type){
            case DOCUMENT:  return documentGraphRepository.save(ResourceUtils.map(resource, DocumentNode.class));
            case DOMAIN:    return domainGraphRepository.save(ResourceUtils.map(resource, DomainNode.class));
            case ITEM:      return itemGraphRepository.save(ResourceUtils.map(resource, ItemNode.class));
            case PART:      return partGraphRepository.save(ResourceUtils.map(resource, PartNode.class));
            case SOURCE:    return sourceGraphRepository.save(ResourceUtils.map(resource, SourceNode.class));
            case TOPIC:     return topicGraphRepository.save(ResourceUtils.map(resource, TopicNode.class));
            case WORD:      return wordGraphRepository.save(ResourceUtils.map(resource, WordNode.class));
        }
        return new Node();

    }

}
