package org.epnoi.model.domain.resources;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(of = "uri", callSuper = true)
public class SerializedObject extends Resource {

    public static final String INSTANCE="instance";
    protected Object instance = "";

}
