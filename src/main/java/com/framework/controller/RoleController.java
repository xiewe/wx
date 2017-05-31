package com.framework.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.AppConstants;
import com.framework.entity.DataControl;
import com.framework.entity.Permission;
import com.framework.entity.Role;
import com.framework.entity.RolePermission;
import com.framework.entity.RolePermissionDataControl;
import com.framework.exception.ServiceException;
import com.framework.log4jdbc.Log;
import com.framework.log4jdbc.LogMessageObject;
import com.framework.service.DataControlService;
import com.framework.service.PermissionService;
import com.framework.service.RolePermissionDataControlService;
import com.framework.service.RolePermissionService;
import com.framework.service.RoleService;
import com.framework.service.UserService;
import com.framework.shiro.ShiroRealm;
import com.framework.utils.dwz.AjaxObject;
import com.framework.utils.dwz.Page;
import com.framework.utils.persistence.DynamicSpecifications;

@Controller
@RequestMapping("/security/role")
public class RoleController extends BaseController {
	private final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Autowired
	private RolePermissionService rolePermissionService;

	@Autowired
	private RolePermissionDataControlService rolePermissionDataControlService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private DataControlService dataControlService;

	@Autowired
	ShiroRealm shiroRealm;

	@Autowired
	private HttpServletRequest request;

	private static final String CREATE = "security/role/create";
	private static final String UPDATE = "security/role/update";
	private static final String LIST = "security/role/list";
	private static final String VIEW = "security/role/view";
	private static final String ASSIGN_DATA_CONTROL = "security/role/assign_data_control";
	private static final String LOOKUP_DATA_CONTROL = "security/role/lookup_data_control";

	@InitBinder
	public void initListBinder(WebDataBinder binder) {
		// 使用SpringMVC提交数组时，如果list大小超过256，就会报错，默认为256
		binder.setAutoGrowCollectionLimit(5000);
	}

	@RequiresPermissions("Role:save")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String preCreate(Map<String, Object> map) {
		map.put("module", permissionService.getTree());
		return CREATE;
	}

	@Log(message = "添加了{0}角色。")
	@RequiresPermissions("Role:save")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody
	String create(@Valid Role role) {
		role.setCategory(AppConstants.ROLE_TYPE_GENERAL);
		roleService.saveOrUpdate(role);
		request.setAttribute(AppConstants.LOG_ARGUMENTS, LogMessageObject
				.newWrite().setObjects(new Object[] { role.getName() }));
		return AjaxObject.ajaxDoneSuccess("添加角色成功！").setNavTabId("Role:*")
				.toString();
	}

	@RequiresPermissions("Role:edit")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String preUpdate(@PathVariable Long id, Map<String, Object> map) {
		Role role = roleService.get(id);
		map.put("role", role);
		return UPDATE;
	}

	@Log(message = "修改了{0}角色的信息。")
	@RequiresPermissions("Role:edit")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody
	String update(@Valid Role role) {
		Role oldRole = roleService.get(role.getId());
		oldRole.setName(role.getName());
		oldRole.setDescription(role.getDescription());

		roleService.saveOrUpdate(oldRole);
		request.setAttribute(AppConstants.LOG_ARGUMENTS, LogMessageObject
				.newWrite().setObjects(new Object[] { oldRole.getName() }));
		return AjaxObject.ajaxDoneSuccess("修改角色成功！").setNavTabId("Role:*")
				.toString();
	}

	@Log(message = "删除了{0}角色。")
	@RequiresPermissions("Role:delete")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public @ResponseBody
	String delete(@PathVariable Long id) {
		Role role = roleService.get(id);
		if (role.getCategory() == AppConstants.ROLE_TYPE_SYSTEM) {
			throw new ServiceException("不能删除系统角色！");
		}
		roleService.delete(role.getId());
		// reload permission
		shiroRealm.clearAllCachedAuthorizationInfo();
		request.setAttribute(AppConstants.LOG_ARGUMENTS, LogMessageObject
				.newWrite().setObjects(new Object[] { role.getName() }));
		return AjaxObject.ajaxDoneSuccess("删除角色成功！").setCallbackType("")
				.setNavTabId("Role:*").toString();
	}

