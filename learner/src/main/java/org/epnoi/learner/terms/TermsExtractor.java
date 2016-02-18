package org.epnoi.learner.terms;

import gate.Annotation;
import gate.Document;
import gate.Factory;
import gate.Utils;
import gate.creole.ResourceInstantiationException;
import org.epnoi.learner.DomainsTable;
import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.model.*;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.domain.resources.Term;
import org.epnoi.nlp.gate.NLPAnnotationsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;

public class TermsExtractor {

	private static final Logger logger = LoggerFactory.getLogger(TermsExtractor.class);
	private static final List<String> stopwords = Arrays.asList(new String[] {
			"comment", "comments", "proceedings", "example", "examples",
			"symposium", "conference", "copyright", "approach", "figure",
			"figures" });
	private static final int MIN_TERM_LENGTH = 4;
	// private List<String> consideredDomains;
	private String targetDomain;
	private String consideredResources;
	// private Map<String, List<String>> resourcePerConsideredDomain;
	private TermsIndex termsIndex;
	private ResourcesIndex resourcesIndex;
	private DomainsIndex domainsIndex;
	private double cValueWeight = 0.5;
	private final double domainPertinenceWeight = 0.2;
	private final double domainConsensusWeight = 1 - cValueWeight- domainPertinenceWeight;

	LearningHelper helper;

	private DomainsTable domainsTable;

	public void init(DomainsTable domainsTable,LearningHelper helper) {
		logger.info("Initializing the TermExtractor with the following parameters: " + helper);
		this.helper = helper;

		this.domainsTable = domainsTable;

		this.targetDomain = domainsTable.getTargetDomain().getUri();

		this.termsIndex = new TermsIndex();
		this.termsIndex.init();

		this.resourcesIndex = new ResourcesIndex();
		this.resourcesIndex.init();

		this.domainsIndex = new DomainsIndex();
		this.domainsIndex.init();
	}

	public void indexResources() {


		logger.info("Indexing the textual resources to extract the terminology ");
		for (String domain : this.domainsTable.getConsideredDomains()) {
			logger.info("Indexing the domain: " + domain);
			this._indexDomainResoures(domain);
		}

	}

	private void _indexDomainResoures(String domain) {

		List<String> resourcesURIs = this.domainsTable.getDomainResources()
				.get(domain);
		//System.out.println(" resourceURIS" + resourcesURIs);
		for (String resourceURI : resourcesURIs) {
			logger.info("Indexing the resource " + resourceURI);
			_indexResource(domain, resourceURI);
		}
		long total = 0;
		for (AnnotatedWord<ResourceMetadata> resource : this.resourcesIndex
				.getResources(domain)) {
			total += resource.getAnnotation().getNumberOfTerms();
		}
		AnnotatedWord<DomainMetadata> indexedDomain = this.domainsIndex
				.lookUp(domain);
		if (indexedDomain != null) {

			indexedDomain.getAnnotation().setNumberOfTerms(total);
		}
	}

	private void _indexResource(String domainUri, String documentUri) {
		Document annotatedDocument = (Document) retrieveAnnotatedDocument(documentUri).getContent();

		TermCandidateBuilder termCandidateBuilder = new TermCandidateBuilder(helper,annotatedDocument);

		for (Annotation annotation : annotatedDocument.getAnnotations().get(NLPAnnotationsConstants.TERM_CANDIDATE)) {

			Term termCandidate = termCandidateBuilder.buildTermCandidate(annotation);
			String word = termCandidate.getContent();

			if ((word.length() > MIN_TERM_LENGTH) && !stopwords.contains(word)) {
				this.termsIndex.updateTerm(domainUri, termCandidate);
				this.resourcesIndex.updateTerm(domainUri, documentUri, termCandidate);

				for (Term subTerm : termCandidateBuilder.splitTermCandidate(termCandidate)) {
					this.resourcesIndex.updateTerm(domainUri, documentUri, subTerm);
				}

				this.domainsIndex.updateTerm(domainUri, documentUri);
			}
		}
	}

	private Document retrieveAnnotatedDocument(String documentUri) {


		Optional<Resource> res = helper.getUdm().read(Resource.Type.DOCUMENT).byUri(documentUri);

		if (!res.isPresent()){
			throw new RuntimeException("No document found in DDBB by uri: " + documentUri);
		}

		// TODO Store annotated document previously in DDBB
		Document document = null;
		try {
			document = (Document) Factory.createResource( "gate.corpora.DocumentImpl",
					Utils.featureMap(gate.Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME, ((org.epnoi.model.domain.resources.Document) res.get()).getContent(), gate.Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME, "text/plain")); //text/xml

		  } catch (ResourceInstantiationException e) {
		  	logger.error("Couldn't retrieve the GATE document that represents the annotated content of " + documentUri,e);
		}

		return document;
	}

	public void extractTerms() {

		this.indexResources();
		if (!this.domainsTable.getDomainResources().get(this.targetDomain).isEmpty()) {
			this.calculateCValues();
			this.calculateDomainConsensus();
			this.calculateDomainPertinence();
			this.normalizeAnDeriveMeasures();
		}
	}

