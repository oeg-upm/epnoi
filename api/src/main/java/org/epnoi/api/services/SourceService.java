package org.epnoi.api.services;

import org.epnoi.storage.model.Source;
import org.epnoi.storage.model.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class SourceService extends AbstractCRUDService<Source> {

    private static final Logger LOG = LoggerFactory.getLogger(SourceService.class);


    @Override
    protected Source save(Source resource) {
        udm.saveSource(resource);
        return resource;
    }

    @Override
    protected Optional<Source> read(String uri) {
        return udm.readSource(uri);
    }

    @Override
    protected Source delete(String uri) {
        return udm.deleteSource(uri);
    }

    @Override
    protected void deleteAll() {
        udm.deleteSources();
    }

    @Override
    protected List<String> findAll() {
        return udm.findSources();
    }

    @Override
    protected String newUri() {
        return uriGenerator.newSource();
    }
}
