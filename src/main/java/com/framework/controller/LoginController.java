package com.framework.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;

@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	private LocaleResolver localeResolver;
	private static final String LOGIN_PAGE = "login";

	@RequestMapping(method = RequestMethod.GET)
	public String login() {
		return LOGIN_PAGE;
	}

	@RequestMapping("/changeLocale/{language}")
	public String changeLocale(@PathVariable String language, HttpServletRequest request, HttpServletResponse response) {
		String[] lang = language.split("_");
		if (null != lang && lang.length >= 2) {
			Locale locale = new Locale(lang[0], lang[1]);
			// LocaleResolver localeResolver = RequestContextUtils
			// .getLocaleResolver(request);
			localeResolver.setLocale(request, response, locale);
		}
		return LOGIN_PAGE;
	}
}
