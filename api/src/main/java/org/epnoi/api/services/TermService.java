package org.epnoi.api.services;

import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.domain.resources.Term;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class TermService extends AbstractResourceService<Term> {

    public TermService() {
        super(Resource.Type.TERM);
    }
}
