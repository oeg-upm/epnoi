package org.epnoi.storage.system;

import org.epnoi.model.domain.LinkableElement;
import org.epnoi.model.domain.resources.Resource;

import java.util.Optional;

/**
 * Created by cbadenes on 03/02/16.
 */
public interface  Repository<L extends LinkableElement,T> {

    void save(L linkableElement);

    Boolean exists(T type, String uri);

    Optional<L> read(T type, String uri);

    Iterable<L> findAll(T type);

    Iterable<L> findBy(T resultType, String field, String value);

    Iterable<L> findIn(T resultType, Resource.Type referenceType, String referenceURI);

    void deleteAll(T type);

    void delete(T type, String uri);

}
