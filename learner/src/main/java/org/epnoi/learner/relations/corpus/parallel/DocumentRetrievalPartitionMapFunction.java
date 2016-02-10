package org.epnoi.learner.relations.corpus.parallel;

import gate.Document;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Content;
import org.epnoi.model.Selector;
import org.epnoi.model.WikipediaPage;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DocumentRetrievalPartitionMapFunction implements FlatMapFunction<Iterator<String>, Document> {

	private static final Logger LOG = LoggerFactory.getLogger(DocumentRetrievalPartitionMapFunction.class);

	@Override
	public Iterable<Document> call(Iterator<String> URIs) throws Exception {
		List<Document> sectionsAnnotatedContent = new ArrayList<>();
		while (URIs.hasNext()) {
			String uri = URIs.next();

			// TODO
			LOG.error("Pending to implement by using UDM");
//			WikipediaPage page = (WikipediaPage) core.getInformationHandler().get(uri, RDFHelper.WIKIPEDIA_PAGE_CLASS);
			WikipediaPage page = null;

			List<String> sectionsAnnotatedContentURIs = _obtainSectionsAnnotatedContentURIs(page);
			List<Document> pageSectionsAnnotatedContent = _obtainSectionsAnnotatedContent(sectionsAnnotatedContentURIs);
			sectionsAnnotatedContent.addAll(pageSectionsAnnotatedContent);
		}
		return sectionsAnnotatedContent;
	}

	// --------------------------------------------------------------------------------------------------------------------

	private List<Document> _obtainSectionsAnnotatedContent(List<String> sectionsAnnotatedContentURIs) {

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, RDFHelper.WIKIPEDIA_PAGE_CLASS);

		List<Document> sectionsAnnotatedContent = new ArrayList<Document>();
		for (String uri : sectionsAnnotatedContentURIs) {
			
			selector.setProperty(SelectorHelper.URI, uri);

			// TODO
			LOG.error("Pending to implement by using UDM");
//			Content<Object> content = core.getInformationHandler().getAnnotatedContent(selector);
			Content<Object> content = null;


			Document sectionAnnotatedContent = (Document) content.getContent();

			if (sectionAnnotatedContent != null) {
				sectionsAnnotatedContent.add(sectionAnnotatedContent);
			}
		}

		return sectionsAnnotatedContent;
	}

	private List<String> _obtainSectionsAnnotatedContentURIs(WikipediaPage page) {
		List<String> URIs = new ArrayList<String>();
		for (String section : page.getSections()) {
			URIs.add(_extractURI(page.getUri(), section, AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE));
		}
		return URIs;
	}

	// --------------------------------------------------------------------------------------------------------------------

	private String _extractURI(String URI, String section, String annotationType) {

		String cleanedSection = section.replaceAll("\\s+$", "").replaceAll("\\s+", "_");

		return URI + "/" + cleanedSection + "/" + annotationType;
	}

}
