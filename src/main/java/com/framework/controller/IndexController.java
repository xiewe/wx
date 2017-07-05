package com.framework.controller;

import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.framework.AppConstants;
import com.framework.log4jdbc.Log;
import com.framework.log4jdbc.LogLevel;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController {

	private static final String INDEX = "index";
	private static final String UPDATE_PASSWORD = "updatePwd";

	@RequiresUser
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ServletRequest request, Map<String, Object> map) {

		map.put(AppConstants.LOGIN_USER, SecurityUtils.getSubject().getPrincipal());
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
	public @ResponseBody String updatePassword(ServletRequest request, String plainPassword, String newPassword,
	        String rPassword) {

		return "failed";
	}

}
