package org.epnoi.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.resources.Resource;

/**
 * Created by cbadenes on 17/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
public class SimilarToItems extends SimilarTo{

    @Override
    public Resource.Type getResourceType() {
        return Resource.Type.ITEM;
    }

    @Override
    public Type getType() {return Type.SIMILAR_TO_ITEMS;}
}
