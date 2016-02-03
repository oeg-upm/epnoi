package org.epnoi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;
import org.epnoi.model.domain.Resource;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Source extends Resource {

	private String uri;

	private String url;

	public String protocol(){
		return StringUtils.substringBefore(url,":");
	}

	public String name(){
		return StringUtils.substringBetween(url,"//","/");
	}

	public String server(){
		return StringUtils.substringBefore(url, "?");
	}

}
