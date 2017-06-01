package com.framework.controller;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.framework.entity.User;
import com.framework.log4jdbc.Log;
import com.framework.log4jdbc.LogLevel;
import com.framework.service.SysUserService;
import com.framework.utils.page.Page;

@Controller
@RequestMapping("/security/user")
public class UserController extends BaseController {
	@Autowired
	private SysUserService sysUserService;

	private static final String CREATE = "security/user/create";
	private static final String UPDATE = "security/user/update";
	private static final String LIST = "security/user/list";

	@RequiresPermissions("User:save")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String preCreate() {
		return CREATE;
	}

	@Log(message = "添加了{0}用户。", level = LogLevel.INFO)
	@RequiresPermissions("User:save")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody String create(@Valid User user) {

		return "success";
	}

	@RequiresPermissions(value = { "User:edit" }, logical = Logical.OR)
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String preUpdate(@PathVariable Long id, Map<String, Object> map) {
		return UPDATE;
	}

	@Log(message = "修改了{0}用户的信息。", level = LogLevel.INFO)
	@RequiresPermissions(value = { "User:edit" }, logical = Logical.OR)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody String update(
			@Valid @ModelAttribute("preloadUser") User user) {

		return "msg.operation.success";
	}

	@Log(message = "删除了{0}用户。", level = LogLevel.INFO)
	@RequiresPermissions(value = { "User:delete" }, logical = Logical.OR)
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public @ResponseBody String delete(@PathVariable Long id) {
		return "删除用户成功！";
	}

	@Log(message = "删除了{0}用户。", level = LogLevel.INFO)
	@RequiresPermissions(value = { "User:delete" }, logical = Logical.OR)
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody String deleteMany(Long[] ids) {
		return "删除用户成功！";
	}

	@RequiresPermissions(value = { "User:view" }, logical = Logical.OR)
	@RequestMapping(value = "/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String list(ServletRequest request, Page page,
			Map<String, Object> map) {
		return LIST;
	}

	@Log(message = "{0}用户{1}", level = LogLevel.INFO)
	@RequiresPermissions(value = { "User:reset" }, logical = Logical.OR)
	@RequestMapping(value = "/reset/{type}/{userId}", method = RequestMethod.POST)
	public @ResponseBody String reset(@PathVariable String type,
			@PathVariable Long userId) {
		return "";
	}

}
