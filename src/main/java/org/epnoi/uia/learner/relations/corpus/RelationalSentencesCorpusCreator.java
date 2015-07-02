package org.epnoi.uia.learner.relations.corpus;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.DocumentContent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.OffsetRangeSelector;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.WikipediaPage;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.commons.GateUtils;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.knowledgebase.KnowledgeBase;
import org.epnoi.uia.learner.nlp.TermCandidatesFinder;
import org.epnoi.uia.learner.nlp.gate.NLPAnnotationsConstants;
import org.epnoi.uia.learner.relations.RelationalSentence;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

public class RelationalSentencesCorpusCreator {
	private static final Logger logger = Logger
			.getLogger(RelationalSentencesCorpusCreator.class.getName());

	private Core core;
	private TermCandidatesFinder termCandidatesFinder;
	private RelationalSentencesCorpus corpus;
	private KnowledgeBase knowledgeBase;
	private RelationalSentencesCorpusCreationParameters parameters;
	private boolean storeResult;
	private boolean verbose;
	private int MAX_SENTENCE_LENGTH = 100;
	private long nonRelationalSentencesCounter = 0;

	// ----------------------------------------------------------------------------------------------------------------------

	public void init(Core core,
			RelationalSentencesCorpusCreationParameters parameters)
			throws EpnoiInitializationException {
		logger.info("Initializing the RelationalSentencesCorpusCreator with the following parameters "+parameters.toString());
		this.core = core;
		this.parameters = parameters;
		this.corpus = new RelationalSentencesCorpus();
		this.termCandidatesFinder = new TermCandidatesFinder();
		this.termCandidatesFinder.init(core);
		this.knowledgeBase = core.getKnowledgeBaseHandler().getKnowledgeBase();

		this.storeResult = (boolean) parameters
				.getParameterValue(RelationalSentencesCorpusCreationParameters.STORE);

		this.verbose = (boolean) parameters
				.getParameterValue(RelationalSentencesCorpusCreationParameters.VERBOSE);
		this.MAX_SENTENCE_LENGTH=(int) parameters.getParameterValue(RelationalSentencesCorpusCreationParameters.MAX_SENTENCE_LENGTH_PARAMETER);

	}

	// ----------------------------------------------------------------------------------------------------------------------

