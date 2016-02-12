package org.epnoi.storage.actions;

import org.epnoi.storage.exception.RepositoryNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by cbadenes on 12/02/16.
 */
public abstract class RepeatableActionExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(RepeatableActionExecutor.class);

    private static final Integer WAITING_TIME = 500; //msecs

    private static final Integer MAX_RETRIES = 5;

    protected void waitForRetry(Integer retries){
        try {

            Thread.sleep(Double.valueOf(Math.exp(retries)*WAITING_TIME).longValue());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected Optional<Object> performRetries(Integer retries, String id, RepeatableAction function){
        try {
            return Optional.of(function.run());
        }catch (RepositoryNotFound e){
            LOG.warn(e.getMessage());
            return Optional.empty();
        }catch (Exception e){
            if (retries >= MAX_RETRIES){
                LOG.error("Error executing "+id+" after " + MAX_RETRIES + " retries",e);
                return Optional.empty();
            }
            else{
                waitForRetry(retries);
                return performRetries(++retries,id,function);
            }
        }
    }
}
