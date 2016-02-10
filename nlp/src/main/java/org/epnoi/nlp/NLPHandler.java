package org.epnoi.nlp;

import gate.Document;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.nlp.pool.NLPProcessorsPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NLPHandler {

	@Autowired
	NLPProcessorsPool pool;


	public Document process(String content) throws EpnoiResourceAccessException {
		NLPProcessor processor = pool.borrowProcessor();
		Document document = processor.process(content);
		pool.returnProcessor(processor);
		return document;
	}

}
