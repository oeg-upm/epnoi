package org.epnoi.api.services;

import org.epnoi.storage.model.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class DomainService extends AbstractCRUDService<Domain> {

    private static final Logger LOG = LoggerFactory.getLogger(DomainService.class);


    @Override
    protected Domain save(Domain resource) {
        udm.saveDomain(resource);
        return resource;
    }

    @Override
    protected Optional<Domain> read(String uri) {
        return udm.readDomain(uri);
    }

    @Override
    protected Domain delete(String uri) {
        return udm.deleteDomain(uri);
    }

    @Override
    protected void deleteAll() {
        udm.deleteDomains();
    }

    @Override
    protected List<String> findAll() {
        return udm.findDomains();
    }

    @Override
    protected String newUri() {
        return uriGenerator.newDomain();
    }
}
