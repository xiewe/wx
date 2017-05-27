package com.framework.controller;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.framework.AppConstants;
import com.framework.log4jdbc.LogMessageObject;
import com.framework.service.RedisService;

/**
 * Controller抽象类，继承此类都要遵守约定以下方法命名规则:
 * 分页查询以list开头、新增以create开头、修改以update、删除以delete开头、单个记录查询以view开头
 * 按此约定系统会在每个方法调用时注入数据级权限控制，否则会导致权限访问失败问题。
 *
 * @author dengyong
 *
 */
public abstract class BaseController {
	private final static Logger logger = LoggerFactory
			.getLogger(BaseController.class);

	@Autowired
	public HttpServletRequest request;

	@Autowired
	public HttpServletResponse response;

	@Autowired
	public RedisService redisService;

	@Autowired
	public ResourceBundleMessageSource resourceBundleMessageSource;

	@Autowired
	public LocaleResolver localeResolver;

	@ModelAttribute
	public void initPath(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String base = request.getContextPath();

		String fullPath = request.getScheme() + "://" + request.getServerName()
				+ base;
		model.addAttribute("base", base);
		model.addAttribute("fullPath", fullPath);

	}

	protected String getMessage(String code) {
		return this.getMessage(code, null);
	}

	protected String getMessage(String code, Object[] args) {
		// HttpServletRequest request =
		// ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LocaleResolver localeResolver = RequestContextUtils
				.getLocaleResolver(request);
		Locale locale = localeResolver.resolveLocale(request);
		return resourceBundleMessageSource.getMessage(code, args, locale);
	}

	protected String getMessage(String code, Object[] args, Locale locale) {
		try {
			return resourceBundleMessageSource.getMessage(code, args, locale);
		} catch (NoSuchMessageException e) {
			e.printStackTrace();
			logger.error("", e);
			return code;
		}
	}

	@JsonFilter("objectFilter")
	public interface ObjectFilterMixIn {
	}

	/**
	 * 动态过滤JSON
	 * 
	 * @param filterName
	 * @param isOutAllExcept
	 *            true:想要的字段,false 不想要的字段
	 * @param propertyes
	 * @return
	 */
	public FilterProvider filter(String filterName, boolean isOutAllExcept,
			String... propertyes) {
		if (isOutAllExcept) {
			// 过滤想要的
			FilterProvider filter = new SimpleFilterProvider().addFilter(
					filterName,
					SimpleBeanPropertyFilter.filterOutAllExcept(propertyes));
			return filter;
		} else {
			// 过滤不想要的
			FilterProvider filter = new SimpleFilterProvider().addFilter(
					filterName,
					SimpleBeanPropertyFilter.serializeAllExcept(propertyes));
			return filter;
		}
	}

	// @ModelAttribute
	public void handerCookie() {
		Cookie[] cookies = request.getCookies();
		String uid = "";
		String token = "";
		String lang = "";
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookie.getName().equals("uid")) {
				uid = cookie.getValue();
				request.setAttribute("uid", uid);
			} else if (cookie.getName().equals("token")) {
				token = cookie.getValue();
				request.setAttribute("token", token);
			} else if (cookie.getName().equals("lang")) {
				lang = cookie.getValue();
				request.setAttribute("language", lang);
			}
			logger.debug("key:" + cookie.getName() + ", value:"
					+ cookie.getValue());
		}
		// if (!redisService.checkToken(uid, token)) {
		//
		// logger.debug(String.format("Invalid authorization, uid is: %s, token:
		// %s", uid,token));
		// }

		String[] language = lang.split("_");
		if (null != language && language.length >= 2) {
			Locale locale = new Locale(language[0], language[1]);
			localeResolver.setLocale(request, response, locale);
		}
	}

	public void setLogObject(Object o) {
		request.setAttribute(AppConstants.LOG_ARGUMENTS, LogMessageObject
				.newWrite().setObjects(new Object[] { o }));
	}
}
