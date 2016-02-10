package org.epnoi.nlp.pool;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.nlp.NLPProcessor;
import org.epnoi.nlp.helper.NLPHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class NLPProcessorsPool {

	private static final Logger LOG = LoggerFactory.getLogger(NLPProcessorsPool.class);

	GenericObjectPool<NLPProcessor> pool;

	@Value("${epnoi.nlp.numberOfProcessors}")
	Integer numberOfProcessors;

	@Autowired
	NLPHelper helper;

	@PostConstruct
	public void setup() throws Exception {
		pool = new GenericObjectPool<NLPProcessor>(new PooledNLPProcessorFactory(helper));
		pool.setBlockWhenExhausted(true);
		pool.setMaxIdle(-1);
		pool.setMaxTotal(numberOfProcessors);
		int i = numberOfProcessors;
		while (i > 0) {
			pool.addObject();
			i--;
		}
	}

	public NLPProcessor borrowProcessor() throws EpnoiResourceAccessException {
		try {
			return this.pool.borrowObject();
		} catch (Exception e) {
			throw new EpnoiResourceAccessException(
					"There was a problem accessing the NLPProcessors pool");

		}
	}

	public void returnProcessor(NLPProcessor processor) {
		this.pool.returnObject(processor);
	}

}
