package com.framework.controller;

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
import com.framework.exception.ServiceException;
import com.framework.log4jdbc.Log;
import com.framework.log4jdbc.LogMessageObject;
import com.framework.service.OrganizationService;
import com.framework.service.RoleService;
import com.framework.utils.dwz.AjaxObject;
import com.framework.utils.dwz.Page;
import com.framework.utils.persistence.DynamicSpecifications;
import com.framework.utils.persistence.SearchFilter;
import com.framework.utils.persistence.SearchFilter.Operator;

@Controller
@RequestMapping("/security/organization")
public class OrganizationController {
	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private HttpServletRequest request;

	private static final String CREATE = "security/organization/create";
	private static final String UPDATE = "security/organization/update";
	private static final String LIST = "security/organization/list";
	private static final String TREE = "security/organization/tree";
	private static final String TREE_LIST = "security/organization/tree_list";

	private static final String LOOKUP_PARENT = "security/organization/lookup_parent";

	@RequiresPermissions("Organization:save")
	@RequestMapping(value = "/create/{parentOrganizationId}", method = RequestMethod.GET)
	public String preCreate(@PathVariable Long parentOrganizationId,
			Map<String, Object> map) {
		map.put("parentOrganizationId", parentOrganizationId);
		return CREATE;
	}

	@Log(message = "添加了{0}组织。")
	@RequiresPermissions("Organization:save")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody
	String create(@Valid Organization organization) {
		try {
			organizationService.saveOrUpdate(organization);
			request.setAttribute(
					AppConstants.LOG_ARGUMENTS,
					LogMessageObject.newWrite().setObjects(
							new Object[] { organization.getName() }));
		} catch (ServiceException e) {
			return AjaxObject.ajaxDoneError("添加组织失败：" + e.getMessage())
					.toString();
		}
		return AjaxObject.ajaxDoneSuccess("添加组织成功！")
				.setNavTabId("Organization:*").toString();
	}

	@ModelAttribute("preloadOrg")
	public Organization preload(
			@RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			Organization organization = organizationService.get(id);
			organization.setParent(null);
			return organization;
		}
		return null;
	}

	@RequiresPermissions("Organization:edit")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String preUpdate(@PathVariable Long id, Map<String, Object> map) {
		Organization organization = organizationService.get(id);

		map.put("organization", organization);
		return UPDATE;
	}

	@Log(message = "修改了{0}组织信息。")
	@RequiresPermissions("Organization:edit")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody
	String update(@Valid @ModelAttribute("preloadOrg") Organization organization) {
		organizationService.saveOrUpdate(organization);
		request.setAttribute(AppConstants.LOG_ARGUMENTS, LogMessageObject
				.newWrite().setObjects(new Object[] { organization.getName() }));
		return AjaxObject.ajaxDoneSuccess("修改组织成功！").toString();
	}

	@Log(message = "删除了ID为{0}的组织。")
	@RequiresPermissions("Organization:delete")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public @ResponseBody
	String delete(@PathVariable Long id) {
		try {
			organizationService.delete(id);
			request.setAttribute(AppConstants.LOG_ARGUMENTS, LogMessageObject
					.newWrite().setObjects(new Object[] { id }));
		} catch (ServiceException e) {
			return AjaxObject.ajaxDoneError("删除组织失败：" + e.getMessage())
					.setCallbackType("").toString();
		}
		return AjaxObject.ajaxDoneSuccess("删除组织成功！").setCallbackType("")
				.toString();
	}

	@RequiresPermissions("Organization:view")
	@RequestMapping(value = "/tree_list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String tree_list(Map<String, Object> map) {
		return TREE_LIST;
	}

	@RequiresPermissions("Organization:view")
	@RequestMapping(value = "/tree", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String tree(Map<String, Object> map) {
		Organization organization = organizationService.getTree();

		map.put("organization", organization);
		return TREE;
	}

	@RequiresPermissions("Organization:view")
	@RequestMapping(value = "/list/{parentOrganizationId}", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String list(ServletRequest request, Page page,
			@PathVariable Long parentOrganizationId, Map<String, Object> map) {
		Specification<Organization> specification = DynamicSpecifications
				.buildSpecification(request, Organization.class,
						new SearchFilter("parent.id", Operator.EQ,
								parentOrganizationId));
		List<Organization> organizations = organizationService.findByPageable(
				specification, page);

		map.put("page", page);
		map.put("organizations", organizations);
		map.put("parentOrganizationId", parentOrganizationId);

		return LIST;
	}

	@RequiresPermissions(value = { "Organization:edit", "Organization:save" }, logical = Logical.OR)
	@RequestMapping(value = "/lookupParent/{id}", method = { RequestMethod.GET })
	public String lookup(@PathVariable Long id, Map<String, Object> map) {
		Organization organization = organizationService.getTree();

		map.put("organization", organization);
		return LOOKUP_PARENT;
	}
}
