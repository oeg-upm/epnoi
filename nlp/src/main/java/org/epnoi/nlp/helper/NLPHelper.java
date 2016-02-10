package org.epnoi.nlp.helper;

import lombok.Getter;
import org.epnoi.nlp.gate.ControllerCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 10/02/16.
 */
@Component
public class NLPHelper {

    @Getter
    @Value("${epnoi.nlp.gatePath}")
    String gatePath;

    @Getter
    @Value("${epnoi.nlp.numberOfProcessors}")
    Integer numberOfProcessors;

    @Getter
    @Value("${epnoi.nlp.content.min}")
    Integer minContentLength;

    @Getter
    @Autowired
    ControllerCreator controller;

}
