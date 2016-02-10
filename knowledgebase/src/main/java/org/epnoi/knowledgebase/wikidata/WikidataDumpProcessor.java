package org.epnoi.knowledgebase.wikidata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocumentProcessor;
import org.wikidata.wdtk.dumpfiles.DumpContentType;
import org.wikidata.wdtk.dumpfiles.DumpProcessingController;
import org.wikidata.wdtk.dumpfiles.EntityTimerProcessor;
import org.wikidata.wdtk.dumpfiles.EntityTimerProcessor.TimeoutException;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class WikidataDumpProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(WikidataDumpProcessor.class);


	@Value("${epnoi.knowledgeBase.wikidata.offline}")
	Boolean offline;

	@Value("${epnoi.knowledgeBase.wikidata.dump.path}")
	String dumpPath;

	@Value("${epnoi.knowledgeBase.wikidata.dump.mode}")
	String wikidataDumpMode;

	@Value("${epnoi.knowledgeBase.wikidata.timeout}")
	Integer timeout;


	private DumpProcessingController dumpProcessingController;

	private DumpProcessingMode dumpProcessingMode;

	private boolean onlyCurrentRevisions;


	@PostConstruct
	public void setup() {

		this.dumpProcessingMode = DumpProcessingMode.from(wikidataDumpMode);


		this.dumpProcessingController = new DumpProcessingController("wikidata");
		this.dumpProcessingController.setOfflineMode(this.offline);
		try {
			this.dumpProcessingController.setDownloadDirectory(this.dumpPath);
		} catch (IOException ioException) {
			LOG.error("Error dumping wikidata", ioException);
		}

		// Should we process historic revisions or only current ones?

		switch (this.dumpProcessingMode) {
		case ALL_REVS:
		case ALL_REVS_WITH_DAILIES:
			this.onlyCurrentRevisions = false;
			break;
		case CURRENT_REVS:
		case CURRENT_REVS_WITH_DAILIES:
		case JSON:
		case JUST_ONE_DAILY_FOR_TEST:
		default:
			this.onlyCurrentRevisions = true;
		}
	}

	public void registerEntityDocumentProcessor(EntityDocumentProcessor entityDocumentProcessor) {

		// Subscribe to the most recent entity documents of type wikibase item:
		dumpProcessingController.registerEntityDocumentProcessor(
				entityDocumentProcessor, null, this.onlyCurrentRevisions);

		// Also add a timer that reports some basic progress information:
		EntityTimerProcessor entityTimerProcessor = new EntityTimerProcessor(this.timeout);
		dumpProcessingController.registerEntityDocumentProcessor(
				entityTimerProcessor, null, this.onlyCurrentRevisions);

	}


	public void processEntitiesFromWikidataDump() {

		// Also add a timer that reports some basic progress information:
		EntityTimerProcessor entityTimerProcessor = new EntityTimerProcessor(this.timeout);
		try {
			// Start processing (may trigger downloads where needed):
			switch (this.dumpProcessingMode) {
			case ALL_REVS:
			case CURRENT_REVS:
				dumpProcessingController.processMostRecentMainDump();
				break;
			case ALL_REVS_WITH_DAILIES:
			case CURRENT_REVS_WITH_DAILIES:
				dumpProcessingController.processAllRecentRevisionDumps();
				break;
			case JSON:
				// MwDumpFile dumpFile
				// =dumpProcessingController.getMostRecentDump(DumpContentType.JSON);
				try {
					dumpProcessingController.setDownloadDirectory(dumpPath);
				} catch (IOException e) {

					e.printStackTrace();
					System.exit(-1);
				}

				dumpProcessingController.processMostRecentJsonDump();

				break;
			case JUST_ONE_DAILY_FOR_TEST:
				dumpProcessingController.processDump(dumpProcessingController
						.getMostRecentDump(DumpContentType.DAILY));
				break;
			default:
				throw new RuntimeException("Unsupported dump processing type "
						+ this.dumpProcessingMode);
			}
		} catch (TimeoutException e) {

		}
		System.out.println("AQUI IRIA LA COMPACTACION!!!");
		// Print final timer results:
		entityTimerProcessor.close();
	}

}