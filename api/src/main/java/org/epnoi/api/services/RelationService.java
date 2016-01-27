package org.epnoi.api.services;

import org.epnoi.storage.model.Relation;
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
public class RelationService extends AbstractCRUDService<Relation> {

    private static final Logger LOG = LoggerFactory.getLogger(RelationService.class);

    public RelationService() {
        super(Resource.Type.RELATION);
    }

    @Override
    protected Relation save(Relation resource) {
        udm.saveRelation(resource);
        return resource;
    }

    @Override
    protected Optional<Relation> read(String uri) {
        return udm.readRelation(uri);
    }

    @Override
    protected void delete(String uri) {
        udm.deleteRelation(uri);
    }

    @Override
    protected void deleteAll() {
        udm.deleteRelations();
    }

    @Override
    protected List<String> findAll() {
        return udm.findRelations();
    }

    @Override
    protected String newUri() {
        return uriGenerator.newRelation();
    }
}
