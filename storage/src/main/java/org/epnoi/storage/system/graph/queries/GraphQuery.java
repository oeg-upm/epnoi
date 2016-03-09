package org.epnoi.storage.system.graph.queries;

import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;

import java.util.List;

/**
 * Created by cbadenes on 28/02/16.
 */
public interface GraphQuery<T extends Relation> {

    Relation.Type accept();

    List<T> query(String startUri, String endUri);

    List<T> inDomain(String uri);

    void deleteIn(Resource.Type type, String uri);

    List<String> findAll();

    void save(Relation relation);

}
