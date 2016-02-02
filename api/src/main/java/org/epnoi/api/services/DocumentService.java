package org.epnoi.api.services;

import org.epnoi.model.Resource;
import org.epnoi.storage.model.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class DocumentService extends AbstractCRUDService<Document> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentService.class);

    public DocumentService() {
        super(Resource.Type.DOCUMENT);
    }


    @Override
    public Document create(Document resource) throws Exception {
        throw new RuntimeException("Method not handled by Web Service");
    }

    @Override
    protected Document save(Document resource) {
        return null;
    }

    @Override
    protected Optional<Document> read(String uri) {
        return udm.readDocument(uri);
    }

    @Override
    protected void delete(String uri) {
        udm.deleteDocument(uri);
    }

    @Override
    protected void deleteAll() {
        udm.deleteDocuments();
    }

    @Override
    protected List<String> findAll() {
        return udm.findDocuments();
    }

    @Override
    protected String newUri() {
        return uriGenerator.newDocument();
    }
}
