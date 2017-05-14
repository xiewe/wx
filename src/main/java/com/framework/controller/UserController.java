package com.framework.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.framework.AppConstants;
import com.framework.entity.Organization;
import com.framework.entity.Role;
import com.framework.entity.User;
import com.framework.entity.UserRole;
import com.framework.exception.ExistedException;
import com.framework.exception.ServiceException;
import com.framework.log4jdbc.Log;
import com.framework.log4jdbc.LogLevel;
import com.framework.log4jdbc.LogMessageObject;
import com.framework.service.OrganizationService;
import com.framework.service.RoleService;
import com.framework.service.UserRoleService;
import com.framework.service.UserService;
import com.framework.shiro.ShiroRealm;
import com.framework.utils.dwz.AjaxObject;
import com.framework.utils.dwz.Page;
import com.framework.utils.persistence.DynamicSpecifications;
import com.framework.utils.persistence.SearchFilter;
import com.framework.utils.persistence.SearchFilter.Operator;

@Controller
@RequestMapping("/security/user")
public class UserController extends BaseController {
	@Autowired
	private ShiroRealm shiroRealm;

	@Autowired
	private UserService userService;

	@Autowired
	UserRoleService userRoleService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private HttpServletRequest request;

	private static final String CREATE = "security/user/create";
	private static final String UPDATE = "security/user/update";
	private static final String LIST = "security/user/list";
	private static final String LOOK_UP_ROLE = "security/user/assign_user_role";
	private static final String LOOK_USER_ROLE = "security/user/delete_user_role";
	private static final String LOOK_ORG = "security/user/lookup_org";

	@RequiresPermissions("User:save")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String preCreate() {
		return CREATE;
	}

	@Log(message = "添加了{0}用户。", level = LogLevel.INFO)
	@RequiresPermissions("User:save")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody
	String create(@Valid User user) {
		user.setCreateTime(new Date());
		if (user.getOrganization().getId() == null) {
			user.setOrganization(null);
		}
		try {
			userService.saveOrUpdate(user);
		} catch (ExistedException e) {
			return AjaxObject.ajaxDoneError("添加用户失败：" + e.getMessage())
					.setCallbackType("").toString();
		}
		request.setAttribute(AppConstants.LOG_ARGUMENTS, LogMessageObject
				.newWrite().setObjects(new Object[] { user.getUsername() }));

		return new AjaxObject(200, getMessage("msg.operation.success"),
				"forward").setForwardUrl("security/user/list").toString();
	}

