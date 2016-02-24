package oeg.epnoi.parser.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.upf.taln.dri.lib.exception.DRIexception;
import es.cbadenes.lab.test.IntegrationTest;
import oeg.epnoi.parser.serializer.threadpool.MyMonitorThread;
import oeg.epnoi.parser.serializer.threadpool.MyRejectedExecutionHandlerImpl;
import oeg.epnoi.parser.serializer.threadpool.MyWork;
import oeg.epnoi.parser.serializer.threadpool.Reference;
import org.epnoi.model.modules.FolderUtils;
import org.epnoi.parser.annotator.upf.AnnotatedDocument;
import org.epnoi.parser.annotator.upf.UpfAnnotator;
import org.epnoi.parser.serializer.AnnotatedDocumentSerializer;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by cbadenes on 24/02/16.
 */
@Category(IntegrationTest.class)
public class SerializerTest {

    private static final Logger LOG = LoggerFactory.getLogger(SerializerTest.class);

    @Test
    public void serialize() throws DRIexception, InterruptedException {

        UpfAnnotator annotator = new UpfAnnotator();
        annotator.setDriConfigPath("src/test/resources/DRIconfig.properties");
        annotator.setup();


        List<String> files = FolderUtils.listFiles("/Users/cbadenes/Documents/OEG/Projects/DrInventor/datasets/2nd-review-xml/22papers");



        BlockingQueue<Runnable> worksQueue = new ArrayBlockingQueue<Runnable>(500);
        RejectedExecutionHandler executionHandler = new MyRejectedExecutionHandlerImpl();

        // Create the ThreadPoolExecutor
        ThreadPoolExecutor executor = new ThreadPoolExecutor(7, 7, 10, TimeUnit.SECONDS, worksQueue, executionHandler);
        executor.allowCoreThreadTimeOut(false);

        // Starting the monitor thread as a daemon
        Thread monitor = new Thread(new MyMonitorThread(executor));
        monitor.setDaemon(true);
        monitor.start();

        // Adding the tasks
        files.stream().filter(file -> !file.endsWith(".ser")).forEach(file -> executor.execute(new MyWork(file,annotator)));

        while (!executor.isTerminated()){
            Thread.sleep(20000);
            LOG.info("waiting for executor");
        }
    }

    @Test
    public void read() throws URISyntaxException, DRIexception, InterruptedException, IOException, NoSuchAlgorithmException {



        List<String> files = FolderUtils.listFiles("/Users/cbadenes/Documents/OEG/Projects/DrInventor/datasets/2nd-review-xml/22papers");


        // Adding the tasks
        List<String> jsonFiles = files.stream().filter(file -> file.endsWith("ser")).collect(Collectors.toList());

        List<Reference> references = new ArrayList<>();

        for (String jsonFile: jsonFiles){

            LOG.info("Trying to read annotate document from json: " + jsonFile);
            AnnotatedDocument document = AnnotatedDocumentSerializer.from(jsonFile).get();

            String s=new StringBuilder().append(document.getContent()).toString();

            MessageDigest m=MessageDigest.getInstance("MD5");

            m.update(s.getBytes(),0,s.length());

            String id = new BigInteger(1, m.digest()).toString(16);

            String uri = "http://drinventor.eu/documents/"+id;

            LOG.info("URI for: " + jsonFile + " is " + uri);

            Reference reference = new Reference();
            reference.setFileName(new File(jsonFile).getName());
            reference.setTitle(document.getTitle());
            reference.setUri(uri);
            references.add(reference);
        }

        ObjectMapper mapper = new ObjectMapper();
        File file = new File("documents.json");
        mapper.writeValue(file,references);

        LOG.info("Wrote: " + file.getAbsolutePath());

    }
}