	@RequiresPermissions("Role:view")
	@RequestMapping(value = "/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String list(ServletRequest request, Page page,
			Map<String, Object> map) {
		Specification<Role> specification = DynamicSpecifications
				.buildSpecification(request, Role.class);
		List<Role> roles = roleService.findByPageable(specification, page);

		map.put("page", page);
		map.put("roles", roles);
		return LIST;
	}

	@RequiresPermissions("Role:view")
	@RequestMapping(value = "/view/{id}", method = { RequestMethod.GET })
	public String view(@PathVariable Long id, Map<String, Object> map) {
		Role role = roleService.get(id);
		String permissionJson = "";

		List<Permission> lstPermissions = new ArrayList();
		// get role all permission
		List<RolePermission> rolePermissions = role.getRolePermissions();

		// set checked
		for (RolePermission rolePermission : rolePermissions) {
			Permission p = permissionService.get(rolePermission.getPermission()
					.getId());
			p.setOpen(true);
			p.setChecked(true);
			lstPermissions.add(p);
		}

		// convert to json
		ObjectMapper mapper = new ObjectMapper();
		try {
			permissionJson = mapper.writeValueAsString(lstPermissions);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} finally {
			mapper = null;
		}

		map.put("permissionJson", permissionJson);
		map.put("role", role);
		return VIEW;
	}

	@RequiresPermissions("Role:assign")
	@RequestMapping(value = "/assignPermission/{id}", method = { RequestMethod.GET })
	public String assignPermission(@PathVariable Long id,
			Map<String, Object> map) {
		Role role = roleService.get(id);
		String permissionJson = "";
		List<Permission> lstPermissions = permissionService.findAll();

		// get role all permission
		Set<RolePermission> rolePermissions = new HashSet<RolePermission>(
				role.getRolePermissions());

		// set checked
		for (int i = 0; i < lstPermissions.size(); i++) {
			lstPermissions.get(i).setOpen(true);
			for (RolePermission rolePermission : rolePermissions) {
				if (lstPermissions
						.get(i)
						.getSn()
						.equalsIgnoreCase(
								rolePermission.getPermission().getSn())) {
					lstPermissions.get(i).setChecked(true);
				}
			}
		}

		// convert to json
		ObjectMapper mapper = new ObjectMapper();
		try {
			permissionJson = mapper.writeValueAsString(lstPermissions);
		} catch (JsonProcessingException e) {
			logger.error("", e);
		} finally {
			mapper = null;
		}
		map.put("permissionJson", permissionJson);
		map.put("role", role);
		return "security/role/assign_role_permission";
	}

	@Log(message = "修改了{0}角色的权限。")
	@RequiresPermissions("Role:assign")
	@RequestMapping(value = "/saveAssignPermission", method = { RequestMethod.POST })
	public @ResponseBody
	String saveAssignPermission(Long id, String permissionJson) {
		logger.debug(permissionJson);
		Role role = roleService.get(id);
		ObjectMapper mapper = null;
		Permission[] arrAssignPermissions = null;
		try {
			mapper = new ObjectMapper();
			// convert json to array
			arrAssignPermissions = mapper.readValue(permissionJson,
					Permission[].class);
		} catch (Exception e) {
			logger.error("分配角色权限错误", e);
			return AjaxObject.ajaxDoneError("分配角色权限错误：" + e.getMessage())
					.setCallbackType("").toString();
		} finally {
			mapper = null;
		}

		permissionService.saveAssignRolePermission(arrAssignPermissions, role);

		request.setAttribute(AppConstants.LOG_ARGUMENTS, LogMessageObject
				.newWrite().setObjects(new Object[] { role.getName() }));

		// reload permission
		shiroRealm.clearAllCachedAuthorizationInfo();

		return AjaxObject.ajaxDoneSuccess(getMessage("msg.operation.success"))
				.setNavTabId("Role:*").toString();
	}

