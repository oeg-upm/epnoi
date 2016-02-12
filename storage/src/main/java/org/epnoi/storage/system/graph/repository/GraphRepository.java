package org.epnoi.storage.system.graph.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by cbadenes on 12/02/16.
 */
public abstract class GraphRepository {

    private static final Logger LOG = LoggerFactory.getLogger(GraphRepository.class);

    private static final Integer WAITING_TIME = 200; //msecs

    private static final Integer MAX_RETRIES = 5;

    protected void waitForRetry(){
        try {
            Thread.sleep(WAITING_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected Optional<Object> performRetries(Integer retries, String id, GraphAction function){
        try{
            System.out.println("retry: " + retries);
            return Optional.of(function.run());
        }catch (Exception e){
            if (retries >= MAX_RETRIES){
                LOG.error("Error executing a "+id+" after " + MAX_RETRIES + " retries",e);
                return Optional.empty();
            }
            else{
                waitForRetry();
                return performRetries(++retries,id,function);
            }
        }
    }
}
