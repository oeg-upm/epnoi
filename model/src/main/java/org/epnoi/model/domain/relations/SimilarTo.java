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
public abstract class SimilarTo extends Relation{

    @Getter @Setter
    private Double weight;

    @Getter @Setter
    private String domain;

    public abstract Resource.Type getResourceType();

    @Override
    public Resource.Type getStartType() {
        return getResourceType();
    }

    @Override
    public Resource.Type getEndType() {
        return getResourceType();
    }

}
