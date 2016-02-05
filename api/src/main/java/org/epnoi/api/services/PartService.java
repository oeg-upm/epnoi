package org.epnoi.api.services;

import org.epnoi.model.domain.Part;
import org.epnoi.model.domain.Resource;
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
