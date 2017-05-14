package com.framework.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.framework.AppConstants;
import com.framework.entity.Permission;
import com.framework.exception.ExistedException;
import com.framework.exception.ServiceException;
import com.framework.log4jdbc.Log;
import com.framework.log4jdbc.LogMessageObject;
import com.framework.service.PermissionService;
import com.framework.utils.dwz.AjaxObject;
import com.framework.utils.dwz.Page;
import com.framework.utils.persistence.DynamicSpecifications;
import com.framework.utils.persistence.SearchFilter;
import com.framework.utils.persistence.SearchFilter.Operator;

@Controller
@RequestMapping("/security/permission")
public class PermissionController {

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private HttpServletRequest request;

	private static final String CREATE = "security/permission/create";
	private static final String UPDATE = "security/permission/update";
	private static final String LIST = "security/permission/list";
	private static final String TREE = "security/permission/tree";
	private static final String VIEW = "security/permission/view";
	private static final String TREE_LIST = "security/permission/tree_list";
	private static final String LOOKUP_PARENT = "security/permission/lookup_parent";

	@RequiresPermissions("Permission:save")
	@RequestMapping(value = "/create/{parentModuleId}", method = RequestMethod.GET)
	public String preCreate(Map<String, Object> map,
			@PathVariable Long parentModuleId) {
		map.put("parentModuleId", parentModuleId);
		return CREATE;
	}

	@Log(message = "添加了{0}模块。")
	@RequiresPermissions("Permission:save")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody
	String create(@Valid Permission permission) {
		Permission parentModule = permissionService.get(permission.getParent()
				.getId());
		if (parentModule == null) {
			return AjaxObject
					.ajaxDoneError(
							"添加模块失败：id=" + permission.getParent().getId()
									+ "的父模块不存在！").setCallbackType("")
					.toString();
		}

		// 验证模块类是否有效
		try {
			Class.forName(permission.getClassName()).newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			return AjaxObject.ajaxDoneError("添加模块失败：类名填写有误，找不到模块类！")
					.setCallbackType("").toString();
		}

		if (permission.getCategory() == AppConstants.PERMISSION_TYPE_FOLDER) {
			permission.setChildren(null);
		} else {
			// 根据页面提交信息获取子模块，如果权限key为null代表未选中，则不添加
			List<Permission> childrens = new ArrayList<Permission>();
			for (Permission childrenPermission : permission.getChildren()) {
				if (StringUtils.isNotEmpty(childrenPermission.getSn())) {
					childrenPermission
							.setCategory(AppConstants.PERMISSION_TYPE_FINAL);
					childrenPermission.setParent(permission);
					childrenPermission.setSn(permission.getSn().split(":")[0]
							+ ":" + childrenPermission.getSn());
					childrenPermission.setClassName(permission.getClassName());
					childrens.add(childrenPermission);
				}
			}

			if (!childrens.isEmpty()) {
				permission.setChildren(childrens);
			} else {
				permission.setChildren(null);
			}
		}

		try {
			permissionService.saveOrUpdate(permission);
		} catch (ExistedException e) {
			return AjaxObject.ajaxDoneError("添加模块失败：" + e.getMessage())
					.setCallbackType("").toString();
		}
		request.setAttribute(AppConstants.LOG_ARGUMENTS, LogMessageObject
				.newWrite().setObjects(new Object[] { permission.getName() }));
		return AjaxObject.ajaxDoneSuccess("添加模块成功！")
				.setNavTabId("Permission:*").toString();
	}

	@RequiresPermissions("Permission:edit")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String preUpdate(@PathVariable Long id, Map<String, Object> map) {
		Permission permission = permissionService.get(id);

		map.put("permission", permission);
		return UPDATE;
	}

