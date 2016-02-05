package org.epnoi.api.services;

import org.epnoi.model.domain.Domain;
import org.epnoi.model.domain.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class DomainService extends AbstractCRUDService<Domain> {

    public DomainService() {
        super(Resource.Type.DOMAIN);
    }
}
