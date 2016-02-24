package org.epnoi.parser.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by cbadenes on 24/02/16.
 */
public class ParserExecutionHandler implements RejectedExecutionHandler {

    private static Logger LOG = LoggerFactory.getLogger(ParserExecutionHandler.class);

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        ParserTask task = (ParserTask) r;
        LOG.error("Document not processed: " + task.getFile());
    }
}
