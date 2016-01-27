package org.epnoi.api.services;

import org.epnoi.storage.model.Analysis;
import org.epnoi.storage.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class AnalysisService extends AbstractCRUDService<Analysis> {

    private static final Logger LOG = LoggerFactory.getLogger(AnalysisService.class);

    public AnalysisService() {
        super(Resource.Type.ANALYSIS);
    }

    @Override
    protected Analysis save(Analysis resource) {
        udm.saveAnalysis(resource);
        return resource;
    }

    @Override
    protected Optional<Analysis> read(String uri) {
        return udm.readAnalysis(uri);
    }

    @Override
    protected void delete(String uri) {
        udm.deleteAnalysis(uri);
    }

    @Override
    protected void deleteAll() {
        udm.deleteAnalyses();
    }

    @Override
    protected List<String> findAll() {
        return udm.findAnalyses();
    }

    @Override
    protected String newUri() {
        return uriGenerator.newAnalysis();
    }
}
