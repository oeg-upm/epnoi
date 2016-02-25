package oeg.epnoi.parser.serializer.threadpool;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by cbadenes on 24/02/16.
 */
public class MyMonitorThread implements Runnable
{
    ThreadPoolExecutor executor;

    public MyMonitorThread(ThreadPoolExecutor executor)
    {
        this.executor = executor;
    }

    @Override
    public void run()
    {
        try
        {
            do
            {
                System.out.println(
                        String.format("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
                                this.executor.getPoolSize(),
                                this.executor.getCorePoolSize(),
                                this.executor.getActiveCount(),
                                this.executor.getCompletedTaskCount(),
                                this.executor.getTaskCount(),
                                this.executor.isShutdown(),
                                this.executor.isTerminated()));
                Thread.sleep(3000);
            }
            while (true);
        }
        catch (Exception e)
        {
        }
    }
}