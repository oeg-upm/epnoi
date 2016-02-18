package org.epnoi.comparator.tasks;

import org.epnoi.comparator.helper.ComparatorHelper;
import org.epnoi.model.domain.resources.Analysis;
import org.epnoi.model.domain.relations.Relation;

/**
 * Created by cbadenes on 13/01/16.
 */
public class PartSimilarityTask extends SimilarityTask {


    public PartSimilarityTask(Analysis analysis, ComparatorHelper helper){
        super(analysis,
                helper,
                Relation.Type.DEALS_WITH_FROM_PART,
                Relation.Type.SIMILAR_TO_PARTS);
    }

}
