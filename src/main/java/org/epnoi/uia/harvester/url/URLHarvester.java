package org.epnoi.uia.harvester.url;

import gate.Document;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Domain;
import org.epnoi.model.Item;
import org.epnoi.model.Paper;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.learner.nlp.TermCandidatesFinder;
import org.xml.sax.ContentHandler;

public class URLHarvester {
	private Core core;
	private String datePattern = "MM/dd/yyyy";
	private URLHarvesterParameters parameters;

	public String path = "/JUNK/drinventorcorpus/corpus";
	private boolean verbose;
	private String corpusLabel;

	private static final Logger logger = Logger.getLogger(URLHarvester.class
			.getName());
	TermCandidatesFinder termCandidatesFinder;

	// ----------------------------------------------------------------------------------------

	public URLHarvester() {

	}

	// ----------------------------------------------------------------------------------------

	private String _scanContent(String resourceURI) {
		Metadata metadata = new Metadata();
		metadata.set(Metadata.RESOURCE_NAME_KEY, resourceURI);
		InputStream is = null;
		ContentHandler handler = null;
		try {
			is = new URL(resourceURI).openStream();

			Parser parser = new AutoDetectParser();
			handler = new BodyContentHandler(-1);

			ParseContext context = new ParseContext();
			context.set(Parser.class, parser);

			parser.parse(is, handler, metadata, new ParseContext());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String content = handler.toString();
		content = content.replaceAll("\\r\\n|\\r|\\n", " ");
		content = content.replaceAll("\\s+", " ");
		// System.out.println("----> " + content);
		return content;
	}

	// ----------------------------------------------------------------------------------------

	public void harvest(String url, Domain domain) {

		try {

			logger.info("Harvesting the url " + url);

			Paper paper = _harvestURI(url);
			if (this.core != null) {
				_addDocument(domain, paper);

				_addDocumentAnnotatedContent(paper);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ----------------------------------------------------------------------------------------

	private void _addDocument(Domain domain, Paper paper) {
		// First the paper is added to the UIA
		core.getInformationHandler().put(paper, Context.getEmptyContext());

		// Later it is annotated as belonging to the harvested
		// corpus

		core.getAnnotationHandler().label(paper.getURI(), domain.getURI());
	}

	private void _addDocumentAnnotatedContent(Paper paper) {
		Document annotatedContent = this.termCandidatesFinder
				.findTermCandidates(paper.getDescription());

		Selector annotationSelector = new Selector();
		annotationSelector.setProperty(SelectorHelper.URI, paper.getURI());
		annotationSelector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI,
				paper.getURI() + "/"
						+ AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE);
		annotationSelector.setProperty(SelectorHelper.TYPE,
				RDFHelper.PAPER_CLASS);

		core.getInformationHandler().setAnnotatedContent(
				annotationSelector,
				new Content<Object>(annotatedContent,
						AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE));
	}

	// ----------------------------------------------------------------------------------------

	protected String convertDateFormat(String dateExpression) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = dateFormat.parse(dateExpression);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		return (dt1.format(date) + "^^xsd:date");

	}

	// ----------------------------------------------------------------------------------------

	public Paper _harvestURI(String filePath) {

		Paper paper = new Paper();

		String fileContent = _scanContent(filePath);
		paper.setURI(filePath);
		paper.setTitle(filePath);
		paper.setDescription(fileContent);
		return paper;
	}

	// -------------------------------------------------------------------------------------------------------------------

	public void init(Core core, URLHarvesterParameters parameters)
			throws EpnoiInitializationException {

		this.core = core;
		this.termCandidatesFinder = new TermCandidatesFinder();
		this.termCandidatesFinder.init(core);
		this.parameters = parameters;
		this.logger
				.info("Initializing the URLHarvester with the following parameters"
						+ this.parameters.toString());
		
		this.verbose = (boolean) this.parameters
				.getParameterValue(URLHarvesterParameters.VERBOSE_PARAMETER);

	}

	// -------------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		logger.info("Starting the harvesting!");

		URLHarvester harvester = new URLHarvester();
		URLHarvesterParameters parameters = new URLHarvesterParameters();

		parameters.setParameter(URLHarvesterParameters.VERBOSE_PARAMETER, true);

		parameters.setParameter(URLHarvesterParameters.OVERWRITE_PARAMETER,
				true);

		String url = "file:///opt/epnoi/epnoideployment/firstReviewResources/CGCorpus/A32_C02_Animating_Wrinkles_on_Clothes__CORPUS__v3.xml";
		String domainURI = "http://www.epnoi.org/testDomain";

		Domain domain = new Domain();
		domain.setURI(domainURI);

		Core core = CoreUtility.getUIACore();

		if (core.getInformationHandler().contains(url, RDFHelper.PAPER_CLASS)) {
			System.out
					.println("The harvested paper was already in the core, we should delete it");
			core.getInformationHandler().remove(url, RDFHelper.PAPER_CLASS);
		}

		core.getHarvestersHandler().harvestURL(url, domain);

		Paper paper = (Paper) core.getInformationHandler().get(url);
		System.out.println("This is the harvested paper " + paper);

		// ---

		Selector annotationSelector = new Selector();
		annotationSelector.setProperty(SelectorHelper.URI, paper.getURI());
		annotationSelector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI,
				paper.getURI() + "/"
						+ AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE);
		annotationSelector.setProperty(SelectorHelper.TYPE,
				RDFHelper.PAPER_CLASS);

		System.out.println("This is teh annotated content "
				+ core.getInformationHandler().getAnnotatedContent(
						annotationSelector));

		logger.info("Ending the harvesting!");
	}
}