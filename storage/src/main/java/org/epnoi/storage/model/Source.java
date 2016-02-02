package org.epnoi.storage.model;

import com.google.common.base.Strings;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;
import org.epnoi.model.Resource;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
@ToString(callSuper = true)
public class Source extends Resource {

    private String name = "";

    private String description = "";

    private String url = "";

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
