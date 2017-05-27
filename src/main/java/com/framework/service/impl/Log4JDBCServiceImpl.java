package com.framework.service.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.framework.entity.SysLog;
import com.framework.entity.User;
import com.framework.log4jdbc.LogLevel;
import com.framework.service.Log4JDBCService;
import com.framework.service.SysLogService;

/**
 * 全局日志等级<包日志等级<类和方法日志等级
 */
public class Log4JDBCServiceImpl implements Log4JDBCService {

	private LogLevel rootLogLevel = LogLevel.ERROR;

	private SysLogService sysLogService;

	private Map<String, LogLevel> customLogLevel = new HashMap<String, LogLevel>();

	/**
	 * 
	 * @param message
	 * @param objects
	 * @param logLevel
	 */
	@Override
	public void log(String message, Object[] objects, LogLevel logLevel) {

		MessageFormat mFormat = new MessageFormat(message);
		String result = mFormat.format(objects);

		if (!StringUtils.isNotBlank(result)) {
			return;
		}

		User loginUser = (User) org.apache.shiro.SecurityUtils.getSubject()
				.getPrincipal();

		SysLog sysLog = new SysLog();
		sysLog.setUsername(loginUser.getUsername());
		sysLog.setIp(loginUser.getLoginIpAddress());
		sysLog.setUserAgent(null);
		sysLog.setMessage(result);
		sysLog.setCreateTime(new Date());

		sysLogService.saveOrUpdate(sysLog);
	}

	public void setRootLogLevel(LogLevel rootLogLevel) {
		this.rootLogLevel = rootLogLevel;
	}

	@Override
	public LogLevel getRootLogLevel() {
		return rootLogLevel;
	}

	public void setCustomLogLevel(Map<String, LogLevel> customLogLevel) {
		this.customLogLevel = customLogLevel;
	}

	@Override
	public Map<String, LogLevel> getCustomLogLevel() {
		return customLogLevel;
	}

	public void setSysLogService(SysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}

	@Override
	public void log(String message, LogLevel logLevel) {
		log(message, null, logLevel);
	}

}
