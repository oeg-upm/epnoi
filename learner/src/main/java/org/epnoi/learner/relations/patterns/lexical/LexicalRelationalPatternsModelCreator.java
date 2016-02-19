package org.epnoi.learner.relations.patterns.lexical;

import org.epnoi.learner.relations.patterns.PatternsConstants;
import org.epnoi.learner.relations.patterns.RelationalPatternsModelCreator;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 19/02/16.
 */
@Component("lexicalPatternsModelCreator")
public class LexicalRelationalPatternsModelCreator extends RelationalPatternsModelCreator {

    public LexicalRelationalPatternsModelCreator() {
        super(PatternsConstants.LEXICAL);
    }
}
