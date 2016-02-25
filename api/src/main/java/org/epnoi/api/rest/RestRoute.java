package org.epnoi.api.rest;

import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestsDefinition;

/**
 * Created by cbadenes on 25/02/16.
 */
public abstract class RestRoute {


    public abstract RestDefinition configure(RestsDefinition definitions);
}
