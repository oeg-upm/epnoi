package org.epnoi.storage.model;

import lombok.Data;
import lombok.ToString;

/**
 * Created by cbadenes on 21/01/16.
 */
@Data
@ToString(callSuper = true)
public class Composition extends Resource{

    String source;

    String domain;

    String date;
}
