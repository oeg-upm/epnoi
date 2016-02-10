package org.epnoi.nlp.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.epnoi.model.parameterization.ParametersModel;
import org.epnoi.nlp.NLPProcessor;
import org.epnoi.nlp.helper.NLPHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PooledNLPProcessorFactory implements PooledObjectFactory<NLPProcessor> {

	private static final Logger LOG = LoggerFactory.getLogger(PooledNLPProcessorFactory.class);

	private final NLPHelper helper;

	public PooledNLPProcessorFactory(NLPHelper helper) {
		this.helper=helper;
	}

	
	@Override
	public void activateObject(PooledObject<NLPProcessor> object)
			throws Exception {
		LOG.debug("activated object: " + object);
	}

	@Override
	public void destroyObject(PooledObject<NLPProcessor> object) throws Exception {
		LOG.debug("destroyed object: " + object);
	}

	@Override
	public PooledObject<NLPProcessor> makeObject() throws Exception {
		return new DefaultPooledObject<NLPProcessor>(new NLPProcessor(helper));
	}

	@Override
	public void passivateObject(PooledObject<NLPProcessor> object)
			throws Exception {
		LOG.debug("passivate object: " + object);
	}

	@Override
	public boolean validateObject(PooledObject<NLPProcessor> object) {
		LOG.debug("validated object: " + object);
		return true;
	}

}
