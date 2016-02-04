package org.epnoi.storage;

import org.epnoi.model.Event;
import org.epnoi.model.domain.Resource;
import org.epnoi.model.domain.*;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.RoutingKey;
import org.epnoi.storage.system.column.domain.*;
import org.epnoi.storage.system.column.repository.*;
import org.epnoi.storage.system.document.domain.*;
import org.epnoi.storage.system.document.repository.*;
import org.epnoi.storage.system.graph.domain.*;
import org.epnoi.storage.system.graph.domain.relationships.*;
import org.epnoi.storage.system.graph.repository.*;
import org.epnoi.storage.session.UnifiedSession;
import org.epnoi.storage.session.UnifiedTransaction;
import org.neo4j.ogm.session.Neo4jSession;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.session.result.ResultProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.cassandra.repository.support.BasicMapId;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by cbadenes on 23/12/15.
 */
@Component
public class UDM {

    private static final Logger LOG = LoggerFactory.getLogger(UDM.class);

    @Autowired
    EventBus eventBus;

    @Autowired
    UnifiedSession unifiedSession;

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    Session session;

    @Autowired
    SourceColumnRepository sourceColumnRepository;
    @Autowired
    SourceDocumentRepository sourceDocumentRepository;
    @Autowired
    SourceGraphRepository sourceGraphRepository;

    @Autowired
    DomainColumnRepository domainColumnRepository;
    @Autowired
    DomainDocumentRepository domainDocumentRepository;
    @Autowired
    DomainGraphRepository domainGraphRepository;

    @Autowired
    DocumentColumnRepository documentColumnRepository;
    @Autowired
    DocumentDocumentRepository documentDocumentRepository;
    @Autowired
    DocumentGraphRepository documentGraphRepository;

    @Autowired
    ItemColumnRepository itemColumnRepository;
    @Autowired
    ItemDocumentRepository itemDocumentRepository;
    @Autowired
    ItemGraphRepository itemGraphRepository;

    @Autowired
    PartColumnRepository partColumnRepository;
    @Autowired
    PartDocumentRepository partDocumentRepository;
    @Autowired
    PartGraphRepository partGraphRepository;

    @Autowired
    WordColumnRepository wordColumnRepository;
    @Autowired
    WordDocumentRepository wordDocumentRepository;
    @Autowired
    WordGraphRepository wordGraphRepository;

    @Autowired
    TopicColumnRepository topicColumnRepository;
    @Autowired
    TopicDocumentRepository topicDocumentRepository;
    @Autowired
    TopicGraphRepository topicGraphRepository;

    @Autowired
    AnalysisColumnRepository analysisColumnRepository;
    @Autowired
    AnalysisDocumentRepository analysisDocumentRepository;


    @Autowired
    ColumnRepository columnRepository;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    GraphRepository graphRepository;

    @Value("${epnoi.neo4j.contactpoints}")
    String neo4jHost;

    @Value("${epnoi.neo4j.port}")
    String neo4jPort;

    @PostConstruct
    public void setup(){
        this.session = (Neo4jSession) sessionFactory.openSession("http://"+neo4jHost+":"+neo4jPort);
    }

    /******************************************************************************
     * Save
     ******************************************************************************/


    // Compact Mode
    public void save(Resource resource, Resource.Type type){
        try{
            unifiedSession.clean();
            UnifiedTransaction transaction = unifiedSession.beginTransaction();

            LOG.debug("trying to save :" + resource);
            // column
            columnRepository.save(resource,type);
            // document
            documentRepository.save(resource,type);
            // graph
            graphRepository.save(resource, type);

            transaction.commit();

            LOG.info("Resource Saved: " + resource);
            //Publish the event
            eventBus.post(Event.from(resource), RoutingKey.of(type, Resource.State.CREATED));
        }catch (Exception e){
            LOG.error("Unexpected error while saving resource: "+resource,e);
        }
    }


    public void saveSource(Source source){
        try{
            session.clear();
            LOG.debug("trying to save :" + source);
            // column
            sourceColumnRepository.save(ResourceUtils.map(source, SourceColumn.class));
            // document
            sourceDocumentRepository.save(ResourceUtils.map(source, SourceDocument.class));
            // graph : TODO Set unique Long id for node
            SourceNode resource = sourceGraphRepository.save(ResourceUtils.map(source, SourceNode.class));
            LOG.info("Saved: " + source + " -> id:"+resource.getId());
            //Publish the event
            eventBus.post(Event.from(source), RoutingKey.of(Resource.Type.SOURCE, Resource.State.CREATED));
        }catch (Exception e){
            LOG.error("Unexpected error during save resource: "+source,e);
        }
    }

    public void saveDomain(Domain domain){
        try{
            session.clear();
            LOG.debug("trying to save :" + domain);
            // column
            domainColumnRepository.save(ResourceUtils.map(domain, DomainColumn.class));
            // document
            domainDocumentRepository.save(ResourceUtils.map(domain, DomainDocument.class));
            // graph : TODO Set unique Long id for node
            DomainNode resource = domainGraphRepository.save(ResourceUtils.map(domain, DomainNode.class));
            LOG.info("Saved:  " + domain+ " -> id:"+resource.getId());
            //Publish the event
            eventBus.post(Event.from(domain), RoutingKey.of(Resource.Type.DOMAIN, Resource.State.CREATED));
        }catch (Exception e){
            LOG.error("Unexpected error during save resource: "+domain,e);
        }
    }

    public void saveDocument(Document document, String sourceURI){
        try{
            session.clear();
            LOG.debug("trying to save :" + document + " from source: " + sourceURI);
            // column
            documentColumnRepository.save(ResourceUtils.map(document, DocumentColumn.class));

            // document
            Iterable<DomainNode> domains = domainGraphRepository.findBySource(sourceURI);
            String domain = (domains == null)? "" : StreamSupport.stream(domains.spliterator(),false).map(domainNode -> domainNode.getUri()).collect(Collectors.joining(","));
            DocumentDocument doc = ResourceUtils.map(document, DocumentDocument.class);
            doc.setDomain(domain);
            documentDocumentRepository.save(doc);

            // graph : TODO Set unique Long id for node
            DocumentNode resource = documentGraphRepository.save(ResourceUtils.map(document, DocumentNode.class));

            LOG.info("Saved:" + document+ " -> id:"+resource.getId());

            //Publish Document.Created
            eventBus.post(Event.from(document), RoutingKey.of(Resource.Type.DOCUMENT, Resource.State.CREATED));

            // relate to Source
            relateDocumentToSource(document.getUri(),sourceURI,document.getCreationTime());
        }catch (Exception e){
            LOG.error("Unexpected error during save resource: "+document,e);
        }
    }

    public void saveItem(Item item, String documentURI){
        try{
            session.clear();
            LOG.debug("trying to save :" + item + " from document: " + documentURI);
            // column
            itemColumnRepository.save(ResourceUtils.map(item, ItemColumn.class));

            // document
            Iterable<DomainNode> domains = domainGraphRepository.findByDocument(documentURI);
            String domain = (domains == null)? "" : StreamSupport.stream(domains.spliterator(),false).map(domainNode -> domainNode.getUri()).collect(Collectors.joining(","));
            ItemDocument doc = ResourceUtils.map(item, ItemDocument.class);
            doc.setDomain(domain);
            itemDocumentRepository.save(doc);

            // graph : TODO Set unique Long id for node
            ItemNode resource = itemGraphRepository.save(ResourceUtils.map(item, ItemNode.class));

            LOG.info("Saved:  " + item+ " -> id:"+resource.getId());

            //Publish Item.Created
            eventBus.post(Event.from(item), RoutingKey.of(Resource.Type.ITEM, Resource.State.CREATED));

            // relate to Document
            relateItemToDocument(item.getUri(),documentURI);
        }catch (Exception e){
            LOG.error("Unexpected error during save resource: "+item,e);
        }
    }

