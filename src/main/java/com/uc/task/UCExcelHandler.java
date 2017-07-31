package com.uc.task;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UCExcelHandler {
    private final static Logger logger = LoggerFactory.getLogger(UCExcelHandler.class);
    private final static UCExcelHandler instance = new UCExcelHandler();
    private ScheduledExecutorService exec;

    private Map<String, ExcelType> fileMap = new ConcurrentHashMap<String, ExcelType>();
    private static long expirePeriod = 86400000;// 24 * 60 * 60 * 1000; // 1 day

    private UCExcelHandler() {
    }

    public static UCExcelHandler getInstance() {
        return instance;
    }

    public void addIMSIFile(String filename) {
        ExcelType et = new ExcelType();
        et.filename = filename;
        et.filetype = ExcelType.FILE_IMSI;
        et.uploadProgress = 0;
        et.createTime = new Date();

        fileMap.put(filename, et);
    }
    
    public void addOpenAccountFile(String filename) {
        ExcelType et = new ExcelType();
        et.filename = filename;
        et.filetype = ExcelType.FILE_OPENACCOUNT;
        et.uploadProgress = 0;
        et.createTime = new Date();

        fileMap.put(filename, et);
    }
    
    public void updateProgress(String filename, int progress) {
        fileMap.get(filename).uploadProgress = progress;
    }
    
    public int getProgress(String filename) {
        return fileMap.get(filename).uploadProgress;
    }

    class BatchIMSIThread implements Runnable {

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

    class BatchOpenAccountThread implements Runnable {

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

    class CleanupThread implements Runnable {

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

    @PostConstruct
    private void init() {
        logger.debug("UCExcelHandler init method");

        exec = Executors.newScheduledThreadPool(3);
        exec.scheduleAtFixedRate(new BatchIMSIThread(), 0, 3, TimeUnit.SECONDS);
        exec.scheduleAtFixedRate(new BatchOpenAccountThread(), 0, 3, TimeUnit.SECONDS);
        exec.scheduleAtFixedRate(new CleanupThread(), 0, 600, TimeUnit.SECONDS);
    }

    @PreDestroy
    private void destory() {
        logger.debug("UCExcelHandler destory method");
        exec.shutdown();
    }

    class ExcelType {
        public static final int FILE_IMSI = 1;
        public static final int FILE_OPENACCOUNT = 2;
        public static final int PARSE_NOTYET_START = 1;
        public static final int PARSE_STARTED = 2;
        public static final int PARSE_FINISHED = 3;

        String filename;
        int filetype;
        int uploadProgress;
        int parseStatus;
        Date createTime;
    }
}
