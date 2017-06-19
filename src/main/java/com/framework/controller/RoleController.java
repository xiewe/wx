package com.framework.controller;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.framework.entity.SysRole;
import com.framework.log4jdbc.Log;
import com.framework.utils.pager.Pager;

@Controller
@RequestMapping("/security/role")
public class RoleController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(RoleController.class);

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
		return CREATE;
	}

	@Log(message = "添加了{0}角色。")
	@RequiresPermissions("Role:save")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody String create(@Valid SysRole role) {
		return "";
	}

	@RequiresPermissions("Role:edit")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String preUpdate(@PathVariable Long id, Map<String, Object> map) {
		return UPDATE;
	}

	@Log(message = "修改了{0}角色的信息。")
	@RequiresPermissions("Role:edit")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody String update(@Valid SysRole role) {
		return "";
	}

	@Log(message = "删除了{0}角色。")
	@RequiresPermissions("Role:delete")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public @ResponseBody String delete(@PathVariable Long id) {
		return "";
	}

	@RequiresPermissions("Role:view")
	@RequestMapping(value = "/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String list(ServletRequest request, Pager pager,
			Map<String, Object> map) {
		return LIST;
	}

	@RequiresPermissions("Role:view")
	@RequestMapping(value = "/view/{id}", method = { RequestMethod.GET })
	public String view(@PathVariable Long id, Map<String, Object> map) {
		return VIEW;
	}

	@RequiresPermissions("Role:assign")
	@RequestMapping(value = "/assignPermission/{id}", method = { RequestMethod.GET })
	public String assignPermission(@PathVariable Long id,
			Map<String, Object> map) {
		return "security/role/assign_role_permission";
	}

	@Log(message = "修改了{0}角色的权限。")
	@RequiresPermissions("Role:assign")
	@RequestMapping(value = "/saveAssignPermission", method = { RequestMethod.POST })
	public @ResponseBody String saveAssignPermission(Long id,
			String permissionJson) {

		return "";
	}

	@RequiresPermissions("Role:assign")
	@RequestMapping(value = "/assign/{id}", method = RequestMethod.GET)
	public String preAssign(@PathVariable Long id, Map<String, Object> map) {
		return ASSIGN_DATA_CONTROL;
	}

	@Log(message = "修改了{0}角色的数据权限。")
	@RequiresPermissions("Role:assign")
	@RequestMapping(value = "/assign", method = RequestMethod.POST)
	public @ResponseBody String assign(SysRole role) {

		return "";
	}

	@RequiresPermissions(value = { "Role:assign" })
	@RequestMapping(value = "/lookup", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String lookup(ServletRequest request, Page page,
			Map<String, Object> map) {
		return LOOKUP_DATA_CONTROL;
	}
}