	@RequiresPermissions("Role:assign")
	@RequestMapping(value = "/assign/{id}", method = RequestMethod.GET)
	public String preAssign(@PathVariable Long id, Map<String, Object> map) {
		Role role = roleService.get(id);
		Map<Permission, List<RolePermission>> mpMap = new LinkedHashMap<Permission, List<RolePermission>>();
		for (RolePermission rolePermission : role.getRolePermissions()) {
			Permission permission = rolePermission.getPermission().getParent();
			if (permission == null) {
				continue;
			}
			if (permission.getCategory() == AppConstants.PERMISSION_TYPE_FOLDER) {
				continue;
			}
			List<RolePermission> lstRolePermissions = mpMap.get(permission);
			if (lstRolePermissions == null) {
				lstRolePermissions = new ArrayList<RolePermission>();
				mpMap.put(permission, lstRolePermissions);
			}
			lstRolePermissions.add(rolePermission);
		}
		map.put("role", role);
		map.put("mpMap", mpMap);
		return ASSIGN_DATA_CONTROL;
	}

	@Log(message = "修改了{0}角色的数据权限。")
	@RequiresPermissions("Role:assign")
	@RequestMapping(value = "/assign", method = RequestMethod.POST)
	public @ResponseBody
	String assign(Role role) {

		// 校验数据权限与角色操作权限是否匹配，即是否有关联性，如自定义类型，则验证指定列是否存在；通用类型：如用户关联数据、组织关联数据，该操作实体是否与用户或组织相关联
		List<RolePermissionDataControl> newRolePermissionDataControlList = new ArrayList<RolePermissionDataControl>();
		for (RolePermission rolePermission : role.getRolePermissions()) {
			for (RolePermissionDataControl rolePermissionDataControl : rolePermission
					.getRolePermissionDataControls()) {
				rolePermission = rolePermissionService
						.get(rolePermissionDataControl.getRolePermission()
								.getId());
				Permission permission = permissionService.get(rolePermission
						.getPermission().getId());
				DataControl dataControl = dataControlService
						.get(rolePermissionDataControl.getDataControl().getId());
				// 是否有定义列名
				if (!dataControlService.isDeclaredField(permission,
						dataControl, true)) {
					return AjaxObject
							.ajaxDoneError(
									"分配数据权限失败！不能为【" + permission.getName()
											+ "】分配【" + dataControl.getName()
											+ "】数据级权限，原因：找不到关联列。")
							.setCallbackType("").toString();
				}
				newRolePermissionDataControlList.add(rolePermissionDataControl);
			}
		}

		// 删除原有数据权限
		List<RolePermissionDataControl> hasRolePermissionDataControls = rolePermissionDataControlService
				.findByRolePermissionRoleId(role.getId());
		rolePermissionDataControlService.delete(hasRolePermissionDataControls);

		// 保存当前数据权限
		rolePermissionDataControlService.save(newRolePermissionDataControlList);

		// reload permission
		shiroRealm.clearAllCachedAuthorizationInfo();

		role = roleService.get(role.getId());
		request.setAttribute(AppConstants.LOG_ARGUMENTS, LogMessageObject
				.newWrite().setObjects(new Object[] { role.getName() }));
		return AjaxObject.ajaxDoneSuccess("分配数据权限成功！").setNavTabId("Role:*")
				.toString();
	}

	@RequiresPermissions(value = { "Role:assign" })
	@RequestMapping(value = "/lookup", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String lookup(ServletRequest request, Page page,
			Map<String, Object> map) {
		Specification<DataControl> specification = DynamicSpecifications
				.buildSpecification(request, DataControl.class);
		List<DataControl> dataControls = dataControlService.findByPageable(
				specification, page);

		map.put("page", page);
		map.put("dataControls", dataControls);
		return LOOKUP_DATA_CONTROL;
	}
}
