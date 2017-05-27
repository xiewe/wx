package com.framework.controller;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.framework.AppConstants;
import com.framework.entity.User;
import com.framework.exception.ServiceException;
import com.framework.log4jdbc.Log;
import com.framework.log4jdbc.LogLevel;
import com.framework.log4jdbc.LogMessageObject;
import com.framework.service.OrganizationService;
import com.framework.service.UserService;

@Controller
@RequestMapping("/index")
public class IndexController {

	@Autowired
	private UserService userService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private HttpServletRequest request;

	private static final String INDEX = "index/index";
	private static final String UPDATE_PASSWORD = "index/updatePwd";
	private static final String UPDATE_BASE = "index/updateBase";

	@RequiresUser
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(ServletRequest request, Map<String, Object> map) {
		User loginUser = (User) org.apache.shiro.SecurityUtils.getSubject()
				.getPrincipal();

		map.put(AppConstants.LOGIN_USER, userService.get(loginUser.getId()));
		return INDEX;
	}

	@RequiresUser
	@RequestMapping(value = "/updatePwd", method = RequestMethod.GET)
	public String preUpdatePassword() {
		return UPDATE_PASSWORD;
	}

	@Log(message = "{0}用户修改密码成功！", level = LogLevel.DEBUG)
	@RequiresUser
	@RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
	public @ResponseBody String updatePassword(ServletRequest request,
			String plainPassword, String newPassword, String rPassword) {
		User loginUser = (User) org.apache.shiro.SecurityUtils.getSubject()
				.getPrincipal();

		if (newPassword != null && newPassword.equals(rPassword)) {
			loginUser.setPlainPassword(plainPassword);
			try {
				userService.updatePwd(loginUser, newPassword);
			} catch (ServiceException e) {
				return "error";
			}
			request.setAttribute(
					AppConstants.LOG_ARGUMENTS,
					LogMessageObject.newWrite().setObjects(
							new Object[] { loginUser.getUsername() }));
			return "success";
		}

		return "failed";
	}

	@RequiresUser
	@RequestMapping(value = "/updateBase", method = RequestMethod.GET)
	public String preUpdateBase(Map<String, Object> map) {
		map.put(AppConstants.LOGIN_USER, ((User) org.apache.shiro.SecurityUtils
				.getSubject().getPrincipal()));
		return UPDATE_BASE;
	}

	@Log(message = "{0}修改详细信息成功！", level = LogLevel.DEBUG)
	@RequiresUser
	@RequestMapping(value = "/updateBase", method = RequestMethod.POST)
	public @ResponseBody String updateBase(User user, ServletRequest request) {
		User loginUser = (User) org.apache.shiro.SecurityUtils.getSubject()
				.getPrincipal();

		loginUser.setPhone(user.getPhone());
		loginUser.setEmail(user.getEmail());

		userService.saveOrUpdate(loginUser);
		request.setAttribute(AppConstants.LOG_ARGUMENTS, LogMessageObject
				.newWrite()
				.setObjects(new Object[] { loginUser.getUsername() }));
		return "success";
	}
}
