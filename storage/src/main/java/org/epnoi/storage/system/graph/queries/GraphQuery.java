package org.epnoi.storage.system.graph.queries;

import org.epnoi.model.domain.relations.Relation;

import java.util.List;

/**
 * Created by cbadenes on 28/02/16.
 */
public interface GraphQuery<T extends Relation> {

    Relation.Type accept();

    List<T> execute(String startUri, String endUri);

}
