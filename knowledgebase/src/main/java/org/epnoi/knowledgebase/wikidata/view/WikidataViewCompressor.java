package org.epnoi.knowledgebase.wikidata.view;

import org.epnoi.model.RelationHelper;
import org.epnoi.model.WikidataView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class WikidataViewCompressor {

	private static final Logger LOG = LoggerFactory.getLogger(WikidataViewCompressor.class);

	private String URI;
	private Map<String, Set<String>> labelsDictionary;
	private Map<String, Set<String>> labelsReverseDictionary;
	private Map<String, Map<String, Set<String>>> relations;

	private Map<String, Set<String>> compressedDictionary = new HashMap<String, Set<String>>();
	private Map<String, Set<String>> compressedlabelsReverseDictionary = new HashMap<String, Set<String>>();

	private Set<String> validTargetIRIs = new HashSet<String>();

	public WikidataView compress(WikidataView wikidataView) {
		LOG.info("Compressing wikidata view: " + wikidataView);
		this.URI = wikidataView.getUri();
		this.labelsDictionary = wikidataView.getLabelsDictionary();
		this.labelsReverseDictionary = wikidataView.getLabelsReverseDictionary();
		this.relations = wikidataView.getRelations();

		this._generateValidTargetIRIs();
		this._generateDictionaries();

		WikidataView compressedWikidataView = new WikidataView(this.URI,
				this.compressedDictionary,
				this.compressedlabelsReverseDictionary, relations);
		//logger.info("Obtained: " + compressedWikidataView);
		return compressedWikidataView;
	}

	// --------------------------------------------------------------------------------------

	private void _generateValidTargetIRIs() {
		for (Set<String> targetIRIs : this.relations.get(RelationHelper.HYPERNYMY).values()) {
			for (String targetIRI : targetIRIs) {
				this.validTargetIRIs.add(targetIRI);
			}
		}
	}

	// --------------------------------------------------------------------------------------

	private void _generateDictionaries() {
		for (Entry<String, Set<String>> reverseDictionaryEntry : labelsReverseDictionary.entrySet()) {
			if (isValidIRI(reverseDictionaryEntry.getKey())) {
				for (String label : reverseDictionaryEntry.getValue()) {
					_addToDictionary(label, reverseDictionaryEntry.getKey(), compressedDictionary);
					_addToDictionary(reverseDictionaryEntry.getKey(), label, compressedlabelsReverseDictionary);
				}
			}
		}

	}

	// ----------------------------------------------------------------------------------------
	private boolean isValidIRI(String IRI) {
		// We consider valid only those IRIs that either the source or the
		// target of some relation
		return (this.validTargetIRIs.contains(IRI) || this.relations.get(RelationHelper.HYPERNYMY).keySet().contains(IRI));
	}

	private void _addToDictionary(String key, String value,
			Map<String, Set<String>> dictionary) {
		Set<String> values = dictionary.get(key);
		if (values == null) {
			values = new HashSet<>();
			dictionary.put(key, values);
		}
		values.add(value);
	}

}
