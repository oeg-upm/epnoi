package org.epnoi.api.services;

import org.epnoi.model.domain.Analysis;
import org.epnoi.model.domain.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class AnalysisService extends AbstractCRUDService<Analysis> {

    public AnalysisService() {
        super(Resource.Type.ANALYSIS);
    }
}
