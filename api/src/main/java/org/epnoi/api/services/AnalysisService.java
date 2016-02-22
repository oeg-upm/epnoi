package org.epnoi.api.services;

import org.epnoi.model.domain.resources.Analysis;
import org.epnoi.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class AnalysisService extends AbstractResourceService<Analysis> {

    public AnalysisService() {
        super(Resource.Type.ANALYSIS);
    }
}
