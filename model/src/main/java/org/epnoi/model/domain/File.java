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
public class File extends Resource {

    public static final String URL="url";
    private String url = "";

    public static final String SOURCE="source";
    private String source = "";

    // TODO this field should be deleted
    public static final String DOMAIN="domain";
    private String domain = "";

    public static final String METAINFORMATION="metainformation";
    private MetaInformation metainformation;

}