package com.framework.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.framework.AppConstants;
import com.framework.entity.SysUser;
import com.framework.utils.Exceptions;

public class BaseFormAuthenticationFilter extends FormAuthenticationFilter {
	private static final Logger log = LoggerFactory
			.getLogger(BaseFormAuthenticationFilter.class);

	private String captcha = AppConstants.CAPTCHA_KEY;

	public String getCaptcha() {
		return captcha;
	}

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptcha());
	}

	@Override
	protected AuthenticationToken createToken(ServletRequest request,
			ServletResponse response) {
		String username = getUsername(request);
		String password = getPassword(request);
		String captcha = getCaptcha(request);
		boolean rememberMe = isRememberMe(request);
		String host = getHost(request);
		return new CaptchaUsernamePasswordToken(username, password, rememberMe,
				host, captcha);
	}

	@Override
	protected boolean onLoginFailure(AuthenticationToken token,
			AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		if (log.isDebugEnabled()) {
			Class<?> clazz = e.getClass();
			if (clazz.equals(AuthenticationException.class)) {
				log.debug(Exceptions.getStackTraceAsString(e));
			}
		}

		return super.onLoginFailure(token, e, request, response);
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) {
		try {
			// 先判断是否是登录操作
			if (isLoginSubmission(request, response)) {
				if (log.isTraceEnabled()) {
					log.trace("Login submission detected.  Attempting to execute login.");
				}
				return false;
			}
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}

		return super.isAccessAllowed(request, response, mappedValue);
	}

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token,
			Subject subject, ServletRequest request, ServletResponse response)
			throws Exception {

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		((SysUser) subject.getPrincipal()).setIp(request.getRemoteAddr());
		((SysUser) subject.getPrincipal()).setUserAgent(httpServletRequest
				.getHeader("User-Agent"));

		if (!"XMLHttpRequest".equalsIgnoreCase(httpServletRequest
				.getHeader("X-Requested-With"))
				|| request.getParameter("ajax") == null) {// 不是ajax请求
			httpServletResponse.sendRedirect(httpServletRequest
					.getContextPath() + this.getSuccessUrl());
		} else {
			httpServletResponse.sendRedirect(httpServletRequest
					.getContextPath() + this.getSuccessUrl());
		}

		return false;
	}
}
