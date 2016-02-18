package org.epnoi.harvester.routes.converter;

import org.apache.camel.Converter;
import org.apache.camel.TypeConversionException;
import org.epnoi.model.Event;
import org.epnoi.model.domain.resources.File;

/**
 * Created by cbadenes on 11/02/16.
 */
@Converter
public class FileTypeConverter {

    @Converter
    public static byte[] toByteArray(File file) throws TypeConversionException {
        return Event.from(file).getBytes();
    }
}
