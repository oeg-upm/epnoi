package org.epnoi.comparator.tasks;

import org.epnoi.comparator.helper.ComparatorHelper;
import org.epnoi.model.domain.Analysis;
import org.epnoi.model.domain.Relation;

/**
 * Created by cbadenes on 13/01/16.
 */
public class PartSimilarityTask extends SimilarityTask {


    public PartSimilarityTask(Analysis analysis, ComparatorHelper helper){
        super(analysis,
                helper,
                Relation.Type.PART_DEALS_WITH_TOPIC,
                Relation.Type.PART_SIMILAR_TO_PART);
    }

}
