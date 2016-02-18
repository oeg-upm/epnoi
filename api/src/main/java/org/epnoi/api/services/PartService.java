package org.epnoi.api.services;

import org.epnoi.model.domain.resources.Part;
import org.epnoi.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class PartService extends AbstractCRUDService<Part> {

    public PartService() {
        super(Resource.Type.PART);
    }
}
