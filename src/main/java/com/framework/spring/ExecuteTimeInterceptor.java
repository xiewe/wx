package com.framework.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ExecuteTimeInterceptor extends HandlerInterceptorAdapter {
	private final static Logger logger = LoggerFactory
			.getLogger(ExecuteTimeInterceptor.class);

	/**
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		long startTime = System.currentTimeMillis();
		request.setAttribute("startTime", startTime);

		return true;
	}

	/**
	 * @param request
	 * @param response
	 * @param handler
	 * @param modelAndView
	 * @throws Exception
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		long startTime = (Long) request.getAttribute("startTime");

		long endTime = System.currentTimeMillis();

		long executeTime = endTime - startTime;

		// modified the exisitng modelAndView
		if (modelAndView != null) {
			modelAndView.addObject("executeTime", executeTime);
		}

		// log it
		if (logger.isDebugEnabled()) {
			logger.debug("--------------[" + handler
					+ "]-------------- executeTime : " + executeTime + "ms");
		}
	}

}
