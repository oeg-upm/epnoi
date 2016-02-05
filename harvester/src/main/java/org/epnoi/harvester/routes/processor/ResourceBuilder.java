package org.epnoi.harvester.routes.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;
import org.epnoi.harvester.mining.TextMiner;
import org.epnoi.harvester.mining.annotation.AnnotatedDocument;
import org.epnoi.harvester.model.MetaInformation;
import org.epnoi.model.Record;
import org.epnoi.model.domain.*;
import org.epnoi.storage.UDM;
import org.epnoi.storage.generator.TimeGenerator;
import org.epnoi.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cbadenes on 01/12/15.
 */
@Component
public class ResourceBuilder implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceBuilder.class);

    private final ObjectMapper mapper;

    @Value("${epnoi.hoarder.storage.path}")
    protected String basedir;

    @Autowired
    UDM udm;

    @Autowired
    URIGenerator uriGenerator;

    @Autowired
    TimeGenerator timeGenerator;

    @Autowired
    TextMiner textMiner;

    public ResourceBuilder(){
        this.mapper = new ObjectMapper();
    }


    @Override
    public void process(Exchange exchange) throws Exception {

        try{
            // Domain URI
            String domainUri        = exchange.getProperty(Record.DOMAIN_URI,String.class);

            // Source URI
            String sourceUri        = exchange.getProperty(Record.SOURCE_URI,String.class);

            LOG.info("Processing resource: " + exchange + " from source: " + sourceUri + " and domain: " + domainUri);

            // Metainformation
            MetaInformation metaInformation = new MetaInformation(exchange);

            // Attached file
            // TODO Handle multiple attached files
            String path             = exchange.getProperty(Record.PUBLICATION_URL_LOCAL,String.class).replace("."+metaInformation.getPubFormat(), "."+metaInformation.getFormat());
            AnnotatedDocument annotatedDocument = textMiner.annotate(path);
            String rawContent       = annotatedDocument.getContent();
//            Map<String, Integer> tokens   = textMiner.parse(rawContent).stream().filter(token -> token.isValid()).collect(Collectors.toConcurrentMap(w -> w.getLemma(), w -> 1, Integer::sum));

            // Update Metainfo if empty
            updateMetainfoFromAnnotatedDoc(metaInformation,annotatedDocument);

            // Check by title if exist in ddbb
            List<String> docs = udm.find(Resource.Type.DOCUMENT).by(Document.TITLE,metaInformation.getTitle());

            if (docs != null && !docs.isEmpty()){
                LOG.warn("Document titled: '"+metaInformation.getTitle()+"' exists in ddbb with uri: " + docs);
                return;
            }

            // Document
            Document document = createAndSaveDocument(metaInformation,rawContent,sourceUri,domainUri);

            // Words
            // TODO
//            List<Word> words = tokens.keySet().stream().map(token -> createAndSaveWord(token)).collect(Collectors.toList());

            // Item
            Item item = createAndSaveItem(metaInformation,rawContent,document.getUri());
            // Mentions from Item
            // TODO
//            words.stream().forEach(word -> udm.relateWordToItem(word.getUri(),item.getUri(),tokens.get(word.getLemma()).longValue()));

            // Part:: Abstract
            Part abstractPart = createAndSavePart("abstract", annotatedDocument.getAbstractContent(), item.getUri());
            // Mentions from Part
            //TODO

            // Part:: Approach
            Part approachPart = createAndSavePart("approach", annotatedDocument.getApproachContent(), item.getUri());
            // Mentions from Part
            //TODO

            // Part:: Background
            Part backgroundPart = createAndSavePart("background", annotatedDocument.getBackgroundContent(), item.getUri());
            // Mentions from Part
            //TODO

            // Part:: Challenge
            Part challenge = createAndSavePart("challenge", annotatedDocument.getChallengeContent(), item.getUri());
            // Mentions from Part
            //TODO

            // Part:: Outcome
            Part outcome = createAndSavePart("outcome", annotatedDocument.getOutcomeContent(), item.getUri());
            // Mentions from Part
            //TODO

            // Part:: FutureWork
            Part futureWork = createAndSavePart("futureWork", annotatedDocument.getFutureWorkContent(), item.getUri());
            // Mentions from Part
            //TODO

            // Part:: Summary by Centroid
            Part summCentroid = createAndSavePart("summaryCentroid", annotatedDocument.getSummaryByCentroidContent(25), item.getUri());
            // Mentions from Part
            //TODO

            // Part:: Summary by Title Similarity
            Part summTitle = createAndSavePart("summaryTitle", annotatedDocument.getSummaryByTitleSimContent(25), item.getUri());
            // Mentions from Part
            //TODO

            // Relate it to Domain
            udm.attachFrom(domainUri).to(document.getUri()).by(Relation.Type.DOMAIN_CONTAINS_DOCUMENT,RelationProperties.builder().date(document.getCreationTime()).build());


            // Convert to json
            String json = mapper.writeValueAsString(document);

            // Put in camel flow
            exchange.getIn().setHeader("FileName", StringUtils.replace(StringUtils.substringAfterLast("file://"+path, basedir), metaInformation.getFormat(), "json"));
            exchange.getIn().setBody(json, String.class);
        }catch (RuntimeException e){
            LOG.error("Error creating resources", e);
        }
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

    }

    private Document createAndSaveDocument(MetaInformation metaInformation, String rawContent, String sourceUri, String domainUri){
        // Document
        Document document = new Document();
        document.setUri(uriGenerator.newFor(Resource.Type.DOCUMENT)); // Maybe better using PUBLICATION_URI
        document.setCreationTime(timeGenerator.asISO());
        document.setPublishedOn(metaInformation.getPublished());
        document.setPublishedBy(metaInformation.getSourceUri());
        document.setAuthoredOn(metaInformation.getAuthored());
        document.setAuthoredBy(metaInformation.getCreators());
        document.setContributedBy(metaInformation.getContributors());
        document.setRetrievedFrom(metaInformation.getSourceUrl());
        document.setRetrievedOn(timeGenerator.asISO()); //TODO hoarding time
        document.setFormat(metaInformation.getPubFormat());
        document.setLanguage(metaInformation.getLanguage());
        document.setTitle(metaInformation.getTitle());
        document.setSubject(metaInformation.getSubject());
        document.setDescription(metaInformation.getDescription());
        document.setRights(metaInformation.getRights());
        document.setType(metaInformation.getType());
        document.setContent(rawContent);

        String tokens   = textMiner.parse(rawContent).stream().filter(token -> token.isValid()).map(token -> token.getLemma()).collect(Collectors.joining(" "));
        document.setTokens(tokens);

        udm.save(Resource.Type.DOCUMENT).with(document);
        udm.attachFrom(sourceUri).to(document.getUri()).by(Relation.Type.SOURCE_PROVIDES_DOCUMENT,RelationProperties.builder().date(timeGenerator.asISO()).build());

        return document;
    }

    private Item createAndSaveItem(MetaInformation metaInformation, String rawContent, String documentUri){
        Item item = new Item();
        item.setUri(uriGenerator.newFor(Resource.Type.ITEM));
        item.setCreationTime(timeGenerator.asISO());
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
        item.setContent(rawContent);

        String tokens   = textMiner.parse(rawContent).stream().filter(token -> token.isValid()).map(token -> token.getLemma()).collect(Collectors.joining(" "));
        item.setTokens(tokens);

        udm.save(Resource.Type.ITEM).with(item);
        udm.attachFrom(documentUri).to(item.getUri()).by(Relation.Type.DOCUMENT_BUNDLES_ITEM,RelationProperties.builder().date(timeGenerator.asISO()).build());

        return item;
    }

    private Part createAndSavePart(String sense, String rawContent, String itemUri) {
        Part part = new Part();
        part.setUri(uriGenerator.newFor(Resource.Type.PART));
        part.setCreationTime(timeGenerator.asISO());
        part.setSense(sense);
        part.setContent(rawContent);

        String tokens   = textMiner.parse(rawContent).stream().filter(token -> token.isValid()).map(token -> token.getLemma()).collect(Collectors.joining(" "));
        part.setTokens(tokens);

        udm.save(Resource.Type.PART).with(part);
        udm.attachFrom(part.getUri()).to(itemUri).by(Relation.Type.PART_DESCRIBES_ITEM,RelationProperties.builder().date(timeGenerator.asISO()).build());

        return part;
    }
}
