package com.framework.aop;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.framework.service.RedisService;


public class H5AuthInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(H5AuthInterceptor.class);
	
	private static final String REDIRECT_URL = "/cwbizchat/w/error/notauth.html";

	@Autowired
	private RedisService redisService;

	@Autowired
	public LocaleResolver localeResolver;

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@SuppressWarnings("unused")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String uid = "";
		String token = "";
		String lang = "";

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				//logger.debug("Cookie name:" + cookies[i].getName() + ", value:" + cookies[i].getValue());
				// logger.debug("Cookie value:" + cookies[i].getValue());
				// logger.debug("Cookie maxAge:" + cookies[i].getMaxAge());

				if ("uid".equals(cookies[i].getName())) {
					uid = cookies[i].getValue();
					request.setAttribute("uid", uid);
				} else if ("token".equals(cookies[i].getName())) {
					token = cookies[i].getValue();
					request.setAttribute("token", token);
				} else if ("lang".equals(cookies[i].getName())) {
					lang = cookies[i].getValue();
					request.setAttribute("lang", lang);
				}
			}
		} else {
			logger.debug("No cookie.");
		}

//		// check Auth
//		if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(token)) {
//
//			// for test begin
//			 uid = "861434305";
//			 token = "12345678";
//			 lang = "zh";
//			 Cookie cookie1 = new Cookie("uid", uid);
//			 cookie1.setPath("/");
//			 response.addCookie(cookie1);
//			 Cookie cookie2 = new Cookie("token", token);
//			 cookie2.setPath("/");
//			 response.addCookie(cookie2);
//			 Cookie cookie3 = new Cookie("lang", lang);
//			 cookie3.setPath("/");
//			 response.addCookie(cookie3);
//			// for test end
//			//
////			response.sendRedirect(REDIRECT_URL);
////			return false;
//		}

		if (!redisService.checkToken(uid, token)) {
//		if (false) {
			response.sendRedirect(REDIRECT_URL);
			return false;
		}

		String[] language = lang.split("_");
		if (null != language && language.length >= 2) {
			Locale locale = new Locale(language[0], language[1]);
			localeResolver.setLocale(request, response, locale);
		}

		return true;
	}

}
