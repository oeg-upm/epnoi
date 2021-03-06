package org.epnoi.hoarder.routes.common;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.epnoi.hoarder.routes.processors.ErrorHandler;
import org.epnoi.hoarder.routes.processors.TimeGenerator;
import org.epnoi.hoarder.routes.processors.UUIDGenerator;
import org.epnoi.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by cbadenes on 30/11/15.
 */
@Component
public class CommonRouteBuilder extends RouteBuilder{

    private static final Logger LOG = LoggerFactory.getLogger(CommonRouteBuilder.class);

    public static final String URI_HTTP_DOWNLOAD_TO_FILE        = "direct:common.http.download.file";

    public static final String URI_HTTP_DOWNLOAD                = "direct:common.http.download";

    public static final String URI_FILE_SAVE                    = "direct:common.file.save";

    @Autowired
    protected ErrorHandler errorHandler;

    @Autowired
    protected TimeGenerator timeClock;

    @Autowired
    protected UUIDGenerator uuidGenerator;

    @Value("${epnoi.hoarder.storage.path}")
    protected String basedir;

    @Override
    public void configure() throws Exception {

        onException(MalformedURLException.class)
                .process(errorHandler).stop();

        onException(IOException.class)
                .maximumRedeliveries(3)
                .process(errorHandler).stop();


        /*********************************************************************************************************************************
         * COMMON ROUTE 1:  Save metadata and retrieve resource by Http
         *********************************************************************************************************************************/
        from(URI_HTTP_DOWNLOAD_TO_FILE).
                process(timeClock).
                process(uuidGenerator).
                setHeader(Record.ARGUMENT_NAME,        simple("${property."+ Record.PUBLICATION_UUID+"}."+"${property."+ Record.PUBLICATION_METADATA_FORMAT+"}")).
                to(URI_FILE_SAVE).
                setHeader(Record.ARGUMENT_PATH,        simple("${property."+ Record.PUBLICATION_URL+"}")).
                to(URI_HTTP_DOWNLOAD).
                setHeader(Record.ARGUMENT_NAME,        simple("${property."+ Record.PUBLICATION_UUID+"}."+"${property."+ Record.PUBLICATION_FORMAT+"}")).
                to(URI_FILE_SAVE).
                setProperty(Record.PUBLICATION_URL_LOCAL, simple("${header." + Record.ARGUMENT_PATH + "}"));

        /*********************************************************************************************************************************
         * -> Save File
         *********************************************************************************************************************************/
        from(URI_FILE_SAVE).
                setHeader(Record.ARGUMENT_PATH, simple("${property." + Record.SOURCE_PROTOCOL + "}/${property." + Record.SOURCE_NAME + "}/${property" + Record.PUBLICATION_PUBLISHED_DATE + "}/${header." + Record.ARGUMENT_NAME + "}")).
                log(LoggingLevel.INFO,LOG,"File Saved: '${header."+ Record.ARGUMENT_PATH+"}'").
                to("file:" + basedir + "/?fileName=${header." + Record.ARGUMENT_PATH + "}&doneFileName=${file:name}.done");

        /*********************************************************************************************************************************
         * -> Download Resource by Http
         *********************************************************************************************************************************/
        from(URI_HTTP_DOWNLOAD).
                // Filter resources with available url
                filter(header(Record.ARGUMENT_PATH).isNotEqualTo("")).
                setHeader(Exchange.HTTP_METHOD, constant("GET")).
                setHeader(Exchange.HTTP_URI, simple("${header." + Record.ARGUMENT_PATH + "}")).
                to("http://dummyhost?throwExceptionOnFailure=false");


    }
}
