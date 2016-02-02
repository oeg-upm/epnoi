package org.epnoi.storage.model;

import lombok.Data;
import lombok.ToString;
import org.epnoi.model.Resource;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
@ToString(callSuper = true)
public class Topic extends Resource {

    private String content;

    private String analysis;
}
