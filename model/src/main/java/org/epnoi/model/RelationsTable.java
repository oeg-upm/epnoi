package org.epnoi.model;

import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.domain.relations.ProvenanceRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;



public class RelationsTable extends Resource {

	private static final Logger LOG = LoggerFactory.getLogger(RelationsTable.class);

	private String uri;
	private Map<String, ProvenanceRelation> relations;
	private Map<ProvenanceRelation, String> orderedRelations;
	private Map<String, List<ProvenanceRelation>> relationsBySource;


	public RelationsTable() {
		this.orderedRelations = new TreeMap<ProvenanceRelation, String>(new RelationsComparator());
		this.relations = new HashMap<>();
		this.relationsBySource = new HashMap<>();
	}

	/**
	 * 
	 * @param sourceURI
	 * @param expansionProbabilityThreshold
	 * @return
	 */
	public List<ProvenanceRelation> getRelations(String sourceURI, double expansionProbabilityThreshold) {
		List<ProvenanceRelation> relations = new ArrayList<>();
		for (ProvenanceRelation relationFromSource : this.relationsBySource.get(sourceURI)) {
			if (relationFromSource.getWeight() >= expansionProbabilityThreshold) {
				relations.add(relationFromSource);
			}
		}
		return relations;
	}

	/**
	 * 
	 * @param sourceURI
	 * @param expansionProbabilityThreshold
	 * @return
	 */
	public List<ProvenanceRelation> getRelations(String sourceURI, String type,
			double expansionProbabilityThreshold) {
		LOG.info("size> " + this.orderedRelations.size());
		List<ProvenanceRelation> relations = new ArrayList<>();
		for (ProvenanceRelation relationFromSource : this.relationsBySource.get(sourceURI)) {
			if (type.equals(relationFromSource.getType())
					&& relationFromSource.getWeight() >= expansionProbabilityThreshold) {
				relations.add(relationFromSource);
			}
		}
		return relations;
	}

	public List<ProvenanceRelation> getMostProbable(int initialNumberOfRelations) {
		List<ProvenanceRelation> mostProblableRelations = new ArrayList<ProvenanceRelation>();
		Iterator<ProvenanceRelation> relationsIt = this.orderedRelations.keySet().iterator();
		int i = 0;
		while (i < initialNumberOfRelations && relationsIt.hasNext()) {
			ProvenanceRelation relation = relationsIt.next();
			mostProblableRelations.add(relation);
			i++;
		}

		return mostProblableRelations;
	}

	@Override
	public Type getResourceType() {
		return null;
	}

	class RelationsComparator implements Comparator<ProvenanceRelation> {
		public RelationsComparator() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public int compare(ProvenanceRelation relationA, ProvenanceRelation relationB) {
			if (relationA.getWeight() < relationB.getWeight()) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	public void introduceRelation(ProvenanceRelation relation) {


		String relationURI = relation.getUri();

		LOG.info("RelationURI > " + relationURI);
		if (this.hasRelation(relationURI)) {
			// If the relation is already in the Relations Table, we have to
			// update just
			// add the new provenance sentence along with its relationhood
			ProvenanceRelation storedRelation = this.getRelation(relationURI);
			storedRelation.addAll(relation.getProvenances());

			// Since the relationhood of the relation has been update, we must
			// update its position in the ordered MapTree
			this.orderedRelations.put(storedRelation, relationURI);

		} else {
			this.orderedRelations.put(relation, relation.getUri());
			this.relations.put(relation.getUri(), relation);
			List<ProvenanceRelation> relations = this.relationsBySource.get(relation.getStartUri());
			if (relations == null) {
				relations = new ArrayList<>();
				this.relationsBySource.put(relation.getStartUri(), relations);
			}

			relations.add(relation);
		}
	}

	public void addRelation(ProvenanceRelation relation) {

		this.orderedRelations.put(relation, relation.getUri());
		this.relations.put(relation.getUri(), relation);

		List<ProvenanceRelation> relations = this.relationsBySource.get(relation.getStartUri());
		if (relations == null) {
			relations = new ArrayList<>();
			this.relationsBySource.put(relation.getStartUri(), relations);
		}
		relations.add(relation);
	}

	public ProvenanceRelation getRelation(String URI) {
		return this.relations.get(URI);
	}

	public Collection<ProvenanceRelation> getRelations() {
		return this.relations.values();
	}

	public Collection<ProvenanceRelation> getRelations(String type) {
		List<ProvenanceRelation> relations = new ArrayList<>();
		for (ProvenanceRelation relation : this.relations.values()) {
			if (type.equals(relation.getType())) {
				relations.add(relation);
			}
		}
		return relations;
	}

	public boolean hasRelation(String URI) {
		return (this.relations.get(URI) != null);
	}

	public int size() {
		return this.relations.size();
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uRI) {
		uri = uRI;
	}

	@Override
	public String toString() {
		return "RelationsTable [uri= " + uri + ", relations=" + relations + "]";
	}

	public void show(int numberOfDeatiledTerms) {

		LOG.info("=====================================================================================================================");
		LOG.info("Terms Table");

		LOG.info("=====================================================================================================================");

		LOG.info("# of candidate relations: " + this.size());
		LOG.info("The top most " + numberOfDeatiledTerms
				+ " probable relations are: ");
		int i = 1;
		for (Relation relation : this.getMostProbable(numberOfDeatiledTerms)) {
			LOG.info("(" + i++ + ")" + relation.getStartUri() + " > "
					+ relation.getType() + " > " + relation.getEndUri());

			LOG.info("------------------------------------------------------");
			LOG.info(""+relation);
			LOG.info("------------------------------------------------------");

		}

		LOG.info("=====================================================================================================================");
		LOG.info("=====================================================================================================================");
	}

}
