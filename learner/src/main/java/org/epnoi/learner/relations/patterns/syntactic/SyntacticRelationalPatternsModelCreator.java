package org.epnoi.learner.relations.patterns.syntactic;

import org.epnoi.learner.relations.patterns.PatternsConstants;
import org.epnoi.learner.relations.patterns.RelationalPatternsModelCreator;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 19/02/16.
 */
@Component("syntacticPatternsModelCreator")
public class SyntacticRelationalPatternsModelCreator extends RelationalPatternsModelCreator {

    public SyntacticRelationalPatternsModelCreator() {
        super(PatternsConstants.SYNTACTIC);
    }
}
