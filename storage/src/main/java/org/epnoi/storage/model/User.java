package org.epnoi.storage.model;

import lombok.Data;
import lombok.ToString;
import org.epnoi.model.Resource;

/**
 * Created by cbadenes on 12/01/16.
 */
@Data
@ToString(callSuper = true)
public class User extends Resource {

    String name;

    String surname;
}
