package org.epnoi.api.services;

import org.epnoi.model.domain.Document;
import org.epnoi.model.domain.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class DocumentService extends AbstractCRUDService<Document> {

    public DocumentService() {
        super(Resource.Type.DOCUMENT);
    }
}
