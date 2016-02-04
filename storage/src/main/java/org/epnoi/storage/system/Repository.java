package org.epnoi.storage.system;

import org.epnoi.model.domain.Resource;

import java.util.Optional;

/**
 * Created by cbadenes on 03/02/16.
 */
public interface Repository {

    void save(Resource resource, Resource.Type type);

    Boolean exists(String uri, Resource.Type type);

    Optional<Resource> read(String uri, Resource.Type type);

    Iterable<Resource> findAll(Resource.Type type);

    Iterable<Resource> findBy(Resource.Type resultType,String field, String value);

    Iterable<Resource> findIn(Resource.Type resultType,Resource.Type referenceType,String referenceURI);

    void deleteAll(Resource.Type type);

    void delete(String uri, Resource.Type type);


}
