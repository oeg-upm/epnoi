package oeg.epnoi.parser.serializer.threadpool;

import org.epnoi.parser.annotator.upf.AnnotatedDocument;
import org.epnoi.parser.annotator.upf.UpfAnnotator;
import org.epnoi.parser.serializer.AnnotatedDocumentSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Optional;

/**
 * Created by cbadenes on 24/02/16.
 */
public class MyWork implements Runnable
{
    private static final Logger LOG = LoggerFactory.getLogger(MyWork.class);

    private final UpfAnnotator annotator;
    String name;

    public MyWork(String name, UpfAnnotator annotator)
    {
        this.name = name;
        this.annotator = annotator;
    }

    @Override
    public void run()
    {
        System.out.println(this.name + " : I'm running ! ");


        try {
            File serialized = new File(name + ".ser");

            Optional<AnnotatedDocument> optionalDocument;
            if (serialized.exists()){
                LOG.info("Trying to read annotate document from: " + serialized);
                optionalDocument = AnnotatedDocumentSerializer.from(serialized.getAbsolutePath());
            }else{
                LOG.info("Trying to annotate document: " + name + " from thread: " + Thread.currentThread());
                optionalDocument = Optional.of(annotator.annotate(name));
            }

            AnnotatedDocument document = optionalDocument.get();

            String s=new StringBuilder().append(document.getTitle()).append(document.getAbstractContent()).toString();

            MessageDigest m=MessageDigest.getInstance("MD5");

            m.update(s.getBytes(),0,s.length());

            String id = new BigInteger(1, m.digest()).toString(16);

            String uri = "http://drinventor.eu/documents/"+id;

            LOG.info("URI for: " + name + " is " + uri);

            if (!serialized.exists()){
                AnnotatedDocumentSerializer.to(document,serialized.getAbsolutePath());
            }

        } catch (Exception e){
            LOG.error("Error annotating file: " + name, e);
        }


        System.out.println(this.name + " : I'm done ! ");
    }

    @Override
    public String toString()
    {
        return (this.name);
    }
}