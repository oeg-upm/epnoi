package org.epnoi.learner.helper;

import lombok.Data;
import org.epnoi.storage.UDM;
import org.epnoi.storage.generator.TimeGenerator;
import org.epnoi.storage.generator.URIGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 10/02/16.
 */
@Data
@Component
public class LearningHelper {

    @Autowired
    URIGenerator uriGenerator;

    @Autowired
    TimeGenerator timeGenerator;

    @Autowired
    UDM udm;

}
