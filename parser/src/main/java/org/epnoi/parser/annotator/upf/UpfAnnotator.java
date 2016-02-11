package org.epnoi.parser.annotator.upf;

import edu.upf.taln.dri.lib.Factory;
import edu.upf.taln.dri.lib.exception.DRIexception;
import edu.upf.taln.dri.lib.model.Document;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * Created by cbadenes on 07/01/16.
 */
@Component
public class UpfAnnotator {

    private static final Logger LOG = LoggerFactory.getLogger(UpfAnnotator.class);

    @Value("${epnoi.upf.miner.config}")
    String driConfigPath;

    @PostConstruct
    public void setup() throws DRIexception {

        LOG.info("Initializing UPF Text Mining Framework from: " + driConfigPath + " ..");

        // Set property file path
        Factory.setDRIPropertyFilePath(driConfigPath);

        // Enable bibliography entry parsing
        Factory.setEnableBibEntryParsing(false);

        // Initialize
        Factory.initFramework();

        LOG.info("UPF Text Mining Framework initialized successfully");
    }

    public AnnotatedDocument annotate(String documentPath) throws DRIexception {
        String extension = FilenameUtils.getExtension(documentPath.toString()).toLowerCase();
        Document document;
        switch(extension){
            case "pdf":
                LOG.info("parsing document as PDF document: " + documentPath);
                document = Factory.getPDFloader().parsePDF(documentPath);
                break;
            case "xml":
            case "htm":
            case "html":
                LOG.info("parsing document as structured document: " + documentPath);
                document = Factory.createNewDocument(documentPath);
                break;
            default:
                LOG.info("parsing document as plain text document: " + documentPath);
                document = Factory.getPlainTextLoader().parsePlainText(new File(documentPath));
                break;
        }
        if (document == null) throw new RuntimeException("Text Mining Library can not parse document: " + documentPath);
        return new AnnotatedDocument(document);
    }

}