    public void savePart(Part part, String itemURI){
        try{
            session.clear();
            LOG.debug("trying to save :" + part + " from Item: " + itemURI);
            // column
            partColumnRepository.save(ResourceUtils.map(part, PartColumn.class));

            // document
            Iterable<DomainNode> domains = domainGraphRepository.findByItem(itemURI);
            String domain = (domains == null)? "" : StreamSupport.stream(domains.spliterator(),false).map(domainNode -> domainNode.getUri()).collect(Collectors.joining(","));
            PartDocument doc = ResourceUtils.map(part, PartDocument.class);
            doc.setDomain(domain);
            partDocumentRepository.save(doc);

            // graph : TODO Set unique Long id for node
            PartNode resource = partGraphRepository.save(ResourceUtils.map(part, PartNode.class));

            LOG.info("Saved:  " + part+ " -> id:"+resource.getId());

            //Publish the event
            eventBus.post(Event.from(part), RoutingKey.of(Resource.Type.PART, Resource.State.CREATED));

            // Relate to Item
            relateItemToPart(itemURI,part.getUri());

        }catch (Exception e){
            LOG.error("Unexpected error during save resource: "+part,e);
        }
    }

    public void saveWord(Word word){
        try{
            session.clear();
            LOG.debug("trying to save :" + word);
            // column
            wordColumnRepository.save(ResourceUtils.map(word, WordColumn.class));

            // document
            wordDocumentRepository.save(ResourceUtils.map(word, WordDocument.class));

            // graph : TODO Set unique Long id for node
            WordNode resource = wordGraphRepository.save(ResourceUtils.map(word, WordNode.class));
            LOG.info("Saved:  " + word+ " -> id:"+resource.getId());
            //Publish the event
            eventBus.post(Event.from(word), RoutingKey.of(Resource.Type.WORD, Resource.State.CREATED));

        }catch (Exception e){
            LOG.error("Unexpected error during save resource: "+word,e);
        }
    }

    public void saveTopic(Topic topic, String domainURI, String analysisURI){
        try{
            session.clear();
            LOG.debug("trying to save :" + topic);

            // column
            topicColumnRepository.save(ResourceUtils.map(topic, TopicColumn.class));

            // document
            topicDocumentRepository.save(ResourceUtils.map(topic, TopicDocument.class));

    //        Transaction transaction = session.beginTransaction();
    //        session.save(ResourceUtils.map(topic, TopicNode.class));
    //        transaction.commit();

            // graph : TODO Set unique Long id for node
            TopicNode resource = topicGraphRepository.save(ResourceUtils.map(topic, TopicNode.class));

            LOG.info("Saved:  " + topic+ " -> id:"+resource.getId());

            //Publish the event
            eventBus.post(Event.from(topic), RoutingKey.of(Resource.Type.TOPIC, Resource.State.CREATED));

            // Relate topic to domain
            relateDomainToTopic(domainURI, topic.getUri(), topic.getCreationTime(), analysisURI);

        }catch (Exception e){
            LOG.error("Unexpected error during save resource: "+topic,e);
        }
    }

    public void saveAnalysis(Analysis analysis){
        try{
            session.clear();
            LOG.debug("trying to save :" + analysis);
            // column
            analysisColumnRepository.save(ResourceUtils.map(analysis, AnalysisColumn.class));
            // document
            analysisDocumentRepository.save(ResourceUtils.map(analysis, AnalysisDocument.class));
            LOG.info("Saved:  " + analysis);
            //Publish the event
            eventBus.post(Event.from(analysis), RoutingKey.of(Resource.Type.ANALYSIS, Resource.State.CREATED));
        }catch (Exception e){
            LOG.error("Unexpected error during save resource: "+analysis,e);
        }
    }

    /******************************************************************************
     * Exist
     ******************************************************************************/

    // Compact Mode
    public boolean exists(String uri, Resource.Type type){
        return columnRepository.exists(uri,type);
    }


