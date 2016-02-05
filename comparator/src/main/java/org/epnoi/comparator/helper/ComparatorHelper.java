package org.epnoi.comparator.helper;

import lombok.Data;
import org.epnoi.storage.generator.TimeGenerator;
import org.epnoi.storage.UDM;
import org.epnoi.storage.generator.URIGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 13/01/16.
 */
@Data
@Component
public class ComparatorHelper {

    @Autowired
    UDM udm;

    @Autowired
    TimeGenerator timeGenerator;

    @Autowired
    URIGenerator uriGenerator;

    @Autowired
    SparkHelper sparkHelper;

    @Value("${epnoi.comparator.threshold}")
    Double threshold;

}
