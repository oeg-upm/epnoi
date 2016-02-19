package org.epnoi.learner.relations.patterns.lexical;

import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.learner.relations.patterns.*;
import org.epnoi.model.RelationalSentencesCorpus;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LexicalRelationalModelTester {
	private static final Logger LOG = LoggerFactory.getLogger(LexicalRelationalModelTester.class);

	private RelationalPatternsCorpusCreator patternsCorpusCreator;
	private RelationalPatternsCorpus patternsCorpus;
	private RelationalPatternsModelSerializer modelSerializer;
	private String relationalSentencesCorpusURI;
	private BigramSoftPatternModel model;
	private boolean store;
	private boolean verbose;
	private boolean test;
	private String path;

	// ----------------------------------------------------------------------------------------------------------------

	public void init(LearningHelper helper) throws EpnoiInitializationException {
		LOG.info("Initializing the LexicalRelationalModelTester with the following parameters: " + this);

		relationalSentencesCorpusURI = helper.getSentencesUri();

		this.path = helper.getLexicalPath();

		this.patternsCorpusCreator = new RelationalPatternsCorpusCreator();
		this.patternsCorpusCreator.init(new LexicalRelationalPatternGenerator());

		try {
			this.model = (BigramSoftPatternModel) RelationalPatternsModelSerializer.deserialize(this.path);
		} catch (EpnoiResourceAccessException e) {
			throw new EpnoiInitializationException(
					"The model couldn't not be retrieved at " + this.path);
		}

		this.verbose = helper.isLexicalVerbose();

	}

	// ----------------------------------------------------------------------------------------------------------------

	public void test() {

		createPatternsModel();

		testPatternsModel();
	}

	private void testPatternsModel() {
		double averageProbability = 0;
		long startingTime = System.currentTimeMillis();
		LOG.info("Testing all the patterns against the model");
		for (RelationalPattern pattern : patternsCorpus.getPatterns()) {
			double patternProbability = this.model
					.calculatePatternProbability(pattern);
			System.out.println("> " + patternProbability);
			averageProbability += patternProbability;
		}
		long totalTime = startingTime - System.currentTimeMillis();
		LOG.info("It took " + Math.abs(totalTime) + " ms to test the model");

		LOG.info("The average probability is " + averageProbability
				/ patternsCorpus.getPatterns().size());

	}

	// ----------------------------------------------------------------------------------------------------------------

	public void createPatternsModel() {
		// TODO
		LOG.error("pending to implement by using UDM");
//		RelationalSentencesCorpus relationalSentencesCorpus = (RelationalSentencesCorpus) this.core.getInformationHandler().get(relationalSentencesCorpusURI,RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);
		RelationalSentencesCorpus relationalSentencesCorpus = null;



		if (relationalSentencesCorpus != null) {
			LOG.info("The relational sentences  has "
					+ relationalSentencesCorpus.getSentences().size()
					+ " sentences");
			patternsCorpus = patternsCorpusCreator
					.buildCorpus(relationalSentencesCorpus);

			LOG.info("The RelationalPatternsCorpus has "
					+ patternsCorpus.getPatterns().size() + " patterns");
		} else {
			LOG.error("The relational sentences corpus with URI "
					+ this.relationalSentencesCorpusURI
					+ " couldn't not be retrieved");
		}
	}


}
