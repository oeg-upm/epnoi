package org.epnoi.storage.model;

import lombok.Data;
import lombok.ToString;

/**
 * Created by cbadenes on 21/01/16.
 */
@Data
@ToString(callSuper = true)
public class Provision extends Resource{

    String source;

    String document;

    String date;
}
