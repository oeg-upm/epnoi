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
public class AppearedIn extends Relation{

    public static final String LENGTH="times";
    @Getter @Setter
    private Long times;

    public static final String SUBTERMS="subtermOf";
    @Getter @Setter
    private long subtermOf;

    public static final String SUPERTERMS="supertermOf";
    @Getter @Setter
    private long supertermOf;

    public static final String CVALUE="cvalue";
    @Getter @Setter
    private double cvalue;

    public static final String CONSENSUS="consensus";
    @Getter @Setter
    private double consensus;

    public static final String PERTINENCE="pertinence";
    @Getter @Setter
    private double pertinence;

    public static final String PROBABILITY="probability";
    @Getter @Setter
    private double probability;

    public static final String TERMHOOD="termhood";
    @Getter @Setter
    private double termhood;

    @Override
    public Resource.Type getStartType() {
        return Resource.Type.TERM;
    }

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.DOMAIN;
    }

    @Override
    public Type getType() {return Type.APPEARED_IN;}

}
