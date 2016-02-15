package org.epnoi.knowledgebase.wikidata.view;

import org.epnoi.knowledgebase.wikidata.WikidataDumpProcessor;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.WikidataView;
import org.epnoi.model.domain.Resource;
import org.epnoi.model.domain.SerializedObject;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.storage.UDM;
import org.epnoi.storage.generator.TimeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wikidata.wdtk.datamodel.interfaces.*;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class WikidataViewCreator {

	private static final Logger LOG = LoggerFactory.getLogger(WikidataViewCreator.class);

	@org.springframework.beans.factory.annotation.Value("${epnoi.knowledgeBase.wikidata.mode}")
	String wikidataMode;

	@org.springframework.beans.factory.annotation.Value("${epnoi.knowledgeBase.wikidata.uri}")
	String wikidataUri;

	@Autowired
	WikidataDumpProcessor wikidataDumpProcessor;

	@Autowired
	UDM udm;

	@Autowired
	TimeGenerator timeGenerator;

	private Map<String, Set<String>> labelsDictionary = new HashMap<>();

	private Map<String, Set<String>> labelsReverseDictionary = new HashMap<>();

	private Map<String, Set<String>> hypernymRelations = new HashMap<>();

	private Map<String, Map<String, Set<String>>> relationsTable = new HashMap<>();


	@PostConstruct
	public void setup() throws EpnoiInitializationException {
		LOG.info("Initializing the WikidataViewCreator");

		// Subscribe to the most recent entity documents of type wikibase item:
		this.wikidataDumpProcessor.registerEntityDocumentProcessor(new HypernymRelationsEntityProcessor());

	}

	public boolean hasToBeCreated(){
		return wikidataMode.equalsIgnoreCase("create");
	}

	public WikidataView create() {
		LOG.info("Creating a new WikidataView ..");

		this.relationsTable.put(RelationHelper.HYPERNYMY, hypernymRelations);
		this.wikidataDumpProcessor.processEntitiesFromWikidataDump();
		WikidataView wikidataView = new WikidataView(wikidataUri, labelsDictionary, labelsReverseDictionary, relationsTable);

		// Compressing wikidata view
		WikidataViewCompressor wikidataViewCompressor = new WikidataViewCompressor();
		wikidataView= wikidataViewCompressor.compress(wikidataView);

		return wikidataView;

	}


	public void store(WikidataView wikidataView) {

		LOG.info("Saving wikidataview: " + wikidataView.getUri() + " by using UDM..");

		SerializedObject serializedObject = new SerializedObject();
		serializedObject.setUri(wikidataView.getUri());
		serializedObject.setCreationTime(timeGenerator.asISO());
		serializedObject.setInstance(wikidataView);

		udm.save(Resource.Type.SERIALIZED_OBJECT).with(serializedObject);
	}


	public WikidataView retrieve(String uri) {
		LOG.debug("Getting wikidata view: " + uri +" by using UDM");
		WikidataView view = null;
		Optional<Resource> result = udm.read(Resource.Type.SERIALIZED_OBJECT).byUri(uri);
		if (result.isPresent()){
			SerializedObject serializedObject = (SerializedObject) result.get();
			view = (WikidataView) serializedObject.getInstance();
		}
		LOG.debug("Wikidata view: " + uri +" read: " + view);
		return view;
	}


	class HypernymRelationsEntityProcessor implements EntityDocumentProcessor {
		private static final String EN = "en";
		public static final String INSTANCE_OF = "http://www.wikidata.org/entity/P31";

		// --------------------------------------------------------------------------------------------------

		@Override
		public void processItemDocument(ItemDocument itemDocument) {

			if (_valuableItem(itemDocument)) {

				processStatements(itemDocument);

				processItem(itemDocument);

			}
		}

		private boolean _valuableItem(ItemDocument itemDocument) {

			return (itemDocument.getLabels().get(
					HypernymRelationsEntityProcessor.EN) != null);

		}

		// --------------------------------------------------------------------------------------------------
		/**
		 * 
		 * @param itemDocument
		 */

		private void processItem(ItemDocument itemDocument) {
			String itemIRI = itemDocument.getEntityId().getId();

			// First we add the label->IRI relation
			if (itemDocument.getLabels().get(
					HypernymRelationsEntityProcessor.EN) != null) {

				String label = itemDocument.getLabels()
						.get(HypernymRelationsEntityProcessor.EN).getText();
				// Though we don't stemm the label, we at least use only
				// lowercase letters
				label = label.toLowerCase();
				if (_validLabel(label)) {
					//_addToDictionary(label, itemIRI, labelsDictionary);
					_addToDictionary(itemIRI, label, labelsReverseDictionary);
				}
			}
			// Now, for each alias of the label we also add the relation
			// alias->IRI
			/*
			if (itemDocument.getAliases().get(
					HypernymRelationsEntityProcessor.EN) != null) {
				for (MonolingualTextValue alias : itemDocument.getAliases()
						.get(HypernymRelationsEntityProcessor.EN)) {
					String aliasText = alias.getText();
					aliasText = aliasText.toLowerCase();
					if (_validLabel(aliasText)) {
					//	_addToDictionary(aliasText, itemIRI, labelsDictionary);
						_addToDictionary(itemIRI, aliasText,
						labelsReverseDictionary);
					}
				}
			}
			*/

		}

		// ---------------------------------------------------------------------

		private boolean _validLabel(String label) {
			return ((label != null) && (!label.contains("disambiguation")));

		}

		// ---------------------------------------------------------------------

		/**
		 * 
		 * @param value
		 * @param key
		 * @param dictionary
		 */
		private void _addToDictionary(String key, String value,
				Map<String, Set<String>> dictionary) {
			Set<String> values = dictionary.get(key);
			if (values == null) {
				values = new HashSet<>();
				dictionary.put(key, values);
			}
			values.add(value);
		}

		// --------------------------------------------------------------------------------------------------

		@Override
		public void processPropertyDocument(PropertyDocument propertyDocument) {

		}

		// --------------------------------------------------------------------------------------------------

		protected void processStatements(StatementDocument statementDocument) {
			// We consider that isValid if contains instanceOf properties
			
			for (StatementGroup statementGroup : statementDocument
					.getStatementGroups()) {

				String property = statementGroup.getProperty().getIri();

				if (INSTANCE_OF.equals(property)) {
					String subject = statementGroup.getSubject().getId();

					Set<String> hyponyms = new HashSet<String>();

					for (Statement statement : statementGroup.getStatements()) {
						Snak mainSnak = statement.getClaim().getMainSnak();
						if (mainSnak instanceof ValueSnak) {
							String object = ((ItemIdValue) ((ValueSnak) mainSnak)
									.getValue()).getId();

							hyponyms.add(object);
						}
					}
				
					hypernymRelations.put(subject, hyponyms);
				}

			}
			
		}
		// --------------------------------------------------------------------------------------------------

	}
}
