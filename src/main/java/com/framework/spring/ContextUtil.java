package com.framework.spring;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.framework.concurrency.ExecutorServiceManage;

public class ContextUtil extends ContextLoaderListener {
	private final static Logger logger = LoggerFactory
			.getLogger(ContextUtil.class);
	/**
	 * spring上下文环境
	 */
	private static ApplicationContext applicationContext;

	/**
	 * servlet上下文环境
	 */
	private static ServletContext servletContext;

	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		servletContext = event.getServletContext();
		applicationContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContext);

	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		super.contextDestroyed(event);
		logger.debug(String
				.format("contextDestroyed -------------------> begin"));
		ExecutorServiceManage.getScheduledExecutorService().shutdown();
		ExecutorServiceManage.getExecuteService().shutdown();

		final Enumeration<Driver> drivers = DriverManager.getDrivers();
		Driver driver;
		while (drivers.hasMoreElements()) {
			driver = drivers.nextElement();
			try {
				DriverManager.deregisterDriver(driver);
				logger.debug(String.format(
						"Deregister JDBC driver %s successful", driver));
			} catch (SQLException e) {
				logger.warn(String.format("Deregister JDBC driver %s error",
						driver), e);
			}
		}
		logger.debug(String.format("contextDestroyed -------------------> end"));
	}

	/**
	 * @return the applicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * @param applicationContext
	 *            the applicationContext to set
	 */
	public static void setApplicationContext(
			ApplicationContext applicationContext) {
		ContextUtil.applicationContext = applicationContext;
	}

	/**
	 * @return the servletContext
	 */
	public static ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * @param servletContext
	 *            the servletContext to set
	 */
	public static void setServletContext(ServletContext servletContext) {
		ContextUtil.servletContext = servletContext;
	}

	public static Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}
}
