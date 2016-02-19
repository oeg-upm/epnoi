package org.epnoi.learner.relations.patterns;

import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.learner.relations.patterns.lexical.RelaxedBigramSoftPatternModelBuilder;
import org.epnoi.learner.relations.patterns.syntactic.SyntacticRelationalPatternsModelBuilder;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;

public class RelationalPatternsModelBuilderFactory {

	public static RelationalPatternsModelBuilder build(LearningHelper helper, String type) throws EpnoiResourceAccessException {

		RelationalPatternsModelBuilder relationalPatternsGenerator = null;
		switch (type) {
		case PatternsConstants.LEXICAL:
			relationalPatternsGenerator = new RelaxedBigramSoftPatternModelBuilder(helper);
			break;
		case PatternsConstants.SYNTACTIC:
		
			relationalPatternsGenerator = new SyntacticRelationalPatternsModelBuilder(helper);
			break;
		default:
			throw new EpnoiResourceAccessException(
					"Unknown RelationalPatternGenerator for type " + type);
		}
		return relationalPatternsGenerator;
	}
}
