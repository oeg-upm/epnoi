package org.epnoi.harvester.executor;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.harvester.Config;
import org.epnoi.harvester.annotator.helper.ParserHelper;
import org.epnoi.model.domain.resources.File;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by cbadenes on 01/03/16.
 */
public class ParserTaskTest {


    @Test
    public void simulate(){

        File file = new File();
        file.setUrl("/opt/drinventor/workspace/ftp/siggraph/2006/a12-peng.pdf");
        file.setDomain("sample-domain");
        file.setSource("sample-source");

        ParserHelper helper = new ParserHelper();
        helper.setSerializationDirectory("/opt/drinventor/workspace/serialized");

        ParserTask task = new ParserTask(file,helper);
        task.run();

    }

}
