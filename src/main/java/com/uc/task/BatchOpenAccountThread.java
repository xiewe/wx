package com.uc.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchOpenAccountThread implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(BatchOpenAccountThread.class);

    @Override
    public void run() {
        try {
            doWork();
        } catch (Throwable e) {
            logger.debug("Exception:" + e.getMessage());
        }

    }

    private void doWork() throws InterruptedException {

    }
}
