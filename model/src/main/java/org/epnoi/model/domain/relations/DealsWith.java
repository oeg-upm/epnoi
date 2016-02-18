package org.epnoi.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.epnoi.model.domain.resources.Resource;

/**
 * Created by cbadenes on 17/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
public abstract class DealsWith extends Relation{

    @Getter @Setter
    Double weight;

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.TOPIC;
    }

}
