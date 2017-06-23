package com.framework.service;

import java.util.Map;

import com.framework.log4jdbc.LogLevel;

public interface Log4JDBCService {
	void log(String message, LogLevel logLevel, String category);

	void log(String message, Object[] objects, LogLevel logLevel, String category);

	/**
	 * 
	 * 得到全局日志等级
	 * 
	 * @return
	 */
	LogLevel getRootLogLevel();

	/**
	 * 
	 * 得到自定义包的日志等级
	 * 
	 * @return
	 */
	Map<String, LogLevel> getCustomLogLevel();
}
