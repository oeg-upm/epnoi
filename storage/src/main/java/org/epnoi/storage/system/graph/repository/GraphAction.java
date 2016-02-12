package org.epnoi.storage.system.graph.repository;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by cbadenes on 12/02/16.
 */
@FunctionalInterface
public interface GraphAction<T> {

    public abstract T run() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
