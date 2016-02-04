package org.epnoi.model.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(of = "uri", callSuper = true)
public class Analysis extends Resource {

    public static final String TYPE="type";
    private String type;

    public static final String DESCRIPTION="description";
    private String description;

    public static final String CONFIGURATION="configuration";
    private String configuration;

    public static final String DOMAIN="domain";
    private String domain;
}
