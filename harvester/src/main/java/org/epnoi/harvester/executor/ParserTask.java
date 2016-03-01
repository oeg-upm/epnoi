package org.epnoi.harvester.executor;

import com.google.common.base.Strings;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.epnoi.harvester.annotator.helper.ParserHelper;
import org.epnoi.harvester.annotator.upf.AnnotatedDocument;
import org.epnoi.harvester.serializer.AnnotatedDocumentSerializer;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.*;
import org.epnoi.model.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by cbadenes on 24/02/16.
 */
public class ParserTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ParserTask.class);

    @Getter
    private final File file;

    private final ParserHelper helper;

    public ParserTask(File file, ParserHelper helper){
        this.file = file;
        this.helper = helper;
    }

    @Override
    public void run() {
        AnnotatedDocument annotatedDocument = null;
        try{
            LOG.info("Processing file: " + file );

            // Domain URI
            String domainUri        = file.getDomain();

            // Source URI
            String sourceUri        = file.getSource();

            // Document File
            String path             = file.getUrl();


            // Check if exists a previously serialized file
            String serializedPath   = serializedPath(path);

            if (new java.io.File(serializedPath).exists()){
                // Load Serialized Document
                annotatedDocument = fromSerialized(serializedPath);
            }else if (path.endsWith(".ser")){
                // Serialized file
                annotatedDocument = fromSerialized(path)
            }else{
                // Parse document
                annotatedDocument = annotateDocument(path);
            }



            // Metainformation
            MetaInformation metaInformation = file.getMetainformation();

            // Attached file
            // TODO Handle multiple attached files


            String rawContent       = annotatedDocument.getContent();
//            Map<String, Integer> tokens   = textMiner.parse(rawContent).stream().filter(token -> token.isValid()).collect(Collectors.toConcurrentMap(w -> w.getLemma(), w -> 1, Integer::sum));

            // Update Metainfo if empty
            updateMetainfoFromAnnotatedDoc(metaInformation,annotatedDocument);

            // Check by title if exist in ddbb
            List<String> docs = helper.getUdm().find(Resource.Type.DOCUMENT).by(Document.TITLE,metaInformation.getTitle());

            if (docs != null && !docs.isEmpty()){
                LOG.warn("Document titled: '"+metaInformation.getTitle()+"' exists in ddbb with uri: " + docs);
                annotatedDocument.clean();
                return;
            }

            // Document
            Document document = createAndSaveDocument(metaInformation,path,annotatedDocument,sourceUri,domainUri);

            // Item
            Item item = createAndSaveItem(metaInformation,annotatedDocument,document.getUri());

            // Part:: Abstract
            Part abstractPart = createAndSavePart("abstract", annotatedDocument.getAbstractContent(), item.getUri());

            // Part:: Approach
            Part approachPart = createAndSavePart("approach", annotatedDocument.getApproachContent(), item.getUri());

            // Part:: Background
            Part backgroundPart = createAndSavePart("background", annotatedDocument.getBackgroundContent(), item.getUri());

            // Part:: Challenge
            Part challenge = createAndSavePart("challenge", annotatedDocument.getChallengeContent(), item.getUri());

            // Part:: Outcome
            Part outcome = createAndSavePart("outcome", annotatedDocument.getOutcomeContent(), item.getUri());

            // Part:: FutureWork
            Part futureWork = createAndSavePart("futureWork", annotatedDocument.getFutureWorkContent(), item.getUri());

            // Part:: Summary by Centroid
            Part summCentroid = createAndSavePart("summaryCentroid", annotatedDocument.getSummaryByCentroidContent(25), item.getUri());

            // Part:: Summary by Title Similarity
            Part summTitle = createAndSavePart("summaryTitle", annotatedDocument.getSummaryByTitleSimContent(25), item.getUri());

        }catch (RuntimeException e){
            LOG.error("Error processing resource: " + file, e);
        } finally {
            // free memory
            if (annotatedDocument != null) annotatedDocument.clean();
        }
    }

    private AnnotatedDocument fromSerialized(String path){
        AnnotatedDocument annotatedDocument;
        Optional<AnnotatedDocument> res = AnnotatedDocumentSerializer.from(path);
        if (res.isPresent()){
            annotatedDocument = res.get();
        } else{
            LOG.warn("Error deserializing document: " + path);
            annotatedDocument = annotateDocument(path);
        }
        return annotatedDocument;
    }

    private AnnotatedDocument annotateDocument(String path){
        AnnotatedDocument annotatedDocument = helper.getTextMiner().annotate(path);
        AnnotatedDocumentSerializer.to(annotatedDocument,serializedPath(path));
        return annotatedDocument;
    }

    private String serializedPath(String path){
        java.io.File inputFile = new java.io.File(path);
        return new StringBuilder().append(helper.getSerializationDirectory()).append(java.io.File.separator).append(inputFile.getName()).append(".ser").toString();
    }

    private void updateMetainfoFromAnnotatedDoc(MetaInformation metaInformation, AnnotatedDocument annotatedDocument){
        // -> Update Title from parser if empty
        if (Strings.isNullOrEmpty(metaInformation.getTitle())){
            String retrievedTitle = annotatedDocument.getTitle();
            metaInformation.setTitle(Strings.isNullOrEmpty(retrievedTitle)? "unknown" : retrievedTitle);
            LOG.info("Title from annotated document: " + metaInformation.getTitle());
        }
        // -> Update Published from parser if empty
        if (Strings.isNullOrEmpty(metaInformation.getPublished())){
            String retrievedDate = annotatedDocument.getYear();
            metaInformation.setPublished(Strings.isNullOrEmpty(retrievedDate)? "unknown" : retrievedDate);
            LOG.info("Published date from annotated document: " + metaInformation.getPublished());
        }
        // -> Update Authors from parser if empty
        if (Strings.isNullOrEmpty(metaInformation.getCreators())){
            metaInformation.setCreators(annotatedDocument.getAuthors().stream().map(author -> author.getFullName()).collect(Collectors.joining(";")));
            LOG.info("Authors from annotated document: " + metaInformation.getCreators());
        }
        // -> Update Format
        if (Strings.isNullOrEmpty(metaInformation.getPubFormat())){
            metaInformation.setPubFormat(FilenameUtils.getExtension(metaInformation.getSourceUrl()));
            LOG.info("Pub Format: " + metaInformation.getPubFormat());
        }
        // -> Update Language
        if (Strings.isNullOrEmpty(metaInformation.getLanguage())){
            metaInformation.setLanguage("en");
            LOG.info("Language: " + metaInformation.getLanguage());
        }
    }

    private Document createAndSaveDocument(MetaInformation metaInformation, String path, AnnotatedDocument annotatedDocument, String sourceUri, String domainUri){
        // Document
        Document document = Resource.newDocument();
        document.setUri(helper.getUriGenerator().basedOnContent(Resource.Type.DOCUMENT,annotatedDocument.getContent())); // Maybe better using PUBLICATION_URI
        document.setPublishedOn(metaInformation.getPublished());
        document.setPublishedBy(metaInformation.getSourceUri());
        document.setAuthoredOn(metaInformation.getAuthored());
        document.setAuthoredBy(metaInformation.getCreators());
        document.setContributedBy(metaInformation.getContributors());
        document.setRetrievedFrom(new java.io.File(path).getName());
        document.setRetrievedOn(TimeUtils.asISO());
        document.setFormat(metaInformation.getPubFormat());
        document.setLanguage(metaInformation.getLanguage());
        document.setTitle(metaInformation.getTitle());
        document.setSubject(metaInformation.getSubject());
        document.setDescription(annotatedDocument.getAbstractContent());
        document.setRights(metaInformation.getRights());
        document.setType(metaInformation.getType());
        document.setContent(annotatedDocument.getContent());

        String tokens   = helper.getTextMiner().parse(annotatedDocument.getContent()).stream().filter(token -> token.isValid()).map(token -> token.getLemma()).collect(Collectors.joining(" "));
        document.setTokens(tokens);

        helper.getUdm().save(document);
        // Relate it to Source
        helper.getUdm().save(Relation.newProvides(sourceUri,document.getUri()));
        // Relate it to Domain
        helper.getUdm().save(Relation.newContains(domainUri,document.getUri()));

        return document;
    }

    private Item createAndSaveItem(MetaInformation metaInformation, AnnotatedDocument document, String documentUri){
        Item item = Resource.newItem();
        item.setUri(helper.getUriGenerator().basedOnContent(Resource.Type.ITEM,document.getContent()));
        item.setAuthoredOn(metaInformation.getAuthored());
        item.setAuthoredBy(metaInformation.getCreators());
        item.setContributedBy(metaInformation.getContributors());
        item.setFormat(metaInformation.getFormat());
        item.setLanguage(metaInformation.getLanguage());
        item.setTitle(metaInformation.getTitle());
        item.setSubject(metaInformation.getSubject());
        item.setDescription(metaInformation.getDescription());
        item.setUrl(metaInformation.getPubURI());
        item.setType(metaInformation.getType());
        item.setContent(document.getContent());

        String tokens   = helper.getTextMiner().parse(item.getContent()).stream().filter(token -> token.isValid()).map(token -> token.getLemma()).collect(Collectors.joining(" "));
        item.setTokens(tokens);

        helper.getUdm().save(item);
        helper.getUdm().save(Relation.newBundles(documentUri,item.getUri()));

        return item;
    }

    private Part createAndSavePart(String sense, String rawContent, String itemUri) {
        Part part = Resource.newPart();
        part.setUri(helper.getUriGenerator().basedOnContent(Resource.Type.PART,rawContent + sense));
        part.setSense(sense);
        part.setContent(rawContent);

        String tokens   = helper.getTextMiner().parse(rawContent).stream().filter(token -> token.isValid()).map(token -> token.getLemma()).collect(Collectors.joining(" "));
        part.setTokens(tokens);

        helper.getUdm().save(part);
        helper.getUdm().save(Relation.newDescribes(part.getUri(),itemUri));

        return part;
    }
}
