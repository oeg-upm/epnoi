package org.epnoi.nlp;

import com.rits.cloning.Cloner;
import gate.Annotation;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.corpora.DocumentImpl;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.util.InvalidOffsetException;
import org.epnoi.nlp.helper.NLPHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NLPProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(NLPProcessor.class);

	private final SerialAnalyserController controller;

	private final NLPHelper helper;

	Corpus corpus;

	Cloner cloner;

	public NLPProcessor(NLPHelper helper) throws ResourceInstantiationException {
		LOG.info("Initializing NLP Processor..");
		this.helper = helper;
		this.cloner = new Cloner();
		this.controller = helper.getController().createController();
		this.corpus = Factory.newCorpus("Working Corpus");
		this.controller.setCorpus(this.corpus);
		LOG.info("NLP Processor initialized successfully");
	}

	public Document process(String content) {
		Document document = null;
		try {
			LOG.trace("Trying to process: " + content);
			document = Factory.newDocument(content);
			if (document.getContent().size() > helper.getMinContentLength()) {
				this.corpus.add(document);

				try {
					controller.execute();
				} catch (ExecutionException e) {
					document = _handleProcessingException(e);
				}
				corpus.remove(0);
			}

			Document clonedDocument = cloner.deepClone(document);
			release(document);
			return clonedDocument;
		} catch (ResourceInstantiationException e) {
			document = _handleResourceInstantiationException(e);
		}
		return document;
	}

	private Document _handleProcessingException(ExecutionException e) {
		LOG.warn("There was an error processing the document, an empty annotated document has been created: "+ e.getMessage());
		return new DocumentImpl();
	}

	private Document _handleResourceInstantiationException(Exception e) {
		LOG.warn("There was an error locating resources while processing the document, an empty annotated document has been created: " +e.getMessage());
		return new DocumentImpl();
	}


	public void release(Document document) {
		try {
			LOG.debug("releasing document: " + document);
			Factory.deleteResource(document);
		} catch (Exception e) {
			LOG.error("Error releasing document: " + document,e);
		}
	}

	private static void showTerms(Document document) {
		for (Annotation annotation : document.getAnnotations().get("TermCandidate")) {
			// System.out.println("The rule
			// :>"+annotation.getFeatures().get("rule"));
			annotation.getStartNode();
			try {
				System.out.println(document.getContent().getContent(annotation.getStartNode().getOffset(),
						annotation.getEndNode().getOffset()));
			} catch (InvalidOffsetException e) {
				LOG.error("Error getting terms",e);
			}
		}

	}

	private static void showDependencies(Document document) {

		for (Annotation dependencyAnnotation : document.getAnnotations().get("Dependency")) {
			// System.out.println("The rule
			// :>"+annotation.getFeatures().get("rule"));

			List<Integer> ids = (List<Integer>) dependencyAnnotation.getFeatures().get("args");
			System.out.println(
					"--------------------------------------------------------------------------------------------------------------------------------");
			System.out.println(dependencyAnnotation.getFeatures().get("kind"));

			for (Integer id : ids) {

				System.out.println(document.getAnnotations().get(id).getFeatures().get("string"));

			}

			// System.out.println("> "+dependencyAnnotation);

		}

	}
/*
	private static void createDependencyGraph(Document document) {

		Graph<Integer, SyntacticPatternGraphEdge> patternGraph = new SimpleGraph<Integer, SyntacticPatternGraphEdge>(
				SyntacticPatternGraphEdge.class);

		for (Annotation dependencyAnnotation : document.getAnnotations().get("Dependency")) {


			List<Integer> ids = (List<Integer>) dependencyAnnotation.getFeatures().get("args");
			System.out.println(
					"--------------------------------------------------------------------------------------------------------------------------------");

			System.out.println();
			String kind = (String) dependencyAnnotation.getFeatures().get("kind");

			Integer source = ids.get(0);
			Integer target = ids.get(1);

			if (source != null && target != null) {
				patternGraph.addVertex(source);
				patternGraph.addVertex(target);
				patternGraph.addEdge(source, target, new SyntacticPatternGraphEdge(kind));
			} else {
				System.out.println("Source > " + source + " > " + "Target > " + target);
			}

		}
		System.out.println("--> " + patternGraph.toString());

	}
*/
	// ----------------------------------------------------------------------------------
/*FOR_TEST
	public static void main(String[] args) {

		System.out.println("TermCandidatesFinder test================================================================");

		Core core = CoreUtility.getUIACore();

		NLPProcessor termCandidatesFinder = new NLPProcessor();
		termCandidatesFinder.init(core);

		Document document = termCandidatesFinder
				.process("Bell, a company which is based in LA, makes and distributes computer products");

		String documentAsString = document.toXml();
		
		Document document2 = null;

		Utils.featureMap(gate.Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME, documentAsString,
				gate.Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME, "text/xml");
		try {
			document2 = (Document) Factory.createResource("gate.corpora.DocumentImpl",
					Utils.featureMap(gate.Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME, documentAsString,
							gate.Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME, "text/xml"));
		} catch (ResourceInstantiationException e) {
			e.printStackTrace();
		}
		// System.out.println("mmm> " + document2.toXml());
		createDependencyGraph(document);
		System.out.println(">>> " + document.toString());

		System.out.println(
				"TermCandidatesFinder test is over!================================================================");
	}
*/
}