package org.epnoi.api.converters;

import org.apache.camel.Converter;
import org.apache.camel.TypeConversionException;
import org.epnoi.api.model.ContainsI;
import org.epnoi.model.Event;
import org.epnoi.model.domain.relations.Contains;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.File;

/**
 * Created by cbadenes on 11/02/16.
 */
@Converter
public class RelationalConverter {

    @Converter
    public static Contains toRelatio(ContainsI facade) throws TypeConversionException {
        return Relation.newContains(facade.getDomainUri(),facade.getDocumentUri());
    }
}
