package com.uc.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchIMSIThread implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(BatchIMSIThread.class);

    private String filename;

    public BatchIMSIThread(String filename) {
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
        UCExcelHandler.getInstance().addIMSIFile(filename);
        String logfilename = filename + ".log";

        int i = 1;
        FileWriter logout = null;
        try {
            logout = new FileWriter(new File(logfilename));

            // 读取excel，得到行数，每行调用相应的hessian call
            while (i <= 100) {
                // 更新进度
                UCExcelHandler.getInstance().setPraseProgress(filename, i);

                // 处理的日志
                String log = "Success:" + i;

                // 写日志到文件
                logout.write(log + "\n");
                // 写日志到缓存
                UCExcelHandler.getInstance().setParseLog(filename, log);

                i++;
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
