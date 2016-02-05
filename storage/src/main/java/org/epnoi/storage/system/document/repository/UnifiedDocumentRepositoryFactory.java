package org.epnoi.storage.system.document.repository;

import org.epnoi.model.domain.Resource;
import org.epnoi.storage.system.document.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 03/02/16.
 */
@Component
public class UnifiedDocumentRepositoryFactory {

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
    SourceDocumentRepository sourceDocumentRepository;

    @Autowired
    TopicDocumentRepository topicDocumentRepository;

    @Autowired
    WordDocumentRepository wordDocumentRepository;

    public BaseDocumentRepository repositoryOf(Resource.Type type){
        switch (type){
            case ANALYSIS: return analysisDocumentRepository;
            case DOCUMENT: return documentDocumentRepository;
            case DOMAIN: return domainDocumentRepository;
            case ITEM: return itemDocumentRepository;
            case PART: return partDocumentRepository;
            case SOURCE: return sourceDocumentRepository;
            case TOPIC: return topicDocumentRepository;
            case WORD: return wordDocumentRepository;
        }
        throw new RuntimeException("Repository not found for " + type);
    }

    public Class mappingOf(Resource.Type type){
        switch (type){
            case ANALYSIS: return AnalysisDocument.class;
            case DOCUMENT: return DocumentDocument.class;
            case DOMAIN: return DomainDocument.class;
            case ITEM: return ItemDocument.class;
            case PART: return PartDocument.class;
            case SOURCE: return SourceDocument.class;
            case TOPIC: return TopicDocument.class;
            case WORD: return WordDocument.class;
        }
        throw new RuntimeException("Mapping not found for " + type);
    }


}
