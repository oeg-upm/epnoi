package org.epnoi.learner.model;

import org.epnoi.learner.relations.patterns.RelationalPattern;
import org.epnoi.learner.relations.patterns.RelationalPatternsModel;
import org.epnoi.learner.relations.patterns.RelationalPatternsModelSerializer;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by cbadenes on 16/02/16.
 */
@Component
public class SoftPatternModel implements RelationalPatternsModel{

    @Value("${learner.task.relations.hypernyms.lexical.path}")
    String path;

    private RelationalPatternsModel model;

    @PostConstruct
    public void setup() throws EpnoiResourceAccessException {
        this.model = RelationalPatternsModelSerializer.deserialize(path);
    }

    @Override
    public double calculatePatternProbability(RelationalPattern relationalPattern) {
        return this.model.calculatePatternProbability(relationalPattern);
    }

    @Override
    public void show() {
        this.model.show();
    }
}
