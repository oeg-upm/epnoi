package org.epnoi.api.services;

import org.epnoi.storage.model.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class PartService extends AbstractCRUDService<Part> {

    private static final Logger LOG = LoggerFactory.getLogger(PartService.class);


    @Override
    public Part create(Part resource) throws Exception {
        throw new RuntimeException("Method not handled by Web Service");
    }

    @Override
    protected Part save(Part resource) {
        return null;
    }

    @Override
    protected Optional<Part> read(String uri) {
        return udm.readPart(uri);
    }

    @Override
    protected Part delete(String uri) {
        return udm.deletePart(uri);
    }

    @Override
    protected void deleteAll() {
        udm.deleteParts();
    }

    @Override
    protected List<String> findAll() {
        return udm.findParts();
    }

    @Override
    protected String newUri() {
        return uriGenerator.newPart();
    }
}