	@ModelAttribute("preloadUser")
	public User preload(@RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			User user = userService.get(id);
			user.setOrganization(null);
			return user;
		}
		return null;
	}

	@RequiresPermissions(value = { "User:edit" }, logical = Logical.OR)
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String preUpdate(@PathVariable Long id, Map<String, Object> map) {
		User user = userService.get(id);
		map.put("user", user);
		return UPDATE;
	}

	@Log(message = "修改了{0}用户的信息。", level = LogLevel.INFO)
	@RequiresPermissions(value = { "User:edit" }, logical = Logical.OR)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody
	String update(@Valid @ModelAttribute("preloadUser") User user) {
		if (user.getOrganization().getId() == null) {
			user.setOrganization(null);
		}
		userService.saveOrUpdate(user);
		// reload permission
		shiroRealm.clearAllCachedAuthorizationInfo();
		request.setAttribute(AppConstants.LOG_ARGUMENTS, LogMessageObject
				.newWrite().setObjects(new Object[] { user.getUsername() }));

		return new AjaxObject(200, getMessage("msg.operation.success"),
				AjaxObject.CALLBACK_TYPE_CLOSE_CURRENT).setNavTabId("User:*")
				.toString();
	}

	@Log(message = "删除了{0}用户。", level = LogLevel.INFO)
	@RequiresPermissions(value = { "User:delete" }, logical = Logical.OR)
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public @ResponseBody
	String delete(@PathVariable Long id) {
		User user = null;
		try {
			user = userService.get(id);
			if (user.getCategory() == AppConstants.USER_CATEGORY_SYSTEM) {
				throw new ServiceException("不能删除系统用户！");
			}
			// reload permission
			shiroRealm.clearAllCachedAuthorizationInfo();
			request.setAttribute(AppConstants.LOG_ARGUMENTS, LogMessageObject
					.newWrite().setObjects(new Object[] { user.getUsername() }));
			userService.delete(user.getId());

		} catch (ServiceException e) {
			return AjaxObject.ajaxDoneError("删除用户失败：" + e.getMessage())
					.setCallbackType("").toString();
		}
		return AjaxObject.ajaxDoneSuccess("删除用户成功！").setCallbackType("")
				.toString();
	}

	@Log(message = "删除了{0}用户。", level = LogLevel.INFO)
	@RequiresPermissions(value = { "User:delete" }, logical = Logical.OR)
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	String deleteMany(Long[] ids) {
		String[] usernames = new String[ids.length];
		try {
			for (int i = 0; i < ids.length; i++) {
				User user = userService.get(ids[i]);
				if (user.getCategory() == AppConstants.USER_CATEGORY_SYSTEM) {
					throw new ServiceException("不能删除系统用户！");
				}
				userService.delete(user.getId());
				usernames[i] = user.getUsername();
			}
			// reload permission
			shiroRealm.clearAllCachedAuthorizationInfo();
			request.setAttribute(
					AppConstants.LOG_ARGUMENTS,
					LogMessageObject.newWrite().setObjects(
							new Object[] { Arrays.toString(usernames) }));
		} catch (ServiceException e) {
			return AjaxObject.ajaxDoneError("删除用户失败：" + e.getMessage())
					.setCallbackType("").toString();
		}
		return AjaxObject.ajaxDoneSuccess("删除用户成功！").setCallbackType("")
				.toString();
	}

	@RequiresPermissions(value = { "User:view" }, logical = Logical.OR)
	@RequestMapping(value = "/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String list(ServletRequest request, Page page,
			Map<String, Object> map) {
		Specification<User> specification = DynamicSpecifications
				.buildSpecification(request, User.class,new SearchFilter(
						"category", Operator.LT, 2));
		List<User> users = userService.findByPageable(specification, page);
		map.put("page", page);
		map.put("users", users);
		return LIST;
	}

	@Log(message = "{0}用户{1}", level = LogLevel.INFO)
	@RequiresPermissions(value = { "User:reset" }, logical = Logical.OR)
	@RequestMapping(value = "/reset/{type}/{userId}", method = RequestMethod.POST)
	public @ResponseBody
	String reset(@PathVariable String type, @PathVariable Long userId) {
		User user = userService.get(userId);
		AjaxObject ajaxObject = new AjaxObject();
		ajaxObject.setCallbackType("");

		if (type.equals("password")) {
			userService.resetPwd(user, "123456");
			ajaxObject.setMessage("重置密码成功，默认为123456");
		} else if (type.equals("status")) {
			if (user.getStatus() == AppConstants.USER_STATUS_ENABLED) {
				user.setStatus(AppConstants.USER_STATUS_DISABLED);
			} else {
				user.setStatus(AppConstants.USER_STATUS_ENABLED);
			}

			ajaxObject
					.setMessage("更新状态成功，当前为"
							+ (user.getStatus() == AppConstants.USER_STATUS_ENABLED ? "可用"
									: "不可用"));
			userService.saveOrUpdate(user);
			// reload permission
			shiroRealm.clearAllCachedAuthenticationCacheInfo();
		}
		request.setAttribute(
				AppConstants.LOG_ARGUMENTS,
				LogMessageObject.newWrite().setObjects(
						new Object[] { user.getUsername(),
								ajaxObject.getMessage() }));
		return ajaxObject.toString();
	}

	@Log(message = "给用户{0}分配了{1}的角色", level = LogLevel.INFO)
	@RequiresPermissions("User:assign")
	@RequestMapping(value = "/create/userRole", method = { RequestMethod.POST })
	public @ResponseBody
	void assignRole(UserRole userRole) {
		userRoleService.saveOrUpdate(userRole);
		User user = userService.get(userRole.getUser().getId());
		Role role = roleService.get(userRole.getRole().getId());
		// reload permission
		shiroRealm.clearAllCachedAuthorizationInfo();
		request.setAttribute(
				AppConstants.LOG_ARGUMENTS,
				LogMessageObject.newWrite().setObjects(
						new Object[] { user.getUsername(), role.getName() }));
	}

	@Log(message = "撤销了用户{0}的{1}角色", level = LogLevel.INFO)
	@RequiresPermissions("User:assign")
	@RequestMapping(value = "/delete/userRole/{userRoleId}", method = { RequestMethod.POST })
	public @ResponseBody
	void deleteUserRole(@PathVariable Long userRoleId) {
		UserRole userRole = userRoleService.get(userRoleId);
		userRoleService.delete(userRoleId);
		// reload permission
		shiroRealm.clearAllCachedAuthorizationInfo();
		request.setAttribute(
				AppConstants.LOG_ARGUMENTS,
				LogMessageObject.newWrite().setObjects(
						new Object[] { userRole.getUser().getUsername(),
								userRole.getRole().getName() }));
	}

	@RequiresPermissions("User:assign")
	@RequestMapping(value = "/lookup2create/userRole/{userId}", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String listUnassignRole(Map<String, Object> map,
			@PathVariable Long userId) {
		Page page = new Page();
		page.setNumPerPage(Integer.MAX_VALUE);

		List<UserRole> userRoles = userRoleService.findByUserId(userId);
		List<Role> roles = roleService.findAll(page);

		List<Role> rentList = new ArrayList<Role>();
		// 删除已分配roles
		for (Role role : roles) {
			boolean isContains = false;
			for (UserRole or : userRoles) {
				if (or.getRole().getId().equals(role.getId())) {
					isContains = true;
					break;
				}
			}
			if (isContains == false) {
				rentList.add(role);
			}
		}

		map.put("userRoles", userRoles);
		map.put("roles", rentList);

		map.put("userId", userId);
		return LOOK_UP_ROLE;
	}

	@RequiresPermissions("User:assign")
	@RequestMapping(value = "/lookup2delete/userRole/{userId}", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String listUserRole(Map<String, Object> map,
			@PathVariable Long userId) {
		List<UserRole> userRoles = userRoleService.findByUserId(userId);
		map.put("userRoles", userRoles);
		return LOOK_USER_ROLE;
	}

	@RequiresPermissions(value = { "User:edit", "User:save" }, logical = Logical.OR)
	@RequestMapping(value = "/lookup2org", method = { RequestMethod.GET })
	public String lookup(Map<String, Object> map) {
		Organization org = organizationService.getTree();
		map.put("org", org);
		return LOOK_ORG;
	}
}
