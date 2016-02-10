package org.epnoi.knowledgebase;

import org.epnoi.knowledgebase.wikidata.WikidataHandler;
import org.epnoi.knowledgebase.wikidata.WikidataHandlerBuilder;
import org.epnoi.knowledgebase.wordnet.WordNetHandler;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class KnowledgeBaseFactory {

	private static final Logger LOG = LoggerFactory.getLogger(KnowledgeBaseFactory.class);

	@Value("${epnoi.knowledgeBase.wordnet.considered}")
	Boolean wordnetEnabled;

	@Value("${epnoi.knowledgeBase.wikidata.considered}")
	Boolean wikidataEnabled;

	@Autowired
	WordNetHandler wordNetHandler;

	@Autowired
	WikidataHandlerBuilder wikidataHandlerBuilder;

	private WikidataHandler wikidataHandler;


	public KnowledgeBase build() throws EpnoiInitializationException {

		KnowledgeBase knowledgeBase = new KnowledgeBase(this.wordNetHandler, this.wikidataHandlerBuilder.build());
		knowledgeBase.init(wikidataEnabled,wordnetEnabled);
		return knowledgeBase;
	}

}
