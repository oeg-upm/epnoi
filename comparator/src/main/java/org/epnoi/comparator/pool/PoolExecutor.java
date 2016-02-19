package org.epnoi.comparator.pool;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by cbadenes on 19/02/16.
 */
@Component
public class PoolExecutor {

    private ThreadPoolTaskExecutor pool;

    @PostConstruct
    public void setup(){
        this.pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(5);
        pool.setMaxPoolSize(10);
        pool.setKeepAliveSeconds(10);
        pool.initialize();
    }


    public void execute(Runnable task){
        this.pool.execute(task);
    }

}
