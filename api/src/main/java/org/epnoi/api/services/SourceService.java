package org.epnoi.api.services;

import org.epnoi.api.model.SourceI;
import org.epnoi.model.domain.Resource;
import org.epnoi.model.domain.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

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


    public Source create(SourceI resource) throws Exception {

        LOG.info("Trying to create: " + resource);

        Source source = new Source();
        BeanUtils.copyProperties(resource,source);
        source.setUri(uriGenerator.newFor(Resource.Type.SOURCE));
        source.setCreationTime(timeGenerator.asISO());
        udm.save(Resource.Type.SOURCE).with(source);
        return source;
    }


    public Source update(String id, SourceI resource) {
        String uri = uriGenerator.from(Resource.Type.SOURCE,id);
        LOG.debug("updating by uri: " + uri);
        Optional<Resource> result = udm.read(Resource.Type.SOURCE).byUri(uri);
        if (!result.isPresent()){
            throw new RuntimeException("Resource does not exist with uri: " + uri);
        }
        Source original = (Source) result.get();
        BeanUtils.copyProperties(resource,original);
        udm.save(Resource.Type.SOURCE).with(original);
        return original;
    }

}