package org.epnoi.learner.relations;

import org.epnoi.model.RelationsTable;
import org.epnoi.model.domain.relations.ProvenanceRelation;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Domain;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.UDM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class RelationsRetriever {

	private static final Logger LOG = LoggerFactory.getLogger(RelationsRetriever.class);

	@Autowired
	UDM udm;

	public void store(RelationsTable relationsTable){

		LOG.debug("trying to store a relations table:  "+relationsTable);
		for (ProvenanceRelation relation: relationsTable.getRelations()){
			udm.save(relation);
		}
	}


	public RelationsTable retrieve(Domain domain) {
		return retrieve(domain.getUri());
	}

	public RelationsTable retrieve(String domainUri) {

		RelationsTable relationsTable = new RelationsTable();
		relationsTable.setUri(domainUri);

		//TODO should be more general
		List<Relation> relations = udm.find(Relation.Type.HYPERNYM_OF).in(Resource.Type.DOMAIN, domainUri);

		for (Relation relation: relations){
			relationsTable.addRelation((ProvenanceRelation) relation);
		}
		return relationsTable;
	}




}
