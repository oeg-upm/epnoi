package org.epnoi.storage.system.column.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.Item;
import org.epnoi.model.domain.Resource;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by cbadenes on 22/12/15.
 */
@Table(value = "items")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ItemColumn extends Resource {

    @PrimaryKey
    private String uri;

    public static final String AUTHORED_ON=Item.AUTHORED_ON;
    private String authoredOn;

    public static final String AUTHORED_BY=Item.AUTHORED_BY;
    private String authoredBy;

    public static final String CONTRIBUTED_BY=Item.CONTRIBUTED_BY;
    private String contributedBy;

    public static final String FORMAT=Item.FORMAT;
    private String format;

    public static final String LANGUAGE=Item.LANGUAGE;
    private String language;

    public static final String TITLE=Item.TITLE;
    private String title;

    public static final String SUBJECT=Item.SUBJECT;
    private String subject;

    public static final String DESCRIPTION=Item.DESCRIPTION;
    private String description;

    public static final String URL=Item.URL;
    private String url;

    public static final String TYPE=Item.TYPE;
    private String type;

    public static final String CONTENT=Item.CONTENT;
    private String content;

    public static final String TOKENS=Item.TOKENS;
    private String tokens;

    public static final String ANNOTATED=Item.ANNOTATED;
    private String annotated;

}
