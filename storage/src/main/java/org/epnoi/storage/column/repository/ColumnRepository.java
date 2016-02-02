package org.epnoi.storage.column.repository;

import org.epnoi.model.Resource;
import org.epnoi.storage.column.domain.*;
import org.epnoi.storage.model.ResourceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 02/02/16.
 */
@Component
public class ColumnRepository {

    @Autowired
    AnalysisColumnRepository analysisColumnRepository;

    @Autowired
    DocumentColumnRepository documentColumnRepository;

    @Autowired
    DomainColumnRepository domainColumnRepository;

    @Autowired
    ItemColumnRepository itemColumnRepository;

    @Autowired
    PartColumnRepository partColumnRepository;

    @Autowired
    RelationColumnRepository relationColumnRepository;

    @Autowired
    SourceColumnRepository sourceColumnRepository;

    @Autowired
    TopicColumnRepository topicColumnRepository;

    @Autowired
    WordColumnRepository wordColumnRepository;


    public Resource save(Resource resource, Resource.Type type){
        switch (type){
            case ANALYSIS: return analysisColumnRepository.save(ResourceUtils.map(resource, AnalysisColumn.class));
            case DOCUMENT: return documentColumnRepository.save(ResourceUtils.map(resource, DocumentColumn.class));
            case DOMAIN: return domainColumnRepository.save(ResourceUtils.map(resource, DomainColumn.class));
            case ITEM: return itemColumnRepository.save(ResourceUtils.map(resource, ItemColumn.class));
            case PART: return partColumnRepository.save(ResourceUtils.map(resource, PartColumn.class));
            case RELATION: return relationColumnRepository.save(ResourceUtils.map(resource, RelationColumn.class));
            case SOURCE: return sourceColumnRepository.save(ResourceUtils.map(resource, SourceColumn.class));
            case TOPIC: return topicColumnRepository.save(ResourceUtils.map(resource, TopicColumn.class));
            case WORD: return wordColumnRepository.save(ResourceUtils.map(resource, WordColumn.class));
        }
        return resource;
    }
}
