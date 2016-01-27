package org.epnoi.harvester.routes.file;

import es.cbadenes.lab.test.IntegrationTest;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by cbadenes on 31/12/15.
 */
@Category(IntegrationTest.class)
public class FileTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

    @Test
    public void oaipmhMessage() throws Exception {

        resultEndpoint.expectedMessageCount(3);

        Thread.sleep(60000);

//        template.sendBody(xml);
        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {

                Path path = Paths.get("src/test/resources/inbox/siggraph-2015");


                /**
                 * Route[[
                 * idempotentRepository=#fileStore]] -> [To[log:org.epnoi.harvester.routes.file.FileRouteMaker?level=INFO], SetProperty[epnoi.domain.uri, constant{}], SetProperty[epnoi.source.url, constant{file://siggraph-2006}], SetProperty[epnoi.source.uri, constant{http://epnoi.org/sources/48afa130-28c1-4bb3-bbc2-dac5da760fa1}], SetProperty[epnoi.source.protocol, constant{file}], SetProperty[epnoi.source.name, constant{siggraph-2006}], SetProperty[epnoi.publication.published, simple{${header.CamelFileLastModified}}], SetProperty[epnoi.publication.reference.format, constant{pdf}], SetProperty[epnoi.publication.format, constant{pdf}], SetProperty[epnoi.publication.url.local, simple{${header.CamelFileAbsolutePath}}], SetProperty[epnoi.publication.reference.url, simple{${header.CamelFileAbsolutePath}}], SetProperty[epnoi.publication.uri, simple{${header.CamelFileAbsolutePath}}], To[direct:common.ro.build]]]

                 */
                from("file://"+path.toFile().getAbsolutePath()+"?"+
                        "recursive=true&" +
                        "noop=true&"+
                        "chmod=777&" +
                        "delete=false&" +
                        "readLock=changed" +
                        "readLockCheckInterval=2000" +
                        "idempotent=true&" +
                        "idempotentKey=${file:name}-${file:size}").
                        to("log:org.epnoi.harvester.routes.FileTest?level=INFO").
                        to("mock:result");

            }
        };
    }

}