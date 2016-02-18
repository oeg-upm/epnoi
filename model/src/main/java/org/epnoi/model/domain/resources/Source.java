package org.epnoi.model.domain.resources;

import com.google.common.base.Strings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(of = "uri", callSuper = true)
public class Source extends Resource {

    @Override
    public Resource.Type getResourceType() {return Type.SOURCE;}

    public static final String NAME="name";
    private String name = "";

    public static final String DESCRIPTION="description";
    private String description = "";

    public static final String URL="url";
    private String url = "";

    public static final String PROTOCOL="protocol";
    private String protocol = "";

    public String getProtocol(){
        if (Strings.isNullOrEmpty(protocol)){
            return StringUtils.substringBefore(url,":");
        }
        return protocol;
    }

    public String getName(){
        if (Strings.isNullOrEmpty(name)){
            return StringUtils.substringBetween(url+"/","//","/");
        }
        return name;
    }

    public String extractServer(){
        return StringUtils.substringBefore(url, "?");
    }
}
