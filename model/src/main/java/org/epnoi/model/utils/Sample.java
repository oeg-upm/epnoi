package org.epnoi.model.utils;

import java.util.Optional;

/**
 * Created by cbadenes on 18/02/16.
 */
public class Sample {

    public <T> Optional<T> read() throws InstantiationException, IllegalAccessException {
        return getValue((Class<T>) String.class);
    }

    public <T> Optional<T> getValue(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        return Optional.of(clazz.newInstance());
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        Sample sample = new Sample();

        Optional<Object> result = sample.read();
    }
}
