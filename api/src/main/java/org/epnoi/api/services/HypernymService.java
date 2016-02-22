package org.epnoi.api.services;

import org.epnoi.model.domain.relations.HypernymOf;
import org.epnoi.model.domain.relations.Relation;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class HypernymService extends AbstractRelationalService<HypernymOf> {

    public HypernymService() {
        super(Relation.Type.HYPERNYM_OF);
    }
}
