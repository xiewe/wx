package com.framework.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.framework.AppConstants;
import com.framework.entity.Permission;
import com.framework.entity.User;
import com.framework.exception.ServiceException;
import com.framework.log4jdbc.Log;
import com.framework.log4jdbc.LogLevel;
import com.framework.log4jdbc.LogMessageObject;
import com.framework.service.OrganizationService;
import com.framework.service.PermissionService;
import com.framework.service.UserService;

@Controller
@RequestMapping("/index")
public class IndexController {

	@Autowired
	private UserService userService;

	@Autowired
	private PermissionService permissionService;

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
		Permission menuPermission = getMenuPermission(SecurityUtils
				.getSubject());
		map.put("menuModule", menuPermission);
		return INDEX;
	}

	private Permission getMenuPermission(Subject subject) {
		Permission rootModule = permissionService.getTree();
		check(rootModule, subject);
		removeEmptyNode(rootModule);
		return rootModule;
	}

	/**
	 * 检查用户拥有的操作权限，返回菜单树对象
	 * 
	 * @param permission
	 * @param subject
	 */
	private void check(Permission permission, Subject subject) {
		List<Permission> lstPermissions = new ArrayList<Permission>();
		if (null != permission && permission.getChildren() != null) {
			for (Permission o : permission.getChildren()) {
				// 如果拥有该角色或者拥有与此菜单相应的权限则加入此菜单
				if (subject.hasRole(o.getSn().split(":")[0])
						|| subject.isPermitted(o.getSn())) {
					check(o, subject);
					lstPermissions.add(o);
				}
			}
		}
		permission.setChildren(lstPermissions);
	}

	/**
	 * 删除那些空的节点
	 * 
	 * @param permission
	 */
	private void removeEmptyNode(Permission permission) {
		for (Permission o : permission.getChildren()) {
			// 如果节点类型是文件夹，且无子节点，则从菜单树中删除不显示
			if (o.getCategory() == AppConstants.PERMISSION_TYPE_FOLDER
					&& (o.getChildren() == null || o.getChildren().size() <= 0)) {
				permission.getChildren().remove(o);
			}
			removeEmptyNode(o);
		}
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
