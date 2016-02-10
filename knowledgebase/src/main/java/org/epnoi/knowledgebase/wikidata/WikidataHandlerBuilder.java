package org.epnoi.knowledgebase.wikidata;

import org.epnoi.knowledgebase.wikidata.view.CassandraWikidataView;
import org.epnoi.knowledgebase.wikidata.view.WikidataViewCreator;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.WikidataView;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.wikidata.wdtk.dumpfiles.DumpProcessingController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A factory that creates WikidataHandlers
 * 
 * @author Rafael Gonzalez-Cabero
 * 
 *
 */
@Component
public class WikidataHandlerBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(WikidataHandlerBuilder.class);

	@Value("${epnoi.knowledgeBase.wikidata.considered}")
	Boolean wikidataEnabled;

	@Value("${epnoi.knowledgeBase.wikidata.mode}")
	String wikidataMode;

	@Value("${epnoi.knowledgeBase.wikidata.offline}")
	Boolean offline;

	@Value("${epnoi.knowledgeBase.wikidata.inMemory}")
	Boolean wikidataInMemory;

	@Value("${epnoi.knowledgeBase.wikidata.dump.path}")
	String wikidataDumpPath;

	@Value("${epnoi.knowledgeBase.wikidata.dump.mode}")
	String wikidataDumpMode;

	@Value("${epnoi.knowledgeBase.wikidata.uri}")
	String wikidataUri;

	@Value("${epnoi.knowledgeBase.wikidata.timeout}")
	Integer wikidataTimeout;

	private DumpProcessingMode dumpProcessingMode;

	private boolean create;
	private DumpProcessingController dumpProcessingController;

	@Autowired
	WikidataViewCreator wikidataViewCreator;

	private Map<String, Set<String>> labelsDictionary = new HashMap<>();

	private Map<String, Set<String>> labelsReverseDictionary = new HashMap<>();

	private Map<String, Set<String>> hypernymRelations = new HashMap<>();
	Map<String, Map<String, Set<String>>> relationsTable = new HashMap<>();


	@PostConstruct
	public void setup() throws EpnoiInitializationException {

		LOG.info("Initialiazing the WikidataHandler with the following parameters");

		this.dumpProcessingMode = DumpProcessingMode.from(wikidataDumpMode);

		this.create = org.epnoi.model.parameterization.ParametersModel.KNOWLEDGEBASE_WIKIDATA_MODE_CREATE.equals(wikidataMode);

	}

	// --------------------------------------------------------------------------------------------------

	/**
	 * Builds a WikidataHandler. If the parameter
	 * WikidataHandlerParameters.STORE_PARAMETER is set to true, the associated
	 * WikidataView is stored in the UIA for later use
	 * 
	 * @return A WikidataHandler with its associated WikidataView
	 */
	public WikidataHandler build() throws EpnoiInitializationException {
		LOG.info("Building a WikidataHandler with the following parameters: ");
		
		

		relationsTable.put(RelationHelper.HYPERNYMY, hypernymRelations);
		if (this.create) {
			_createWikidataVIew();
		}

		LOG.info("Retrieving the  WikidataView");
		try {
			if (this.wikidataInMemory) {
				return _retrieveWikidataViewInMemory();
			} else {
				return _retriveWikidataViewFromCassandra();

			}
		} catch (Exception e) {
			LOG.error("The wikidataview with uri " + this.wikidataUri + " couldn't be retrieved ");
			throw new EpnoiInitializationException(
					"The wikidataview " + this.wikidataUri + " couldn't be retrieved " + e.getMessage());
		}


	}
	// --------------------------------------------------------------------------------------------------

	private WikidataHandler _retriveWikidataViewFromCassandra() {
		LOG.info("The WikidataView will be accessed directly in Cassandra");
		
		CassandraWikidataView wikidataView = new CassandraWikidataView(wikidataUri);
		
		return new WikidataHandlerCassandraImpl(wikidataView);
	}

	// --------------------------------------------------------------------------------------------------
	
	private WikidataHandler _retrieveWikidataViewInMemory() {
		LOG.error("pending to implement by using UDM");
		//TODO
		//WikidataView inMemoryWikidataView= (WikidataView) this.core.getInformationHandler().get(wikidataUri, RDFHelper.WIKIDATA_VIEW_CLASS);
//		return new WikidataHandlerInMemoryImpl(inMemoryWikidataView);
		return null;
	}
	
	// --------------------------------------------------------------------------------------------------

	private void _createWikidataVIew() {
		
		LOG.info(
				"Creating a new WikidataView, since the retrieve flag was set false, and the create flag was set as true");
		//WikidataView inMemoryWikidataView = _generateWikidataview();
	


		WikidataView inMemoryWikidataView = this.wikidataViewCreator.create();
	
		LOG.info("Storing the new built WikidataView, since the store flag was activated");
		// First we remove the WikidataWiew if there is one with the same
		// URI

		LOG.error("pending to implement by using UDM");
		//TODO
//		this.core.getInformationHandler().remove(this.wikidataViewURI, RDFHelper.WIKIDATA_VIEW_CLASS);
//		this.core.getInformationHandler().put(inMemoryWikidataView, Context.getEmptyContext());
	
	}

}
