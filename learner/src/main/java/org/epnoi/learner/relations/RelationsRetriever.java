package org.epnoi.learner.relations;

import org.epnoi.model.Domain;
import org.epnoi.model.RelationsTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RelationsRetriever {
	private static final Logger logger = LoggerFactory.getLogger(RelationsRetriever.class);


	// ------------------------------------------------------------------------------------------------------------

	public RelationsRetriever() {
	}

	// ------------------------------------------------------------------------------------------------------------

	public RelationsTable retrieve(Domain domain) {
		String URI = domain.getUri()+"/relations";

		// TODO
		logger.severe("Pending to implement by using UDM");
//		return (RelationsTable) this.core.getInformationHandler().get(URI, RDFHelper.RELATIONS_TABLE_CLASS);
		return null;
	}

	public RelationsTable retrieve(String domainUri) {
		String uri = domainUri+"/relations";

		System.out.println("Retrieving "+ uri);

		// TODO
		logger.severe("Pending to implement by using UDM");
//		RelationsTable relationsTable = 	 (RelationsTable) this.core.getInformationHandler().get(uri, RDFHelper.RELATIONS_TABLE_CLASS);
		RelationsTable relationsTable = null;


		System.out.println("The relation table > "+relationsTable);
		return relationsTable;
	}

	public void store(RelationsTable relationsTable){
		System.out.println("ESTORING > "+relationsTable);

		// TODO
		logger.severe("Pending to implement by using UDM");
//		this.core.getInformationHandler().put(relationsTable, Context.getEmptyContext());

	}


}
