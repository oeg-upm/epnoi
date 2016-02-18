package org.epnoi.learner.relations.patterns;

import java.io.Serializable;


public interface RelationalPatternsModel extends Serializable  {
	double calculatePatternProbability(RelationalPattern relationalPattern);
	void show();
}