	private void normalizeAnDeriveMeasures() {

		logger.info("Starting the normalization of cValue and Domain Consensus values");
		for (String domain : this.domainsTable.getConsideredDomains()) {
			double maxCValue = this.domainsIndex.lookUp(domain).getAnnotation().getMaxCValue();
			double minCValue = this.domainsIndex.lookUp(domain).getAnnotation().getMinCValue();

			double maxDomainConsesus = this.domainsIndex.lookUp(domain)
					.getAnnotation().getMaxDomainConsensus();
			double minDomainConsesus = this.domainsIndex.lookUp(domain)
					.getAnnotation().getMinDomainConsensus();

			for (Term termCandidate : this.termsIndex.getTermCandidates(domain)) {

				termCandidate.setCvalue(
						_normalize(termCandidate.getCvalue(), minCValue, maxCValue));
				termCandidate.setConsensus(
						_normalize(termCandidate.getConsensus(), minDomainConsesus,
								maxDomainConsesus));

				//
				_termhoodCalculation(termCandidate);

			}

		}
	}

	private void _termhoodCalculation(Term termCandidate) {
		termCandidate.setTermhood(
				termCandidate.getCvalue() * cValueWeight
						+ termCandidate.getConsensus()
						* domainConsensusWeight
						+ termCandidate.getPertinence()
						* domainPertinenceWeight);

	}

	private double _normalize(double value, double min, double max) {
		return (value - min) / (max - min);
	}

	private void calculateCValues() {

		logger.info("Starting the calculation of the cValues");
		for (String domain : this.domainsTable.getConsideredDomains()) {
			TermCandidateBuilder termCandidateBuilder = new TermCandidateBuilder(helper,null);
			for (Term termCandidate : this.termsIndex
					.getTermCandidates(domain)) {

				for (Term subTerm : termCandidateBuilder
						.splitTermCandidate(termCandidate)) {
					this.termsIndex.updateSubTerm(domain, termCandidate,
							subTerm);

				}

			}

			for (Term termCandidate : this.termsIndex
					.getTermCandidates(domain)) {

				double cValue = CValueCalculator.calculateCValue(termCandidate);
				termCandidate.setCvalue(cValue);
				// System.out.println("el cvalue es " + cValue);
				if (cValue > this.domainsIndex.getDomain(domain)
						.getAnnotation().getMaxCValue()) {
					this.domainsIndex.getDomain(domain).getAnnotation()
							.setMaxCValue(cValue);
				} else if (cValue < this.domainsIndex.getDomain(domain)
						.getAnnotation().getMinCValue()) {
					this.domainsIndex.getDomain(domain).getAnnotation()
							.setMinCValue(cValue);
				}

			}

		}
	}

	private void calculateDomainPertinence() {
		logger.info("Calculating the domain pertinence");
		for (String domain : this.domainsTable.getConsideredDomains()) {
			long totalOcurrences = this.domainsIndex.lookUp(domain)
					.getAnnotation().getNumberOfTerms();

			for (Term termCandidate : this.termsIndex
					.getTermCandidates(domain)) {

				termCandidate.setProbability(
								((double) termCandidate.getOcurrences())
										/ ((double) totalOcurrences));
				List<Double> ocurrencesInOtherDomains = new ArrayList<>();
				for (String otherDomain : this.domainsTable
						.getConsideredDomains()) {
					Term term = this.termsIndex.lookUp(
							otherDomain, termCandidate.getContent());

					if (term != null) {

						ocurrencesInOtherDomains.add(((double) term.getOcurrences())
								/ ((double) this.domainsIndex
										.lookUp(otherDomain).getAnnotation()
										.getNumberOfTerms()));
					}
				}

				double maxOcurrences = Collections
						.max(ocurrencesInOtherDomains);
				termCandidate.setPertinence(
						termCandidate.getProbability()
								/ maxOcurrences);
			}
		}
	}

	private void calculateDomainConsensus() {
		logger.info("Calculating the domain pertinence");
		for (String domain : this.domainsTable.getConsideredDomains()) {

			for (String resourceURI : this.domainsIndex.getDomain(domain)
					.getAnnotation().getResources()) {
				AnnotatedWord<ResourceMetadata> resource = this.resourcesIndex
						.getResource(domain, resourceURI);
				for (Entry<String, Long> termCandidateEntry : resource
						.getAnnotation().getTermsOcurrences().entrySet()) {
					Term termCandidate = this.termsIndex
							.lookUp(domain, termCandidateEntry.getKey());
					updateDomainConsensus(
							domain,
							termCandidate,
							resource.getAnnotation().getTermsOcurrences()
									.get(termCandidateEntry.getKey()), resource
									.getAnnotation().getNumberOfTerms());
				}
			}

		}

	}

	private void updateDomainConsensus(String domain,
			Term termCandidate, long termOcurrences,
			long resourceTermOcurrences) {

		double probabiltyTermResource = ((double) termOcurrences) / ((double) resourceTermOcurrences);

		double resourceConsensus = -probabiltyTermResource * Math.log(probabiltyTermResource);

		termCandidate.setConsensus(termCandidate.getConsensus() + resourceConsensus);

		if (termCandidate.getConsensus() > this.domainsIndex
				.getDomain(domain).getAnnotation().getMaxDomainConsensus()) {
			this.domainsIndex
					.getDomain(domain)
					.getAnnotation()
					.setMaxDomainConsensus(termCandidate.getConsensus());
		}

		if (termCandidate.getConsensus() < this.domainsIndex
				.getDomain(domain).getAnnotation().getMinDomainConsensus()) {
			this.domainsIndex
					.getDomain(domain)
					.getAnnotation()
					.setMinDomainConsensus(termCandidate.getConsensus());
		}

	}

	public TermsTable extract() {
		logger.info("Extracting terms with the following parameters "+ helper);
		this.extractTerms();
		TermsTable termsTable = new TermsTable();

		for (Term term : this.termsIndex.getTerms(this.targetDomain)) {
			termsTable.addTerm(term);
		}

		return termsTable;
	}

}