	public void createCorpus() {

		logger.info("Creating a relational sencences corpus with the following parameters:");
		logger.info(this.parameters.toString());
		// This should be done in parallel!!
		_searchWikipediaCorpus();
		_searchReutersCorpus();

		corpus.setURI((String) this.parameters
				.getParameterValue(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER));
		corpus.setDescription((String) this.parameters
				.getParameterValue(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_DESCRIPTION_PARAMETER));
		corpus.setType((String) this.parameters
				.getParameterValue(RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_TYPE_PARAMETER));

		if (this.verbose) {
			_showRelationalSentenceCorpusInfo();
		}

		if (this.storeResult) {
			_storeCorpus();
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------

	private void _storeCorpus() {
		core.getInformationHandler().remove(this.corpus.getURI(),
				RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);
		core.getInformationHandler()
				.put(this.corpus, Context.getEmptyContext());
	}

	// ----------------------------------------------------------------------------------------------------------------------

	private void _showRelationalSentenceCorpusInfo() {
		System.out
				.println("------------------------------------------------------------------------------------------");
		System.out.println("Information about the corpus "
				+ this.corpus.getURI());
		System.out.println("Relations type: " + this.corpus.getType());
		System.out.println("Corpus description: "
				+ this.corpus.getDescription());
		System.out.println("It has " + this.corpus.getSentences().size()
				+ " relational sentences");
		/*
		 * for (RelationalSentence relationalSencente :
		 * this.corpus.getSentences()) {
		 * _showRelationalSentenceInfo(relationalSencente); }
		 */
		double average = 0.;
		for (RelationalSentence relationalSencente : this.corpus.getSentences()) {
			average += relationalSencente.getSentence().length();
		}
		System.out.println("The average length is " + average
				/ this.corpus.getSentences().size());
		System.out.println("There were " + nonRelationalSentencesCounter
				+ " that were left out as no relational");
		System.out
				.println("------------------------------------------------------------------------------------------");

	}

	// ----------------------------------------------------------------------------------------------------------------------

	private void _searchReutersCorpus() {
		// TODO Auto-generated method stub

	}

	// ----------------------------------------------------------------------------------------------------------------------

	private void _searchWikipediaCorpus() {
		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE,
				RDFHelper.WIKIPEDIA_PAGE_CLASS);
		// String uri = "http://en.wikipedia.org/wiki/AccessibleComputing";
		int nullCounts = 1;
		int sectionsCount = 1;
		int count = 1;
		List<String> wikipediaPages = _getWikipediaArticles();
		logger.info(wikipediaPages.size() + " wikipedia pages were retrieved");
		for (String uri : wikipediaPages) {

			if (this.core.getInformationHandler().contains(uri,
					RDFHelper.WIKIPEDIA_PAGE_CLASS)) {

				// System.out.println(count++ + " Retrieving " + uri);
				logger.info("Analyzing " + count++ + "> " + uri);
				WikipediaPage wikipediaPage = (WikipediaPage) this.core
						.getInformationHandler().get(uri,
								RDFHelper.WIKIPEDIA_PAGE_CLASS);

				selector.setProperty(SelectorHelper.URI, uri);
				for (String section : wikipediaPage.getSections()) {
					// logger.info("				Section:  " + section);
					selector.setProperty(
							SelectorHelper.ANNOTATED_CONTENT_URI,
							_extractURI(
									uri,
									section,
									AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE));
					// System.out.println("selector >" + selector);
					Content<Object> annotatedContent = this.core
							.getInformationHandler().getAnnotatedContent(
									selector);
					if (annotatedContent != null) {
						Document annotatedContentDocument = (Document) annotatedContent
								.getContent();

						_searchDocument(annotatedContentDocument);
						sectionsCount++;

					} else {
						/*
						 * System.out.println("The section " + section + " of "
						 * + uri + " was null");
						 */
						nullCounts++;
					}
				}

			}

		}
		logger.info("The number of not nulls is " + sectionsCount
				+ " of nulls is " + nullCounts);
	}

	// ----------------------------------------------------------------------------------------------------------------------

	public boolean _isValid(Annotation sentenceAnnotation) {
		System.out.println("--------------------------> "
				+ sentenceAnnotation.getFeatures());
		if (sentenceAnnotation.getFeatures() != null
				&& sentenceAnnotation.getFeatures().get("string") != null
				&& sentenceAnnotation.getFeatures().get("string").toString()
						.length() < MAX_SENTENCE_LENGTH) {
			System.out.println("--------------------------> "
					+ sentenceAnnotation.getFeatures().get("string").toString()
							.length());
		}

		return (sentenceAnnotation.getFeatures() != null
				&& sentenceAnnotation.getFeatures().get("string") != null && sentenceAnnotation
				.getFeatures().get("string").toString().length() < MAX_SENTENCE_LENGTH);

	}

	// ----------------------------------------------------------------------------------------------------------------------

	/**
	 * Method that scans all the sentences in a document testing if they are
	 * definitional or not
	 * 
	 * @param document
	 */

	public void _searchDocument(Document document) {

		AnnotationSet sentenceAnnotations = document.getAnnotations().get(
				NLPAnnotationsConstants.SENTENCE);
		DocumentContent sentenceContent = null;
		AnnotationSet sentencesAnnotations = document.getAnnotations();
		Iterator<Annotation> sentencesIt = sentenceAnnotations.iterator();
		while (sentencesIt.hasNext()) {
			Annotation sentenceAnnotation = sentencesIt.next();

			_searchSentence(document, sentencesAnnotations, sentenceAnnotation);

		}

	}
	
	// ----------------------------------------------------------------------------------------------------------------------

	private void _searchSentence(Document document,
			AnnotationSet sentencesAnnotations, Annotation sentenceAnnotation) {
		DocumentContent sentenceContent;
		Long sentenceStartOffset = sentenceAnnotation.getStartNode()
				.getOffset();
		Long sentenceEndOffset = sentenceAnnotation.getEndNode().getOffset();

		sentenceContent = GateUtils.extractAnnotationContent(
				sentenceAnnotation, document);
		if (sentenceContent != null
				&& sentenceContent.size() < MAX_SENTENCE_LENGTH) {

			_testSentence(sentenceAnnotation, sentenceContent,
					sentencesAnnotations.getContained(sentenceStartOffset,
							sentenceEndOffset));
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------

	private void _testSentence(Annotation sentence,
			DocumentContent sentenceContent,
			AnnotationSet sentenceAnnotationsSet) {
		Long sentenceStartOffset = sentence.getStartNode().getOffset();
		Long sentenceEndOffset = sentence.getEndNode().getOffset();

		Set<String> sentenceTerms = new HashSet<String>();
		// This table stores the string representation of each sentence terms
		// and their corresponding annotation
		Map<String, Annotation> termsAnnotationsTable = _createTermsAnnotationsTable(
				sentenceContent, sentenceAnnotationsSet, sentenceStartOffset,
				sentenceTerms);
		for (String term : sentenceTerms) {
			if (term != null && term.length() > 0) {
				// For each term we retrieve its well-known hypernyms
				Set<String> termHypernyms = this.knowledgeBase
						.getHypernyms(term);
				termHypernyms.retainAll(sentenceTerms);
				termHypernyms.removeAll(this.knowledgeBase.stem(term));
			
				

				// If the intersection of the well-known hypernyms and the terms
				// that belong to the sentence, this is a relational sentence
				if (termHypernyms.size() > 0) {

					_createRelationalSentence(sentenceContent,
							sentenceStartOffset, termsAnnotationsTable, term,
							termHypernyms);

				} else {
					nonRelationalSentencesCounter++;
				}
			}
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------

	private void _createRelationalSentence(DocumentContent sentenceContent,
			Long sentenceStartOffset,
			Map<String, Annotation> termsAnnotationsTable, String term,
			Set<String> termHypernyms) {
		Annotation sourceTermAnnotation = termsAnnotationsTable.get(term);

		// Note that the offset is relative to the beginning of the
		// sentence
		OffsetRangeSelector source = new OffsetRangeSelector(
				sourceTermAnnotation.getStartNode().getOffset()
						- sentenceStartOffset, sourceTermAnnotation
						.getEndNode().getOffset() - sentenceStartOffset);
		// For each target term a relational sentence is created
		for (String destinationTerm : termHypernyms) {

			Annotation destinationTermAnnotation = termsAnnotationsTable
					.get(destinationTerm);

			// Note that the offset is relative to the beginning of
			// the
			// sentence
			OffsetRangeSelector target = new OffsetRangeSelector(
					destinationTermAnnotation.getStartNode().getOffset()
							- sentenceStartOffset, destinationTermAnnotation
							.getEndNode().getOffset() - sentenceStartOffset);

			Document annotatedContent = termCandidatesFinder
					.findTermCandidates(sentenceContent.toString());

			RelationalSentence relationalSentence = new RelationalSentence(
					source, target, sentenceContent.toString(),
					annotatedContent.toXml());

			corpus.getSentences().add(relationalSentence);
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------

	private Map<String, Annotation> _createTermsAnnotationsTable(
			DocumentContent sentenceContent,
			AnnotationSet sentenceAnnotationsSet, Long sentenceStartOffset,
			Set<String> sentenceTerms) {
		HashMap<String, Annotation> termsAnnotationsTable = new HashMap<String, Annotation>();
		for (Annotation termAnnotation : sentenceAnnotationsSet
				.get(NLPAnnotationsConstants.TERM_CANDIDATE)) {
			Long startOffset = termAnnotation.getStartNode().getOffset()
					- sentenceStartOffset;
			Long endOffset = termAnnotation.getEndNode().getOffset()
					- sentenceStartOffset;

			String term = "";
			try {
				// First of all we retrieve the surface form of the term

				term = sentenceContent.getContent(startOffset, endOffset)
						.toString();

			} catch (Exception e) {
				term = "";

			}

			// We stem the surface form (we left open the possibility of
			// different stemming results so we consider a set of stemmed
			// forms)
			_addTermToTermsTable(sentenceTerms, termsAnnotationsTable,
					termAnnotation, term);
		}
		return termsAnnotationsTable;
	}

	// ----------------------------------------------------------------------------------------------------------------------

	private void _addTermToTermsTable(Set<String> sentenceTerms,
			HashMap<String, Annotation> termsAnnotationsTable,
			Annotation termAnnotation, String term) {
		if (term.length() > 2) {
			for (String stemmedTerm : this.knowledgeBase.stem(term)) {

				termsAnnotationsTable.put(stemmedTerm, termAnnotation);
				sentenceTerms.add(stemmedTerm);

			}
			termsAnnotationsTable.put(term, termAnnotation);
			sentenceTerms.add(term);
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------

	/**
	 * Method that obtains the list of URIs of the wikipedia articles in the
	 * corpus
	 * 
	 * @return
	 */

	private List<String> _getWikipediaArticles() {
		logger.info("Retrieving the URIs of the Wikipedia articles ");

		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		String queryExpression = "SELECT DISTINCT  ?uri FROM <{GRAPH}>"
				+ " { ?uri a <{WIKIPEDIA_PAPER_CLASS}> " + "}";

		queryExpression = queryExpression.replace(
				"{GRAPH}",
				((VirtuosoInformationStoreParameters) informationStore
						.getParameters()).getGraph()).replace(
				"{WIKIPEDIA_PAPER_CLASS}", RDFHelper.WIKIPEDIA_PAGE_CLASS);

		List<String> queryResults = informationStore.query(queryExpression);

		logger.info("The number of retrived Wikipeda articles are "
				+ queryResults.size());
		return queryResults;
	}

	// ----------------------------------------------------------------------------------------------------------------------

	private String _extractURI(String URI, String section, String annotationType) {

		String cleanedSection = section.replaceAll("\\s+$", "").replaceAll(
				"\\s+", "_");

		return URI + "/" + cleanedSection + "/" + annotationType;
	}

	// ----------------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		logger.info("Starting the Relation Sentences Corpus Creator");

		RelationalSentencesCorpusCreator relationSentencesCorpusCreator = new RelationalSentencesCorpusCreator();

		Core core = CoreUtility.getUIACore();

		RelationalSentencesCorpusCreationParameters parameters = new RelationalSentencesCorpusCreationParameters();

		String relationalCorpusURI = "http://drInventorFirstReview/relationalSentencesCorpus";

		parameters
				.setParameter(
						RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER,
						relationalCorpusURI);

		parameters
				.setParameter(
						RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_TYPE_PARAMETER,
						RelationHelper.HYPERNYM);

		parameters
				.setParameter(
						RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_DESCRIPTION_PARAMETER,
						"DrInventor first review relational sentences corpus");

		parameters
				.setParameter(
						RelationalSentencesCorpusCreationParameters.RELATIONAL_SENTENCES_CORPUS_URI_PARAMETER,
						relationalCorpusURI);

		parameters
				.setParameter(
						RelationalSentencesCorpusCreationParameters.MAX_SENTENCE_LENGTH_PARAMETER,
						80);

		parameters.setParameter(
				RelationalSentencesCorpusCreationParameters.STORE, true);

		parameters.setParameter(
				RelationalSentencesCorpusCreationParameters.VERBOSE, true);

		try {
			relationSentencesCorpusCreator.init(core, parameters);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		/*
		 * RelationalSentencesCorpus testRelationalSentenceCorpus =
		 * relationSentencesCorpusCreator .createTestCorpus();
		 * 
		 * System.out.println("testCorpus>" + testRelationalSentenceCorpus);
		 * 
		 * core.getInformationHandler().put(testRelationalSentenceCorpus,
		 * Context.getEmptyContext());
		 * 
		 * System.out.println(core.getInformationHandler().get(
		 * testRelationalSentenceCorpus.getURI()));
		 * 
		 * System.exit(0);
		 */

		relationSentencesCorpusCreator.createCorpus();

		System.out
				.println("Checking if the Relational Sentence Corpus can be retrieved");

		RelationalSentencesCorpus relationalSentenceCorpus = (RelationalSentencesCorpus) core
				.getInformationHandler().get(relationalCorpusURI,
						RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);
		System.out.println("The readed relational sentences corpus "
				+ relationalSentenceCorpus);
		logger.info("Stopping the Relation Sentences Corpus Creator");
	}

	// ----------------------------------------------------------------------------------------------------------------------

	public RelationalSentencesCorpus createTestCorpus() {
		String relationalSentenceURI = "http://thetestcorpus/drinventor";
		RelationalSentencesCorpus relationalSentencesCorpus = new RelationalSentencesCorpus();
		relationalSentencesCorpus.setDescription("The test corpus");
		relationalSentencesCorpus.setURI(relationalSentenceURI);
		// relationalSentencesCorpus.setType(RelationHelper.HYPERNYM);

		Document annotatedContentA = termCandidatesFinder
				.findTermCandidates("A dog is a canine");
		RelationalSentence relationalSentenceA = new RelationalSentence(
				new OffsetRangeSelector(2L, 5L), new OffsetRangeSelector(11L,
						17L), "A dog is a canine", annotatedContentA.toXml());

		Document annotatedContentB = termCandidatesFinder
				.findTermCandidates("A dog, is a canine (and other things!)");

		RelationalSentence relationalSentenceB = new RelationalSentence(
				new OffsetRangeSelector(2L, 5L), new OffsetRangeSelector(12L,
						18L), "A dog, is a canine (and other things!)",
				annotatedContentB.toXml());

		relationalSentencesCorpus.getSentences().add(relationalSentenceA);

		relationalSentencesCorpus.getSentences().add(relationalSentenceB);
		return relationalSentencesCorpus;
	}

}
