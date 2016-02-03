package org.epnoi.api.services;

import org.epnoi.api.model.SourceI;
import org.epnoi.model.domain.Resource;
import org.epnoi.model.domain.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class SourceService extends AbstractCRUDService<Source> {

    private static final Logger LOG = LoggerFactory.getLogger(SourceService.class);

    public SourceService() {
        super(Resource.Type.SOURCE);
    }


    @Override
    protected Source save(Source resource) {
        udm.saveSource(resource);
        return resource;
    }

    public Source create(SourceI resource) throws Exception {

        LOG.info("Trying to create: " + resource);

        Source source = new Source();
        BeanUtils.copyProperties(resource,source);
        source.setUri(newUri());
        source.setCreationTime(timeGenerator.getNowAsISO());
        return save(source);
    }


    public Source update(String id, SourceI resource) {
        String uri = uriGenerator.from(Resource.Type.SOURCE,id);
        LOG.debug("updating by uri: " + uri);
        Optional<Source> result = read(uri);
        if (!result.isPresent()){
            throw new RuntimeException("Resource does not exist with uri: " + uri);
        }
        Source original = result.get();
        BeanUtils.copyProperties(resource,original);
        return save(original);
    }

    @Override
    protected Optional<Source> read(String uri) {
        return udm.readSource(uri);
    }

    @Override
    protected void delete(String uri) {
        udm.deleteSource(uri);
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
