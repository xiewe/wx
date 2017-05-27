package com.framework.spring;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.framework.AppConstants;
import com.framework.log4jdbc.Log;
import com.framework.log4jdbc.LogLevel;
import com.framework.log4jdbc.LogMessageObject;
import com.framework.service.Log4JDBCService;
import com.framework.utils.Exceptions;

public class Log4JDBCInterceptor extends HandlerInterceptorAdapter {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(Log4JDBCInterceptor.class);

	private Log4JDBCService log4JDBCService;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		if (!(handler instanceof HandlerMethod)) {
			return;
		}

		final HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();

		final Log log = method.getAnnotation(Log.class);
		if (log != null) {
			// 得到LogMessageObject
			final LogMessageObject logMessageObject = (LogMessageObject) request
					.getAttribute(AppConstants.LOG_ARGUMENTS);

			if (null != logMessageObject) {
				// 另起线程异步操作
				AppConstants.EXECUTOR_THREAD_POOL.submit(new Runnable() {
					@Override
					public void run() {
						try {
							LogLevel lastLogLevel = log4JDBCService
									.getRootLogLevel();

							// 先对自定义包等级做判断
							Map<String, LogLevel> customLogLevel = log4JDBCService
									.getCustomLogLevel();
							if (!customLogLevel.isEmpty()) {
								Class<?> clazz = handlerMethod.getBean()
										.getClass();
								String packageName = clazz.getPackage()
										.getName();

								Set<String> keys = customLogLevel.keySet();
								for (String key : keys) {
									if (packageName.startsWith(key)) {
										lastLogLevel = customLogLevel.get(key);
										break;
									}
								}
							}

							LogMessageObject defaultLogMessageObject = logMessageObject;

							if (defaultLogMessageObject.isWritten()) { // 判断是否写入log
								// 覆盖，直接写入日志
								if (log.override()) {
									log4JDBCService.log(log.message(),
											defaultLogMessageObject
													.getObjects(), log.level());
								} else {
									// 不覆盖，参考方法的日志等级是否大于等于最终的日志等级
									if (!log.override()
											&& log.level().compareTo(
													lastLogLevel) >= 0) {
										log4JDBCService.log(log.message(),
												defaultLogMessageObject
														.getObjects(), log
														.level());
									}
								}
							}
						} catch (Exception e) {
							LOGGER.error(Exceptions.getStackTraceAsString(e));
						}
					}
				});
			}
		}

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

	public void setLog4JDBCService(Log4JDBCService log4JDBCService) {
		this.log4JDBCService = log4JDBCService;
	}
}
