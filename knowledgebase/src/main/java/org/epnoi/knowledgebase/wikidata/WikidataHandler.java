package org.epnoi.knowledgebase.wikidata;

import java.util.Set;

public interface WikidataHandler {

	Set<String> getRelated(String source, String type);

	String stem(String word);
}
