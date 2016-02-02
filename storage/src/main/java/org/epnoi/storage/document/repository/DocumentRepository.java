package org.epnoi.storage.document.repository;

import org.epnoi.model.Resource;
import org.epnoi.storage.document.domain.*;
import org.epnoi.storage.model.ResourceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 02/02/16.
 */
@Component
public class DocumentRepository {

    @Autowired
    AnalysisDocumentRepository analysisDocumentRepository;

    @Autowired
    DocumentDocumentRepository documentDocumentRepository;

    @Autowired
    DomainDocumentRepository domainDocumentRepository;

    @Autowired
    ItemDocumentRepository itemDocumentRepository;

    @Autowired
    PartDocumentRepository partDocumentRepository;

    @Autowired
    RelationDocumentRepository relationDocumentRepository;

    @Autowired
    SourceDocumentRepository sourceDocumentRepository;

    @Autowired
    TopicDocumentRepository topicDocumentRepository;

    @Autowired
    WordDocumentRepository wordDocumentRepository;

    public Resource save(Resource resource, Resource.Type type){

        switch(type){
            case ANALYSIS: return analysisDocumentRepository.save(ResourceUtils.map(resource, AnalysisDocument.class));
            case DOCUMENT: return documentDocumentRepository.save(ResourceUtils.map(resource, DocumentDocument.class));
            case DOMAIN: return domainDocumentRepository.save(ResourceUtils.map(resource, DomainDocument.class));
            case ITEM: return itemDocumentRepository.save(ResourceUtils.map(resource, ItemDocument.class));
            case PART: return partDocumentRepository.save(ResourceUtils.map(resource, PartDocument.class));
            case RELATION: return relationDocumentRepository.save(ResourceUtils.map(resource, RelationDocument.class));
            case SOURCE: return sourceDocumentRepository.save(ResourceUtils.map(resource, SourceDocument.class));
            case TOPIC: return topicDocumentRepository.save(ResourceUtils.map(resource, TopicDocument.class));
            case WORD: return wordDocumentRepository.save(ResourceUtils.map(resource, WordDocument.class));
        }
        return resource;
    }

}