    public boolean existSource(String uri){
        return sourceColumnRepository.exists(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public boolean existDomain(String uri){
        return domainColumnRepository.exists(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public boolean existDocument(String uri){
        return documentColumnRepository.exists(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public boolean existItem(String uri){
        return itemColumnRepository.exists(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public boolean existPart(String uri){
        return partColumnRepository.exists(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public boolean existWord(String uri){
        return wordColumnRepository.exists(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public boolean existAnalysis(String uri){
        return analysisColumnRepository.exists(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public boolean existTopic(String uri){
        return topicColumnRepository.exists(BasicMapId.id(ResourceUtils.URI,uri));
    }

    /******************************************************************************
     * Read
     ******************************************************************************/

    // Compact Mode
    public Optional<Resource> read(String uri, Resource.Type type){
        return columnRepository.read(uri,type);
    }


    public Optional<Source> readSource(String uri){
        SourceColumn result = sourceColumnRepository.findOne(BasicMapId.id(ResourceUtils.URI, uri));
        if (result == null) return Optional.empty();
        return Optional.of(ResourceUtils.map(result, Source.class));
    }

    public Optional<Domain> readDomain(String uri){
        DomainColumn result = domainColumnRepository.findOne(BasicMapId.id(ResourceUtils.URI, uri));
        if (result == null) return Optional.empty();
        return Optional.of(ResourceUtils.map(result, Domain.class));
    }

    public Optional<Document> readDocument(String uri){
        DocumentColumn result = documentColumnRepository.findOne(BasicMapId.id(ResourceUtils.URI, uri));
        if (result == null) return Optional.empty();
        return Optional.of(ResourceUtils.map(result, Document.class));
    }

    public Optional<Item> readItem(String uri){
        ItemColumn result = itemColumnRepository.findOne(BasicMapId.id(ResourceUtils.URI, uri));
        if (result == null) return Optional.empty();
        return Optional.of(ResourceUtils.map(result, Item.class));
    }

    public Optional<Part> readPart(String uri){
        PartColumn result = partColumnRepository.findOne(BasicMapId.id(ResourceUtils.URI, uri));
        if (result == null) return Optional.empty();
        return Optional.of(ResourceUtils.map(result, Part.class));
    }

    public Optional<Word> readWord(String uri){
        WordColumn result = wordColumnRepository.findOne(BasicMapId.id(ResourceUtils.URI, uri));
        if (result == null) return Optional.empty();
        return Optional.of(ResourceUtils.map(result, Word.class));
    }

    public Optional<Topic> readTopic(String uri){
        TopicColumn result = topicColumnRepository.findOne(BasicMapId.id(ResourceUtils.URI, uri));
        if (result == null) return Optional.empty();
        return Optional.of(ResourceUtils.map(result, Topic.class));
    }

    public Optional<Analysis> readAnalysis(String uri){
        AnalysisColumn result = analysisColumnRepository.findOne(BasicMapId.id(ResourceUtils.URI, uri));
        if (result == null) return Optional.empty();
        return Optional.of(ResourceUtils.map(result, Analysis.class));
    }

    /******************************************************************************
     * Relate
     ******************************************************************************/
    // TODO review relations:: relation.ID to avoid duplicates

    // Compact Mode
    public void relate(String uri1, String uri2, Relation.Type type, Relation.Properties properties){
        try{
            unifiedSession.clean();
            UnifiedTransaction transaction = unifiedSession.beginTransaction();
            Resource resource = graphRepository.relate(uri1,uri2,type,properties);
            transaction.commit();

            //Publish the event
            eventBus.post(Event.from(ResourceUtils.map(resource,type.getStart().classOf())), RoutingKey.of(type.getStart(), Resource.State.UPDATED));
        }catch (ResultProcessingException e){
            LOG.warn("Creating relation between:["+uri1+"-"+uri2+"]",e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error during relation " + type + " between '"+uri1 +"' and '"+uri2+"'",e);
        }
    }

    // Compact Mode
    public void unrelate(String uri1, String uri2, Relation.Type type){
        try{
            unifiedSession.clean();
            UnifiedTransaction transaction = unifiedSession.beginTransaction();
            Resource resource = graphRepository.unrelate(uri1,uri2,type);
            transaction.commit();

            //Publish the event
            eventBus.post(Event.from(ResourceUtils.map(resource,type.getStart().classOf())), RoutingKey.of(type.getStart(), Resource.State.UPDATED));
        }catch (ResultProcessingException e){
            LOG.warn("Removing relation between:["+uri1+"-"+uri2+"]",e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error during removing relation " +type+" between '"+uri1 +"' and '"+uri2+"'",e);
        }
    }

    public void relateDocumentToSource(String documentURI, String sourceURI, String date){
        LOG.debug("Trying to relate document: " + documentURI + " to source: " + sourceURI + " in: " + date);
        try {
            session.clear();
            // Document
            DocumentNode documentNode = documentGraphRepository.findOneByUri(documentURI);
            // Source
            SourceNode sourceNode = sourceGraphRepository.findOneByUri(sourceURI);

            DocumentProvidedBySource relation = new DocumentProvidedBySource();
            relation.setDate(date);
            relation.setSource(sourceNode);
            relation.setDocument(documentNode);
            sourceNode.addDocumentProvidedBySource(relation);
            LOG.debug("Saving relation: " + relation);
            SourceNode resource = sourceGraphRepository.save(sourceNode);
            LOG.info("Related: Document[" + documentURI + "] to Source[" + sourceURI + "] -> id:"+relation.getId()+" in: " + date);

            //Publish the event
            eventBus.post(Event.from(ResourceUtils.map(resource,Source.class)), RoutingKey.of(Resource.Type.SOURCE, Resource.State.UPDATED));
        }catch (ResultProcessingException e){
            // TODO Avoid this by transaction
            LOG.warn("Creating relation between:["+documentURI+"-"+sourceURI+"]",e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error during relation of '"+documentURI +"' with '"+sourceURI+"'",e);
        }
    }

    public void relateDocumentToDomain(String documentURI, String domainURI, String date){
        LOG.debug("Trying to relate document: " + documentURI + " to domain: " + domainURI + " in: " + date);
        try {
            session.clear();
            // Document
            DocumentNode documentNode = documentGraphRepository.findOneByUri(documentURI);
            // Domain
            DomainNode domainNode = domainGraphRepository.findOneByUri(domainURI);

            ContainedDocument relation = new ContainedDocument();
            relation.setDocument(documentNode);
            relation.setDomain(domainNode);
            relation.setDate(date);
            domainNode.addContainedDocument(relation);
            LOG.debug("Saving relation: " + relation);
            DomainNode resource = domainGraphRepository.save(domainNode);
            LOG.info("Related: Document[" + documentURI + "] to Domain[" + domainURI + "] -> id:"+relation.getId()+" in: " + date);

            //Publish the event
            eventBus.post(Event.from(ResourceUtils.map(resource,Domain.class)), RoutingKey.of(Resource.Type.DOMAIN, Resource.State.UPDATED));
        }catch (ResultProcessingException e){
            // TODO Avoid this by transaction
            LOG.warn("Creating relation between:["+documentURI+"-"+domainURI+"]",e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error during relation of '"+documentURI +"' with '"+domainURI+"'",e);
        }
    }

    public void relateDocumentToDocument(String documentURI1, String documentURI2, Double weight, String domainURI){
        LOG.debug("Trying to relate document: " + documentURI1 + " to document: " + documentURI2 + " with weight: " + weight + " and domain: " + domainURI);
        try {
            session.clear();
            // Document
            DocumentNode documentNode1 = documentGraphRepository.findOneByUri(documentURI1);
            // Document
            DocumentNode documentNode2 = documentGraphRepository.findOneByUri(documentURI2);


            SimilarDocument relation = new SimilarDocument();
            relation.setX(documentNode1);
            relation.setY(documentNode2);
            relation.setWeight(weight);
            relation.setDomain(domainURI);
            documentNode1.addSimilarDocument(relation);
            LOG.debug("Saving relation: " + relation);
            DocumentNode resource = documentGraphRepository.save(documentNode1);

            LOG.info("Related: Document[" + documentURI1 + "] to Document[" + documentURI2+"] -> id:"+relation.getId());

            //Publish the events
            eventBus.post(Event.from(ResourceUtils.map(resource,Document.class)), RoutingKey.of(Resource.Type.DOCUMENT, Resource.State.UPDATED));
        }catch (ResultProcessingException e){
            // TODO Avoid this by transaction
            LOG.warn("Creating relation between:["+documentURI1+"-"+documentURI2+"]",e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error during relation of '"+documentURI1 +"' with '"+documentURI2+"'",e);
        }
    }

    public void relateItemToDocument(String itemURI, String documentURI){
        LOG.debug("Trying to relate item: " + itemURI + " to document: " + documentURI);
        try {
            session.clear();
            // Item
            ItemNode itemNode = itemGraphRepository.findOneByUri(itemURI);
            // Document
            DocumentNode documentNode = documentGraphRepository.findOneByUri(documentURI);

            ItemBundledByDocument relation = new ItemBundledByDocument();
            relation.setDocument(documentNode);
            relation.setItem(itemNode);

            documentNode.addItemBundledByDocument(relation);
            LOG.debug("Saving relation: " + relation);
            DocumentNode resource = documentGraphRepository.save(documentNode);
            LOG.info("Related: Item[" + itemURI + "] to Document[" + documentURI+"] -> id:"+relation.getId());

            //Publish the event
            eventBus.post(Event.from(ResourceUtils.map(resource,Document.class)), RoutingKey.of(Resource.Type.DOCUMENT, Resource.State.UPDATED));
        }catch (ResultProcessingException e){
            // TODO Avoid this by transaction
            LOG.warn("Creating relation between:["+itemURI+"-"+documentURI+"]",e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error during relation of '"+itemURI +"' with '"+documentURI+"'",e);
        }
    }

    public void relateItemToItem(String itemURI1, String itemURI2, Double weight, String domainURI){
        LOG.debug("Trying to relate item: " + itemURI1 + " to item: " + itemURI2+ " with weight: " + weight + " in domain: " + domainURI);
        try {
            session.clear();
            // Item
            ItemNode itemNode1 = itemGraphRepository.findOneByUri(itemURI1);
            // Item
            ItemNode itemNode2 = itemGraphRepository.findOneByUri(itemURI2);

            SimilarItem relation = new SimilarItem();
            relation.setX(itemNode1);
            relation.setY(itemNode2);
            relation.setWeight(weight);
            relation.setDomain(domainURI);
            itemNode1.addSimilarItem(relation);
            LOG.debug("Saving relation: " + relation);
            ItemNode resource = itemGraphRepository.save(itemNode1);
            LOG.info("Related: Item[" + itemURI1 + "] to Item[" + itemURI2+"] -> id:"+relation.getId());

            //Publish the event
            eventBus.post(Event.from(ResourceUtils.map(resource,Item.class)), RoutingKey.of(Resource.Type.ITEM, Resource.State.UPDATED));
        }catch (ResultProcessingException e){
            // TODO Avoid this by transaction
            LOG.warn("Creating relation between:["+itemURI1+"-"+itemURI2+"]",e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error during relation of '"+itemURI1 +"' with '"+itemURI2+"'",e);
        }
    }


    public void relatePartToPart(String partURI1, String partURI2, Double weight, String domainURI){
        LOG.debug("Trying to relate part: " + partURI1 + " to part: " + partURI2 + " with weight: " + weight + " in domain: " + domainURI);
        try {
            session.clear();
            // Part
            PartNode partNode1 = partGraphRepository.findOneByUri(partURI1);
            // Part
            PartNode partNode2 = partGraphRepository.findOneByUri(partURI2);


            SimilarPart relation = new SimilarPart();
            relation.setX(partNode1);
            relation.setY(partNode2);
            relation.setWeight(weight);
            relation.setDomain(domainURI);
            partNode1.addSimilarPart(relation);
            LOG.debug("Saving relation: " + relation);
            PartNode resource = partGraphRepository.save(partNode1);

            LOG.info("Related: Part[" + partURI1 + "] to Part[" + partURI2+"] -> id:"+relation.getId());

            //Publish the event
            eventBus.post(Event.from(ResourceUtils.map(resource,Part.class)), RoutingKey.of(Resource.Type.PART, Resource.State.UPDATED));
        }catch (ResultProcessingException e){
            // TODO Avoid this by transaction
            LOG.warn("Creating relation between:["+partURI1+"-"+partURI2+"]",e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error during relation of '"+partURI1 +"' with '"+partURI2+"'",e);
        }
    }



    public void relateItemToPart(String itemURI, String partURI){
        LOG.debug("Trying to relate Part: " + partURI + " to Item: " + itemURI);
        try {
            session.clear();
            // Part
            PartNode partNode = partGraphRepository.findOneByUri(partURI);
            // Item
            ItemNode itemNode = itemGraphRepository.findOneByUri(itemURI);

            ItemDescribedByPart relation = new ItemDescribedByPart();
            relation.setItem(itemNode);
            relation.setPart(partNode);
            partNode.addItemDescribedByPart(relation);
            LOG.debug("Saving relation: " + relation);
            PartNode resource = partGraphRepository.save(partNode);
            LOG.info("Related: Part[" + partURI + "] to Item[" + itemURI+"] -> id:"+relation.getId());

            //Publish the event
            eventBus.post(Event.from(ResourceUtils.map(resource,Part.class)), RoutingKey.of(Resource.Type.PART, Resource.State.UPDATED));
        }catch (ResultProcessingException e){
            // TODO Avoid this by transaction
            LOG.warn("Creating relation between:["+itemURI+"-"+partURI+"]",e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error during relation of '"+itemURI +"' with '"+partURI+"'",e);
        }
    }

    public void relateWordToItem(String wordURI, String itemURI, Long times){
        LOG.debug("Trying to relate word: " + wordURI + " to item: " + itemURI+ " with times: " + times);
        try {
            session.clear();
            ItemNode item = itemGraphRepository.findOneByUri(itemURI);
            WordNode word = wordGraphRepository.findOneByUri(wordURI);

            WordMentionedByItem relation = new WordMentionedByItem();
            relation.setItem(item);
            relation.setWord(word);
            relation.setTimes(times);
            item.addWordMentionedByItem(relation);
            LOG.debug("Saving relation: " + relation);
            ItemNode resource = itemGraphRepository.save(item);
            LOG.info("Related: Word[" + wordURI + "] to Item[" + itemURI+"] -> id:"+relation.getId());

            //Publish the event
            eventBus.post(Event.from(ResourceUtils.map(resource,Item.class)), RoutingKey.of(Resource.Type.ITEM, Resource.State.UPDATED));
        }catch (ResultProcessingException e){
            // TODO Avoid this by transaction
            LOG.warn("Creating relation between:["+wordURI+"-"+itemURI+"]",e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error during relation of '"+wordURI +"' with '"+itemURI+"'",e);
        }
    }

    public void relateWordToPart(String wordURI, String partURI, Long times){
        LOG.debug("Trying to relate word: " + wordURI + " to part: " + partURI+ " with times: " + times );
        try {
            session.clear();
            PartNode part = partGraphRepository.findOneByUri(partURI);
            WordNode word = wordGraphRepository.findOneByUri(wordURI);

            WordMentionedByPart relation = new WordMentionedByPart();
            relation.setPart(part);
            relation.setWord(word);
            relation.setTimes(times);
            part.addWordMentionedByPart(relation);
            LOG.debug("Saving relation: " + relation);
            PartNode resource = partGraphRepository.save(part);
            LOG.info("Related: Word[" + wordURI + "] to Part[" + partURI+"] -> id:"+relation.getId());

            //Publish the event
            eventBus.post(Event.from(ResourceUtils.map(resource,Part.class)), RoutingKey.of(Resource.Type.PART, Resource.State.UPDATED));
        }catch (ResultProcessingException e){
            // TODO Avoid this by transaction
            LOG.warn("Creating relation between:["+wordURI+"-"+partURI+"]",e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error during relation of '"+wordURI +"' with '"+partURI+"'",e);
        }
    }

    public void relateWordToWord(String wordURI1, String wordURI2, Double weight, String domainURI){
        LOG.debug("Trying to relate word: " + wordURI1 + " to word: " + wordURI2 + " with weight: " + weight + " and domain: " + domainURI);
        try {
            session.clear();
            WordNode word1 = wordGraphRepository.findOneByUri(wordURI1);
            WordNode word2 = wordGraphRepository.findOneByUri(wordURI2);

            PairedWord relation = new PairedWord();
            relation.setX(word1);
            relation.setY(word2);
            relation.setWeight(weight);
            relation.setDomain(domainURI);
            word1.addPairedWord(relation);
            LOG.debug("Saving relation: " + relation);
            WordNode resource = wordGraphRepository.save(word1);
            LOG.info("Related: Word[" + wordURI1 + "] to Word[" + wordURI2 + "] -> id:" + relation.getId());

            //Publish the event
            eventBus.post(Event.from(ResourceUtils.map(resource, Word.class)), RoutingKey.of(Resource.Type.WORD, Resource.State.UPDATED));
        }catch (ResultProcessingException e){
            // TODO Avoid this by transaction
            LOG.warn("Creating relation between:["+wordURI1+"-"+wordURI2+"]",e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error during relation of '"+wordURI1 +"' with '"+wordURI2+"'",e);
        }
    }

    public void relateTopicToDocument(String topicURI, String documentURI, Double weight){
        LOG.debug("Trying to relate topic: " + topicURI + " to document: " + documentURI+ " with weight: " + weight);
        try {
            session.clear();
            DocumentNode document = documentGraphRepository.findOneByUri(documentURI);
            TopicNode topic = topicGraphRepository.findOneByUri(topicURI);

            TopicDealtByDocument relation = new TopicDealtByDocument();
            relation.setDocument(document);
            relation.setTopic(topic);
            relation.setWeight(weight);
            document.addTopicDealtByDocument(relation);
            LOG.debug("Saving relation: " + relation);
            DocumentNode resource = documentGraphRepository.save(document);
            LOG.info("Related: Topic[" + topicURI + "] to Document[" + documentURI+"] -> id:"+relation.getId());

            //Publish the event
            eventBus.post(Event.from(ResourceUtils.map(resource,Document.class)), RoutingKey.of(Resource.Type.DOCUMENT, Resource.State.UPDATED));
        }catch (ResultProcessingException e){
            // TODO Avoid this by transaction
            LOG.warn("Creating relation between:["+topicURI+"-"+documentURI+"]",e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error during relation of '"+topicURI +"' with '"+documentURI+"'",e);
        }
    }

    public void relateTopicToItem(String topicURI, String itemURI, Double weight){
        LOG.debug("Trying to relate topic: " + topicURI + " to item: " + itemURI+ " with weight: " + weight );
        try {
            session.clear();
            ItemNode item = itemGraphRepository.findOneByUri(itemURI);
            TopicNode topic = topicGraphRepository.findOneByUri(topicURI);

            TopicDealtByItem relation = new TopicDealtByItem();
            relation.setItem(item);
            relation.setTopic(topic);
            relation.setWeight(weight);
            item.addTopicDealtByItem(relation);
            LOG.debug("Saving relation: " + relation);
            ItemNode resource = itemGraphRepository.save(item);
            LOG.info("Related: Topic[" + topicURI + "] to Item[" + itemURI+"] -> id:"+relation.getId());

            //Publish the event
            eventBus.post(Event.from(ResourceUtils.map(resource,Item.class)), RoutingKey.of(Resource.Type.ITEM, Resource.State.UPDATED));
        }catch (ResultProcessingException e){
            // TODO Avoid this by transaction
            LOG.warn("Creating relation between:["+topicURI+"-"+itemURI+"]",e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error during relation of '"+topicURI +"' with '"+itemURI+"'",e);
        }
    }

    public void relateTopicToPart(String topicURI, String partURI, Double weight){
        LOG.debug("Trying to relate topic: " + topicURI + " to part: " + partURI + " with weight: " + weight );
        try {
            session.clear();
            PartNode part = partGraphRepository.findOneByUri(partURI);
            TopicNode topic = topicGraphRepository.findOneByUri(topicURI);

            TopicDealtByPart relation = new TopicDealtByPart();
            relation.setPart(part);
            relation.setTopic(topic);
            relation.setWeight(weight);
            part.addTopicDealtByPart(relation);
            LOG.debug("Saving relation: " + relation);
            PartNode resource = partGraphRepository.save(part);
            LOG.info("Related: Topic[" + topicURI + "] to Part[" + partURI+"] -> id:"+relation.getId());

            //Publish the event
            eventBus.post(Event.from(ResourceUtils.map(resource,Part.class)), RoutingKey.of(Resource.Type.PART, Resource.State.UPDATED));
        }catch (ResultProcessingException e){
            // TODO Avoid this by transaction
            LOG.warn("Creating relation between:["+topicURI+"-"+partURI+"]",e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error during relation of '"+topicURI +"' with '"+partURI+"'",e);
        }
    }

    public void relateWordToTopic(String wordURI, String topicURI, Double weight){
        LOG.debug("Trying to relate word: " + wordURI + " to topic: " + topicURI+ " with weight: " + weight);
        try {
            session.clear();
            TopicNode topic = topicGraphRepository.findOneByUri(topicURI);
            WordNode word = wordGraphRepository.findOneByUri(wordURI);

            WordMentionedByTopic relation = new WordMentionedByTopic();
            relation.setTopic(topic);
            relation.setWord(word);
            relation.setWeight(weight);
            topic.addWordMentionedByTopic(relation);
            LOG.debug("Saving relation: " + relation);
            TopicNode resource = topicGraphRepository.save(topic);
            LOG.info("Related: Word[" + wordURI + "] to Topic[" + topicURI+"] -> id:"+relation.getId());

            //Publish the event
            eventBus.post(Event.from(ResourceUtils.map(resource,Topic.class)), RoutingKey.of(Resource.Type.TOPIC, Resource.State.UPDATED));
        }catch (ResultProcessingException e){
            // TODO Avoid this by transaction
            LOG.warn("Creating relation between:["+wordURI+"-"+topicURI+"]",e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error during relation of '"+wordURI +"' with '"+topicURI+"'",e);
        }
    }

    public void relateWordToDomain(String wordURI, String domainURI, String vector){
        LOG.debug("Trying to relate word: " + wordURI + " to domain: " + domainURI+ " with vector: " + vector);
        try {
            session.clear();
            DomainNode domain = domainGraphRepository.findOneByUri(domainURI);
            WordNode word = wordGraphRepository.findOneByUri(wordURI);

            WordDocument doc = wordDocumentRepository.findOne(wordURI);
            String base = (doc.getDomain() != null)? doc.getDomain() : "";
            doc.setDomain(new StringBuilder(base).append(domain.getUri()).toString());
            wordDocumentRepository.save(doc);


            DomainInWord relation = new DomainInWord();
            relation.setDomain(domain);
            relation.setWord(word);
            relation.setVector(vector);
            word.addDomainInWord(relation);
            LOG.debug("Saving relation: " + relation);
            WordNode resource = wordGraphRepository.save(word);
            LOG.info("Related: Word[" + wordURI + "] to Domain[" + domainURI+"] -> id:"+relation.getId());

            //Publish the event
            eventBus.post(Event.from(ResourceUtils.map(resource,Word.class)), RoutingKey.of(Resource.Type.WORD, Resource.State.UPDATED));
        }catch (ResultProcessingException e){
            // TODO Avoid this by transaction
            LOG.warn("Creating relation between:["+wordURI+"-"+domainURI+"]",e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error during relation of '"+wordURI +"' with '"+domainURI+"'",e);
        }
    }

    public void relateDomainToTopic(String domainURI, String topicURI, String date, String analysisURI){
        LOG.debug("Trying to relate domain: " + domainURI + " to topic: " + topicURI+ " in date: " + date + " by analysis: " + analysisURI);
        try {
            session.clear();
            TopicNode topic = topicGraphRepository.findOneByUri(topicURI);
            DomainNode domain = domainGraphRepository.findOneByUri(domainURI);

            TopicDocument topicDoc = topicDocumentRepository.findOne(topicURI);
            String base = (topicDoc.getDomain() != null)? topicDoc.getDomain() : "";
            topicDoc.setDomain(new StringBuilder(base).append(domain.getUri()).toString());
            topicDocumentRepository.save(topicDoc);


            DomainInTopic relation = new DomainInTopic();
            relation.setTopic(topic);
            relation.setDomain(domain);
            relation.setDate(date);
            relation.setAnalysis(analysisURI);

            topic.addDomainInTopic(relation);
            LOG.debug("Saving relation: " + relation);
            TopicNode resource = topicGraphRepository.save(topic);
            LOG.info("Related: Domain[" + domainURI + "] to Topic[" + topicURI+"] -> id:"+relation.getId());

            //Publish the event
            eventBus.post(Event.from(ResourceUtils.map(resource,Topic.class)), RoutingKey.of(Resource.Type.TOPIC, Resource.State.UPDATED));
        }catch (ResultProcessingException e){
            // TODO Avoid this by transaction
            LOG.warn("Creating relation between:["+domainURI+"-"+topicURI+"]",e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error during relation of '"+domainURI +"' with '"+topicURI+"'",e);
        }
    }

    public void relateDomainToSource(String domainURI, String sourceURI, String date){
        LOG.debug("Trying to relate Domain: " + domainURI + " to Source: " + sourceURI+ " in date: " + date);
        try {
            session.clear();
            DomainNode domain = domainGraphRepository.findOneByUri(domainURI);
            SourceNode source = sourceGraphRepository.findOneByUri(sourceURI);

            DomainComposedBySource relation = new DomainComposedBySource();
            relation.setSource(source);
            relation.setDomain(domain);
            relation.setDate(date);

            source.addDomainComposedBySource(relation);
            LOG.debug("Saving relation: " + relation);
            SourceNode resource = sourceGraphRepository.save(source);
            LOG.info("Related: Domain[" + domainURI + "] to Source[" + sourceURI+"] -> id:"+relation.getId());

            //Publish the event
            eventBus.post(Event.from(ResourceUtils.map(resource,Source.class)), RoutingKey.of(Resource.Type.SOURCE, Resource.State.UPDATED));
        }catch (ResultProcessingException e){
            // TODO Avoid this by transaction
            LOG.warn("Creating relation between:["+domainURI+"-"+sourceURI+"]",e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error during relation of '"+domainURI +"' with '"+sourceURI+"'",e);
        }
    }


    /******************************************************************************
     * Find
     ******************************************************************************/

    // Compact Mode
    public List<String> findAll(Resource.Type type){
        LOG.debug("Finding " + type.name() + "s");
        List<String> uris = new ArrayList<>();
        try{
            unifiedSession.clean();
            UnifiedTransaction transaction = unifiedSession.beginTransaction();

            columnRepository.findAll(type).forEach(x -> uris.add(x.getUri()));
            transaction.commit();
            LOG.info(type.name() + "s: " + uris);

        }catch (ResultProcessingException e){
            LOG.warn("getting all " + type,e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error while getting all " + type,e);
        }
        return uris;
    }

    // Compact Mode

    /**
     * Filter by other resource
     * @param resultType
     * @param referenceType
     * @param referenceURI
     * @return
     */
    public List<String> findIn(Resource.Type resultType, Resource.Type referenceType, String referenceURI){
        LOG.debug("Finding " + resultType.name() + "s in " + referenceType + ": " + referenceURI);
        List<String> uris = new ArrayList<>();
        try{
            unifiedSession.clean();
            UnifiedTransaction transaction = unifiedSession.beginTransaction();
            graphRepository.findIn(resultType, referenceType,referenceURI).forEach(x -> uris.add(x.getUri()));
            transaction.commit();
            LOG.info("In "+referenceType+": " + referenceURI + " found: ["+resultType + "]: " + uris);
        }catch (ResultProcessingException e){
            LOG.warn("exception while finding " + resultType +"s in " + referenceType + ": " + referenceURI + ":: " + e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error while finding " + resultType +"s in " + referenceType + ": " + referenceURI,e);
        }
        return uris;
    }

    // Compact Mode

    /**
     * Filter by field value
     * @param resultType
     * @param field
     * @param value
     * @return
     */
    public List<String> findBy(Resource.Type resultType, String field, String value){
        LOG.debug("Finding " + resultType.name() + "s");
        List<String> uris = new ArrayList<>();
        try{
            unifiedSession.clean();
            UnifiedTransaction transaction = unifiedSession.beginTransaction();
            columnRepository.findBy(resultType, field,value).forEach(x -> uris.add(x.getUri()));
            transaction.commit();
            LOG.info("By "+field+": " + value+ " found: ["+resultType + "]: " + uris);
        }catch (ResultProcessingException e){
            LOG.warn("getting all " + resultType,e.getMessage());
        }catch (Exception e){
            LOG.error("Unexpected error while getting all " + resultType,e);
        }
        return uris;
    }


    public List<String> findSources(){
        session.clear();
        LOG.debug("Finding sources");
        List<String> uris = new ArrayList<>();
        sourceColumnRepository.findAll().forEach(x -> uris.add(x.getUri()));
        LOG.info("Sources: " + uris);
        return uris;
    }

    public List<String> findDomains(){
        session.clear();
        LOG.debug("Finding domains");
        List<String> uris = new ArrayList<>();
        domainColumnRepository.findAll().forEach(x -> uris.add(x.getUri()));
        LOG.info("domains: " + uris);
        return uris;
    }

    public List<String> findDocuments(){
        session.clear();
        LOG.debug("Finding documents");
        List<String> uris = new ArrayList<>();
        documentColumnRepository.findAll().forEach(x -> uris.add(x.getUri()));
        LOG.info("Documents: " + uris);
        return uris;
    }

    public List<String> findItems(){
        session.clear();
        LOG.debug("Finding items");
        List<String> uris = new ArrayList<>();
        itemColumnRepository.findAll().forEach(x -> uris.add(x.getUri()));
        LOG.info("Items: " + uris);
        return uris;
    }

    public List<String> findParts(){
        session.clear();
        LOG.debug("Finding parts");
        List<String> uris = new ArrayList<>();
        partColumnRepository.findAll().forEach(x -> uris.add(x.getUri()));
        LOG.info("Parts: " + uris);
        return uris;
    }

    public List<String> findWords(){
        session.clear();
        LOG.debug("Finding words");
        List<String> uris = new ArrayList<>();
        wordColumnRepository.findAll().forEach(x -> uris.add(x.getUri()));
        LOG.info("Words: " + uris);
        return uris;
    }

    public List<String> findWordsByDomain(String domain){
        session.clear();
        LOG.debug("Finding words in Domain: " + domain);
        List<String> uris = new ArrayList<>();
        wordGraphRepository.findByDomain(domain).forEach(x -> uris.add(x.getUri()));
        LOG.info("Words in Domain["+domain+"]: " + uris);
        return uris;
    }

    public List<String> findTopics(){
        session.clear();
        LOG.debug("Finding topics");
        List<String> uris = new ArrayList<>();
        topicColumnRepository.findAll().forEach(x -> uris.add(x.getUri()));
        LOG.info("Topics: " + uris);
        return uris;
    }

    public List<String> findAnalyses(){
        session.clear();
        LOG.debug("Finding analyses");
        List<String> uris = new ArrayList<>();
        analysisColumnRepository.findAll().forEach(x -> uris.add(x.getUri()));
        LOG.info("Analyses: " + uris);
        return uris;
    }


    public List<String> findDocumentsByDomain(String uri){
        session.clear();
        LOG.debug("Finding documents in domain: " + uri);
        List<String> uris = new ArrayList<>();
        documentGraphRepository.findByDomain(uri).forEach(x -> uris.add(x.getUri()));
        LOG.info("Documents in domain '"+uri+"': " + uris);
        return uris;
    }

    public List<String> findDocumentsByTitle(String title){
        session.clear();
        LOG.debug("Finding documents by title: " + title);
        List<String> uris = new ArrayList<>();
        documentColumnRepository.findByTitle(title).forEach(x -> uris.add(x.getUri()));
        LOG.info("Documents by title '"+title+"': " + uris);
        return uris;
    }

    public List<String> findDomainBySource(String uri){
        session.clear();
        LOG.debug("Finding domains by source: " + uri);
        List<String> uris = new ArrayList<>();
        domainGraphRepository.findBySource(uri).forEach(x -> uris.add(x.getUri()));
        LOG.info("Domains composed by source '"+uri+"': " + uris);
        return uris;
    }

    public List<String> findDocumentRetrievedFrom(String url){
        session.clear();
        LOG.debug("Finding documents retrieved from: " + url);
        List<String> uris = new ArrayList<>();
        documentColumnRepository.findByRetrievedFrom(url).forEach(x -> uris.add(x.getUri()));
        LOG.info("Documents retrieved from '" + url + "':" + uris);
        return uris;
    }

    public List<Relationship> findDealsByDocumentInDomain(String documentURI, String domainURI){
        session.clear();
        LOG.debug("Finding deals for document: " + documentURI + " in domain: " + domainURI);
        List<Relationship> relationships = new ArrayList<>();
        documentGraphRepository.dealsInDomain(documentURI,domainURI).forEach(x -> relationships.add(new Relationship(x.getTopic().getUri(),x.getWeight())));
        LOG.info("Document["+documentURI+"] DEALS_WITH [" + relationships + "] in Domain["+domainURI+"]");
        return relationships;
    }


    public List<String> findItemsByDomain(String uri){
        session.clear();
        LOG.debug("Finding items in domain: " + uri);
        List<String> uris = new ArrayList<>();
        itemGraphRepository.findByDomain(uri).forEach(x -> uris.add(x.getUri()));
        LOG.info("Items: " + uris);
        return uris;
    }

    public List<String> findItemsByDocument(String uri){
        session.clear();
        LOG.debug("Finding items in document: " + uri);
        List<String> uris = new ArrayList<>();
        itemGraphRepository.findByDocument(uri).forEach(x -> uris.add(x.getUri()));
        LOG.info("Items: " + uris);
        return uris;
    }

    public Optional<String> findItemByPart(String uri){
        session.clear();
        LOG.debug("Finding item in part: " + uri);
        List<String> uris = new ArrayList<>();
        itemGraphRepository.findByPart(uri).forEach(x -> uris.add(x.getUri()));
        LOG.info("Items: " + uris);
        if ((uris != null) && (!uris.isEmpty())) return Optional.of(uris.get(0));
        return Optional.empty();
    }

    public List<Relationship> findDealsByItemAndDomain(String itemURI, String domainURI){
        session.clear();
        LOG.debug("Finding deals for item: " + itemURI + " in domain: " + domainURI);
        List<Relationship> relationships = new ArrayList<>();
        itemGraphRepository.dealsInDomain(itemURI,domainURI).forEach(x -> relationships.add(new Relationship(x.getTopic().getUri(),x.getWeight())));
        LOG.info("Item: ["+itemURI+"] DEALS_WITH [" + relationships +"] in domain["+domainURI+"]");
        return relationships;
    }


    public List<String> findPartsByDomain(String uri){
        session.clear();
        LOG.debug("Finding parts in domain: " + uri);
        List<String> uris = new ArrayList<>();
        partGraphRepository.findByDomain(uri).forEach(x -> uris.add(x.getUri()));
        LOG.info("Parts: " + uris);
        return uris;
    }

    public List<String> findPartsByItem(String uri){
        session.clear();
        LOG.debug("Finding parts in item: " + uri);
        List<String> uris = new ArrayList<>();
        partGraphRepository.findByItem(uri).forEach(x -> uris.add(x.getUri()));
        LOG.info("Parts: " + uris);
        return uris;
    }

    public List<String> findTopicsByDomain(String uri){
        session.clear();
        LOG.debug("Finding topics in domain: " + uri);
        List<String> uris = new ArrayList<>();
        topicGraphRepository.findByDomain(uri).forEach(x -> uris.add(x.getUri()));
        LOG.info("Topics[" + uris +"] in domain["+uri+"]");
        return uris;
    }

    public List<Relationship> findDealsByPartAndDomain(String partURI, String domainURI){
        session.clear();
        LOG.debug("Finding deals for part: " + partURI + " in domain: " + domainURI);
        List<Relationship> relationships = new ArrayList<>();
        partGraphRepository.dealsInDomain(partURI,domainURI).forEach(x -> relationships.add(new Relationship(x.getTopic().getUri(),x.getWeight())));
        LOG.info("Part["+partURI+"] DEALS_WITH [" + relationships+"] in Domain["+domainURI);
        return relationships;
    }

    public Optional<String> findWordByContent(String content){
        return emptyOrFirst(wordColumnRepository.findByContent(content));
    }

    public Optional<String> findWordByLemma(String lemma){
        return emptyOrFirst(wordColumnRepository.findByLemma(lemma));
    }

    public Optional<String> findWordByPos(String pos){
        return emptyOrFirst(wordColumnRepository.findByPos(pos));
    }

    public Optional<String> findWordByStem(String stem){
        return emptyOrFirst(wordColumnRepository.findByStem(stem));
    }

    public Optional<String> findWordByType(String type){
        return emptyOrFirst(wordColumnRepository.findByType(type));
    }

    private Optional<String> emptyOrFirst(Iterable<? extends Resource> resources){
        if (resources == null || !resources.iterator().hasNext()) return Optional.empty();
        return Optional.of(resources.iterator().next().getUri());
    }

    /******************************************************************************
     * Delete
     ******************************************************************************/

    // Compact Mode
    public void delete(String uri, Resource.Type type){
        try{
            unifiedSession.clean();
            UnifiedTransaction transaction = unifiedSession.beginTransaction();

            columnRepository.delete(uri,type);
            documentRepository.delete(uri,type);
            graphRepository.delete(uri,type);

            transaction.commit();

            LOG.info("Deleted: "+type.name()+"[" + uri+"]");

            //Publish the event
            Resource resource = new Resource();
            resource.setUri(uri);
            eventBus.post(Event.from(resource), RoutingKey.of(type, Resource.State.DELETED));

        }catch (Exception e){
            LOG.error("Unexpected error during delete of '"+uri,e);
        }
    }

    // Compact Mode
    public void deleteAll(Resource.Type type){
        try{
            unifiedSession.clean();
            UnifiedTransaction transaction = unifiedSession.beginTransaction();

            columnRepository.deleteAll(type);
            documentRepository.deleteAll(type);
            graphRepository.deleteAll(type);

            transaction.commit();

            LOG.info("Deleted All: "+type.name());

            //Publish the event
            // TODO
        }catch (Exception e){
            LOG.error("Unexpected error during delete all '"+type,e);
        }
    }


    public void deleteSource(String uri){
        try{
            session.clear();
            // column
            sourceColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
            // document
            sourceDocumentRepository.delete(uri);
            // graph : TODO Get id directly from URI
            SourceNode source = sourceGraphRepository.findOneByUri(uri);
            sourceGraphRepository.delete(source);
            session.clear();
            LOG.info("Deleted: Source[" + uri+"]");

            //Publish the event
            Source resource = ResourceUtils.map(source, Source.class);
            eventBus.post(Event.from(resource), RoutingKey.of(Resource.Type.SOURCE, Resource.State.DELETED));

        }catch (Exception e){
            LOG.error("Unexpected error during delete of '"+uri,e);
        }
    }

    public void deleteSources(){
        try{
            session.clear();
            sourceColumnRepository.deleteAll();
            sourceDocumentRepository.deleteAll();
            sourceGraphRepository.deleteAll();
            LOG.info("All sources removed");
            //TODO Publish the events
        }catch (Exception e){
            LOG.error("Unexpected error during delete of sources",e);
        }
    }

    public void deleteDomain(String uri){
        try{
            session.clear();
            // column
            domainColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
            // document
            domainDocumentRepository.delete(uri);
            // graph : TODO Get id directly from URI
            DomainNode domain = domainGraphRepository.findOneByUri(uri);
            domainGraphRepository.delete(domain);
            session.clear();
            LOG.info("Deleted: Domain[" + uri+"]");

            //Publish the event
            Domain resource = ResourceUtils.map(domain,Domain.class);
            eventBus.post(Event.from(resource), RoutingKey.of(Resource.Type.DOMAIN, Resource.State.DELETED));

        }catch (Exception e){
            LOG.error("Unexpected error during delete of '"+uri,e);
        }
    }

    public void deleteDomains(){
        try{
            session.clear();
            domainColumnRepository.deleteAll();
            domainDocumentRepository.deleteAll();
            domainGraphRepository.deleteAll();
            LOG.info("All domains removed");
            //TODO Publish the events
        }catch (Exception e){
            LOG.error("Unexpected error during delete of domains",e);
        }
    }

    public void deleteDocument(String uri){
        try{
            session.clear();
            // column
            documentColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
            // document
            documentDocumentRepository.delete(uri);
            // graph : TODO Get id directly from URI
            DocumentNode document = documentGraphRepository.findOneByUri(uri);
            documentGraphRepository.delete(document);
            session.clear();
            LOG.info("Deleted: Document[" + uri+"]");

            //Publish the event
            Document resource = ResourceUtils.map(document,Document.class);
            eventBus.post(Event.from(resource), RoutingKey.of(Resource.Type.DOCUMENT, Resource.State.DELETED));

        }catch (Exception e){
            LOG.error("Unexpected error during delete of '"+uri,e);
        }
    }

    public void deleteDocuments(){
        try{
            session.clear();
            documentColumnRepository.deleteAll();
            documentDocumentRepository.deleteAll();
            documentGraphRepository.deleteAll();
            LOG.info("All documents removed");
            //TODO Publish the events
        }catch (Exception e){
            LOG.error("Unexpected error during delete of documents",e);
        }
    }

    public void deleteItem(String uri){
        try{
            session.clear();
            // column
            itemColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
            // document
            itemDocumentRepository.delete(uri);
            // graph : TODO Get id directly from URI
            ItemNode item = itemGraphRepository.findOneByUri(uri);
            itemGraphRepository.delete(item);
            session.clear();
            LOG.info("Deleted: Item[" + uri+"]");

            //Publish the event
            Item resource = ResourceUtils.map(item,Item.class);
            eventBus.post(Event.from(resource), RoutingKey.of(Resource.Type.ITEM, Resource.State.DELETED));

        }catch (Exception e){
            LOG.error("Unexpected error during delete of '"+uri,e);
        }
    }

    public void deleteItems(){
        try{
            session.clear();
            itemColumnRepository.deleteAll();
            itemDocumentRepository.deleteAll();
            itemGraphRepository.deleteAll();
            LOG.info("All items removed");
            //TODO Publish the events
        }catch (Exception e){
            LOG.error("Unexpected error during delete of items",e);
        }
    }

    public void deletePart(String uri){
        try{
            session.clear();
            // column
            partColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
            // document
            partDocumentRepository.delete(uri);
            // graph : TODO Get id directly from URI
            PartNode part = partGraphRepository.findOneByUri(uri);
            partGraphRepository.delete(part);
            session.clear();
            LOG.info("Deleted: Part[" + uri+"]");

            //Publish the event
            Part resource = ResourceUtils.map(part,Part.class);
            eventBus.post(Event.from(resource), RoutingKey.of(Resource.Type.PART, Resource.State.DELETED));

        }catch (Exception e){
            LOG.error("Unexpected error during delete of '"+uri,e);
        }
    }

    public void deleteParts(){
        try{
            session.clear();
            partColumnRepository.deleteAll();
            partDocumentRepository.deleteAll();
            partGraphRepository.deleteAll();
            LOG.info("All parts removed");
            //TODO Publish the events
        }catch (Exception e){
            LOG.error("Unexpected error during delete of parts",e);
        }
    }

    public void deleteWord(String uri){
        try{
            session.clear();
            // column
            wordColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
            // document
            wordDocumentRepository.delete(uri);
            // graph : TODO Get id directly from URI
            WordNode word = wordGraphRepository.findOneByUri(uri);
            wordGraphRepository.delete(word);
            session.clear();
            LOG.info("Deleted: Word[" + uri+"]");

            //Publish the event
            Word resource = ResourceUtils.map(word,Word.class);
            eventBus.post(Event.from(resource), RoutingKey.of(Resource.Type.WORD, Resource.State.DELETED));

        }catch (Exception e){
            LOG.error("Unexpected error during delete of '"+uri,e);
        }
    }

    public void deleteWords(){
        try{
            session.clear();
            wordColumnRepository.deleteAll();
            wordDocumentRepository.deleteAll();
            wordGraphRepository.deleteAll();
            LOG.info("All words removed");
            //TODO Publish the events
        }catch (Exception e){
            LOG.error("Unexpected error during delete of words",e);
        }
    }

    public void deleteTopic(String uri){
        try{
            session.clear();
            // column
            topicColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
            // document
            topicDocumentRepository.delete(uri);
            // graph : TODO Get id directly from URI
            TopicNode topic = topicGraphRepository.findOneByUri(uri);
            topicGraphRepository.deleteAndDetach(uri);
            session.clear();
            LOG.info("Deleted: Topic[" + uri+"]");

            //Publish the event
            Topic resource = ResourceUtils.map(topic,Topic.class);
            eventBus.post(Event.from(resource), RoutingKey.of(Resource.Type.TOPIC, Resource.State.DELETED));

        }catch (Exception e){
            LOG.error("Unexpected error during delete of '"+uri,e);
        }
    }

    public void deleteTopics(){
        try{
            session.clear();
            topicColumnRepository.deleteAll();
            topicDocumentRepository.deleteAll();
            topicGraphRepository.deleteAll();
            LOG.info("All topics removed");
            //TODO Publish the events
        }catch (Exception e){
            LOG.error("Unexpected error during delete of topics",e);
        }
    }

    public void deleteAnalysis(String uri){
        try{
            session.clear();
            // column
            analysisColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
            // document
            analysisDocumentRepository.delete(uri);
            // graph : TODO remove TOPIC and/or RELATIONS of that analysis

            //Publish the event
            Analysis analysis = new Analysis();
            analysis.setUri(uri);
            eventBus.post(Event.from(analysis), RoutingKey.of(Resource.Type.ANALYSIS, Resource.State.DELETED));

        }catch (Exception e){
            LOG.error("Unexpected error during delete of '"+uri,e);
        }
    }

    public void deleteAnalyses(){
        try{
            session.clear();
            // column
            analysisColumnRepository.deleteAll();
            // document
            analysisDocumentRepository.deleteAll();
            LOG.info("All analyses removed");
            //TODO Publish the events
        }catch (Exception e){
            LOG.error("Unexpected error during delete of analyses",e);
        }
    }

    public void deleteSimilarsInDomain(String uri){
        try{
            session.clear();
            LOG.debug("trying to delete SIMILAR relationships in domain: " + uri);
            domainGraphRepository.deleteSimilarRelations(uri);
            session.clear();
            LOG.info("Deleted SIMILAR relationships of Item: " + uri);
        }catch (Exception e){
            LOG.error("Unexpected error during deleteSimilarsInDomain of '"+uri,e);
        }
    }

    public void deleteSimilarsBetweenDocumentsInDomain(String uri){
        try{
            session.clear();
            LOG.debug("trying to delete SIMILAR relationships between Documents in Domain: " + uri);
            documentGraphRepository.deleteSimilarRelationsInDomain(uri);
            session.clear();
            LOG.info("Deleted SIMILAR relationships between Documents in Domain: " + uri);
        }catch (Exception e){
            LOG.error("Unexpected error during deleteSimilarsBetweenDocumentsInDomain of '"+uri,e);
        }
    }

    public void deleteSimilarsBetweenItemsInDomain(String uri){
        try{
            session.clear();
            LOG.debug("trying to delete SIMILAR relationships between Items in Domain: " + uri);
            itemGraphRepository.deleteSimilarRelationsInDomain(uri);
            session.clear();
            LOG.info("Deleted SIMILAR relationships between Items in Domain: " + uri);
        }catch (Exception e){
            LOG.error("Unexpected error during deleteSimilarsBetweenItemsInDomain of '"+uri,e);
        }
    }

    public void deleteSimilarsBetweenPartsInDomain(String uri){
        try{
            session.clear();
            LOG.debug("trying to delete SIMILAR relationships between Parts in Domain: " + uri);
            partGraphRepository.deleteSimilarRelationsInDomain(uri);
            session.clear();
            LOG.info("Deleted SIMILAR relationships between Parts in Domain: " + uri);
        }catch (Exception e){
            LOG.error("Unexpected error during deleteSimilarsBetweenPartsInDomain of '"+uri,e);
        }
    }

    public void deleteSimilarsBetweenWordsInDomain(String uri){
        try{
            session.clear();
            LOG.debug("trying to delete SIMILAR relationships between Words in Domain: " + uri);
            wordGraphRepository.deletePairingInDomain(uri);
            session.clear();
            LOG.info("Deleted SIMILAR relationships between Words in Domain: " + uri);
        }catch (Exception e){
            LOG.error("Unexpected error during deleteSimilarsBetweenWordsInDomain of '"+uri,e);
        }
    }

    public void deleteEmbeddingWordsInDomain(String uri){
        try{
            session.clear();
            LOG.debug("trying to delete EMBEDDED relationships from Words to Domain: " + uri);
            wordGraphRepository.deleteEmbeddingInDomain(uri);
            session.clear();
            LOG.info("Deleted EMBEDDED_IN relationships from Words to Domain: " + uri);
        }catch (Exception e){
            LOG.error("Unexpected error during deleteEmbeddingWordsInDomain of '"+uri,e);
        }
    }

    public void deleteAll(){
        try{
            session.clear();
            analysisColumnRepository.deleteAll();
            analysisDocumentRepository.deleteAll();

            documentColumnRepository.deleteAll();
            documentDocumentRepository.deleteAll();
            documentGraphRepository.deleteAll();

            domainColumnRepository.deleteAll();
            domainDocumentRepository.deleteAll();
            domainGraphRepository.deleteAll();

            itemColumnRepository.deleteAll();
            itemDocumentRepository.deleteAll();
            itemGraphRepository.deleteAll();

            partColumnRepository.deleteAll();
            partDocumentRepository.deleteAll();
            partGraphRepository.deleteAll();

            sourceColumnRepository.deleteAll();
            sourceDocumentRepository.deleteAll();
            sourceGraphRepository.deleteAll();

            topicColumnRepository.deleteAll();
            topicDocumentRepository.deleteAll();
            topicGraphRepository.deleteAll();

            wordColumnRepository.deleteAll();
            wordDocumentRepository.deleteAll();
            wordGraphRepository.deleteAll();
        }catch (Exception e){
            LOG.error("Unexpected error during delete all",e);
        }
    }

}
