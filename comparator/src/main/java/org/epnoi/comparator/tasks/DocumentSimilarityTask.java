package org.epnoi.comparator.tasks;

import org.epnoi.comparator.helper.ComparatorHelper;
import org.epnoi.model.domain.resources.Analysis;
import org.epnoi.model.domain.relations.Relation;

/**
 * Created by cbadenes on 13/01/16.
 */
public class DocumentSimilarityTask extends SimilarityTask {

    public DocumentSimilarityTask(Analysis analysis, ComparatorHelper helper){
        super(analysis,
                helper,
                Relation.Type.DEALS_WITH_FROM_DOCUMENT,
                Relation.Type.SIMILAR_TO_DOCUMENTS);
    }



}
