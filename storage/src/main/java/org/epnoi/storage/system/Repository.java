package org.epnoi.storage.system;

import org.epnoi.model.domain.LinkableElement;
import org.epnoi.model.domain.Resource;

import java.util.Optional;

/**
 * Created by cbadenes on 03/02/16.
 */
public interface  Repository<R extends LinkableElement,T> {

    void save(R resource, T type);

    Boolean exists(T type, String uri);

    Optional<R> read(T type, String uri);

    Iterable<R> findAll(T type);

    Iterable<R> findBy(T resultType,String field, String value);

    Iterable<R> findIn(T resultType,Resource.Type referenceType,String referenceURI);

    void deleteAll(T type);

    void delete(T type, String uri);

}
