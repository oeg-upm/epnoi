package org.epnoi.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.epnoi.model.domain.resources.Resource;

import java.util.List;

/**
 * Created by cbadenes on 17/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
public class EmbeddedIn extends Relation{

    @Getter
    @Setter
    private float[] vector;

    @Override
    public Resource.Type getStartType() {
        return Resource.Type.WORD;
    }

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.DOMAIN;
    }

    @Override
    public Double getWeight() {
        return 1.0;
    }

    @Override
    public Type getType() {return Type.EMBEDDED_IN;}
}
