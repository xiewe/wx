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

import com.framework.AppConstants;
import com.framework.log4jdbc.LogMessageObject;
import com.framework.service.RedisService;

public abstract class BaseController {
	private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

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
	public void initPath(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String base = request.getContextPath();

		String fullPath = request.getScheme() + "://" + request.getServerName() + base;
		model.addAttribute("base", base);
		model.addAttribute("fullPath", fullPath);

	}

	protected String getMessage(String code) {
		return this.getMessage(code, null);
	}

	protected String getMessage(String code, Object[] args) {
		// HttpServletRequest request =
		// ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
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
			logger.debug("key:" + cookie.getName() + ", value:" + cookie.getValue());
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

	public void setLogObject(Object[] o) {
		request.setAttribute(AppConstants.LOG_ARGUMENTS, LogMessageObject.newWrite().setObjects(o));
	}
}
