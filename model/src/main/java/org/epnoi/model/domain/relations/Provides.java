package org.epnoi.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.resources.Resource;

/**
 * Created by cbadenes on 17/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
public class Provides extends Relation{

    @Override
    public Resource.Type getStartType() {
        return Resource.Type.SOURCE;
    }

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.DOCUMENT;
    }

    @Override
    public Double getWeight() {
        return 1.0;
    }

    @Override
    public Type getType() {return Type.PROVIDES;}

}
