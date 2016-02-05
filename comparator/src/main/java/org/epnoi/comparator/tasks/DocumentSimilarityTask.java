package org.epnoi.comparator.tasks;

import org.epnoi.comparator.helper.ComparatorHelper;
import org.epnoi.comparator.model.WeightedPair;
import org.epnoi.model.domain.Analysis;
import org.epnoi.model.domain.Relation;
import org.epnoi.model.domain.RelationProperties;
import org.epnoi.model.domain.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by cbadenes on 13/01/16.
 */
public class DocumentSimilarityTask extends SimilarityTask {

    public DocumentSimilarityTask(Analysis analysis, ComparatorHelper helper){
        super(analysis,
                helper,
                Relation.Type.DOCUMENT_DEALS_WITH_TOPIC,
                Relation.Type.DOCUMENT_SIMILAR_TO_DOCUMENT);
    }



}
