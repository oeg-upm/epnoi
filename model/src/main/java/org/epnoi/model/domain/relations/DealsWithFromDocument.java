package org.epnoi.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.resources.Resource;

/**
 * Created by cbadenes on 17/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
public class DealsWithFromDocument extends DealsWith{

    @Override
    public Resource.Type getStartType() {
        return Resource.Type.DOCUMENT;
    }

    @Override
    public Type getType() {return Type.DEALS_WITH_FROM_DOCUMENT;}
}
