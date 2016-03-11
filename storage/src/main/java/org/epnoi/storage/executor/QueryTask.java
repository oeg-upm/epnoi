package org.epnoi.storage.executor;

import lombok.Getter;

/**
 * Created by cbadenes on 09/03/16.
 */
public class QueryTask implements Runnable {


    private final Runnable task;

    @Getter
    private final Integer priority;

    @Getter
    private final Long delay;

    public QueryTask(Runnable task, Integer priority, Long delay){
        this.task = task;
        this.priority = priority;
        this.delay = delay;
    }


    @Override
    public void run() {
        this.task.run();
    }
}
