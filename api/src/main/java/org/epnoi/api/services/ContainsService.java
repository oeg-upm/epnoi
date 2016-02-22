package org.epnoi.api.services;

import org.epnoi.model.domain.relations.Contains;
import org.epnoi.model.domain.relations.HypernymOf;
import org.epnoi.model.domain.relations.Relation;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class ContainsService extends AbstractRelationalService<Contains> {

    public ContainsService() {
        super(Relation.Type.CONTAINS);
    }
}
