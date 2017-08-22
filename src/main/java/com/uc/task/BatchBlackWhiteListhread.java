package com.uc.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchBlackWhiteListhread implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(BatchBlackWhiteListhread.class);

    private String filename;

    public BatchBlackWhiteListhread(String filename) {
        super();
        this.filename = filename;
    }

    @Override
    public void run() {
        try {
            doWork();
        } catch (Throwable e) {
            logger.debug("Exception:" + e.getMessage());
        }

    }

    private void doWork() throws InterruptedException {
        UCExcelHandler.getInstance().addBlackWhiteListFile(filename);
        String logfilename = filename + ".log";

        int i = 0;
        FileWriter logout = null;
        try {
            logout = new FileWriter(new File(logfilename));

            while (i <= 100) {
                i++;
                UCExcelHandler.getInstance().setPraseProgress(filename, i);
                logout.write("Success:" + i);

                Thread.sleep(1000);
            }

            logout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (logout != null) {
                    logout.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
