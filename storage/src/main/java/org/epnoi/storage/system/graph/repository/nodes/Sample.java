package org.epnoi.storage.system.graph.repository.nodes;

import org.apache.commons.lang.math.RandomUtils;
import org.epnoi.storage.system.graph.repository.GraphRepository;

import java.util.Optional;

/**
 * Created by cbadenes on 12/02/16.
 */
public class Sample extends GraphRepository {

    public Integer test(){
        Optional<Object> result = performRetries(4, "sample", () -> {
            if (RandomUtils.nextInt() % 2 == 0) return 1;
            throw new RuntimeException();
        });

        if (result.isPresent()){
            System.out.println("All was ok!: " + result.get());
            return (Integer) result.get();
        }else{
            System.out.println("Something was wrong!");
            return 0;
        }
    }

    public static void main(String[] args){
        Sample sample = new Sample();
        System.out.println(sample.test());
    }
}
