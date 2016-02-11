package org.epnoi.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by cbadenes on 07/01/16.
 */
@Data
public class MetaInformation implements Serializable{

    private String pubURI;
    private String pubFormat;
    private String sourceName;
    private String sourceUrl;
    private String sourceUri;
    private String title;
    private String published;
    private String authored;
    private String format;
    private String type;
    private String subject;
    private String language;
    private String rights;
    private String description;
    private String creators;
    private String contributors;

}
