package com.framework.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ExecutorServiceManage {
	/**
	 * 线程池大小：线程数=CPU可用核心数/(1-阻塞系数)
	 * 阻塞系数(值越大，线程数目越小)的取值在0-1之间，根据应用实际场景来设置：在计算密集型的应用中，线程阻塞时间相对较少；
	 * IO密集型的应用，任务处理阻塞时间较长，线程池需要创建比处理器核心数大几倍数量的线程。
	 */
	private static int poolSize = (int) (Runtime.getRuntime().availableProcessors() / 0.1);
	private static ExecutorService executeService = Executors.newFixedThreadPool(poolSize);

	private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

	public static ExecutorService getExecuteService() {
		return executeService;
	}

	public static void execute(Runnable runnable) {
		executeService.execute(runnable);
	}

	public static ScheduledExecutorService getScheduledExecutorService() {
		return scheduledExecutorService;
	}

	public static void setScheduledExecutorService(ScheduledExecutorService scheduledExecutorService) {
		ExecutorServiceManage.scheduledExecutorService = scheduledExecutorService;
	}

    public void shutDown(){
        executeService.shutdown();
    }
}
