package org.epnoi.api.model;

import lombok.Data;

/**
 * Created by cbadenes on 22/01/16.
 */
@Data
public class DocumentI {

    String title;

    String subject;

    String publishedOn;

    String publishedBy;

    String authoredOn;

    String authoredBy;

    String contributedBy;

    String language;

    String description;

    String rights;

    String content;

    String tokens;
}