	@Log(message = "修改了{0}模块的信息。")
	@RequiresPermissions("Permission:edit")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody
	String update(@Valid Permission permission) {
		Permission oldPermission = permissionService.get(permission.getId());
		oldPermission.setName(permission.getName());
		oldPermission.setPriority(permission.getPriority());
		oldPermission.setDescription(permission.getDescription());
		oldPermission.setSn(permission.getSn());
		oldPermission.setUrl(permission.getUrl());
		oldPermission.setParent(permission.getParent());
		oldPermission.setClassName(permission.getClassName());

		// 模块自定义权限，删除过后新增报错，会有validate的验证错误。hibernate不能copy到permission的值，导致。
		for (Permission childrenPermission : permission.getChildren()) {
			if (StringUtils.isNotBlank(childrenPermission.getSn())) {// 已选中的
				if (childrenPermission.getId() == null) {// 新增
					childrenPermission.setParent(oldPermission);
					childrenPermission
							.setCategory(AppConstants.PERMISSION_TYPE_FINAL);
					oldPermission.getChildren().add(childrenPermission);
					permissionService.saveOrUpdate(childrenPermission);
				}
			} else {// 未选中的
				if (childrenPermission.getId() != null) {// 删除
					for (Permission oldChildrenPermission : oldPermission
							.getChildren()) {
						if (oldChildrenPermission.getId().equals(
								childrenPermission.getId())) {
							oldChildrenPermission.setParent(null);
							childrenPermission = oldChildrenPermission;
							break;
						}
					}
					permissionService.delete(childrenPermission.getId());
					oldPermission.getChildren().remove(childrenPermission);
				}
			}
		}

		permissionService.saveOrUpdate(oldPermission);
		request.setAttribute(AppConstants.LOG_ARGUMENTS, LogMessageObject
				.newWrite()
				.setObjects(new Object[] { oldPermission.getName() }));
		return AjaxObject.ajaxDoneSuccess("修改模块成功！")
				.setNavTabId("Permission:*").toString();
	}

	@Log(message = "删除了{0}模块。")
	@RequiresPermissions("Permission:delete")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public @ResponseBody
	String delete(@PathVariable Long id) {
		Permission permission = permissionService.get(id);
		try {
			if (permission.getSecurityLevel() == Permission.SecurityLevel.SYSTEM) {
				throw new ServiceException("不能删除系统模块！");
			}
			permissionService.delete(id);
		} catch (ServiceException e) {
			return AjaxObject.ajaxDoneError("删除模块失败：" + e.getMessage())
					.setCallbackType("").toString();
		}
		request.setAttribute(AppConstants.LOG_ARGUMENTS, LogMessageObject
				.newWrite().setObjects(new Object[] { permission.getName() }));
		return AjaxObject.ajaxDoneSuccess("删除模块成功！").setCallbackType("")
				.toString();
	}

	@RequiresPermissions("Permission:view")
	@RequestMapping(value = "/tree_list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String tree_list(Map<String, Object> map) {
		return TREE_LIST;
	}

	@RequiresPermissions("Permission:view")
	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	public String tree(Map<String, Object> map) {
		Permission permission = permissionService.getTree();

		map.put("permission", permission);
		return TREE;
	}

	@RequiresPermissions("Permission:view")
	@RequestMapping(value = "/list/{parentModuleId}", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String list(ServletRequest request, Page page,
			@PathVariable Long parentModuleId, Map<String, Object> map) {
		Specification<Permission> specification = DynamicSpecifications
				.buildSpecification(request, Permission.class,
						new SearchFilter("parent.id", Operator.EQ,
								parentModuleId));
		List<Permission> modules = permissionService.findByPageable(
				specification, page);

		map.put("page", page);
		map.put("modules", modules);
		map.put("parentModuleId", parentModuleId);

		return LIST;
	}

	@RequiresPermissions("Permission:view")
	@RequestMapping(value = "/view/{id}", method = { RequestMethod.GET })
	public String view(@PathVariable Long id, Map<String, Object> map) {
		Permission permission = permissionService.get(id);

		map.put("permission", permission);
		return VIEW;
	}

	@RequiresPermissions(value = { "Permission:edit", "Permission:save" }, logical = Logical.OR)
	@RequestMapping(value = "/lookupParent/{id}", method = { RequestMethod.GET })
	public String lookup(@PathVariable Long id, Map<String, Object> map) {
		Permission permission = permissionService.getTree();

		map.put("permission", permission);
		return LOOKUP_PARENT;
	}
}
