package org.epnoi.model.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * Created by cbadenes on 05/02/16.
 */
@ToString
@EqualsAndHashCode(of={"uri"})
public class LinkableElement implements Serializable, Comparable {

    public static final String URI="uri";
    @Getter
    @Setter
    String uri;

    public static final String CREATION_TIME="creationTime";
    @Getter
    @Setter
    String creationTime;

    @Override
    public int compareTo(Object o) {
        return this.uri.compareTo(((LinkableElement)o).getUri());
    }

    public boolean isValid(){
        return !StringUtils.isEmpty(uri) && !StringUtils.isEmpty(creationTime);
    }
}
