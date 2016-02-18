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
public abstract class Mentions extends Relation{

    @Getter @Setter
    private Long times;

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.WORD;
    }

    @Override
    public Double getWeight() {
        return Double.valueOf(times);
    }
}
