package com.framework.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.AppConstants;
import com.framework.SysErrorCode;
import com.framework.entity.GeneralResponseData;
import com.framework.entity.SysRole;
import com.framework.log4jdbc.Log;
import com.framework.log4jdbc.LogLevel;
import com.framework.service.SysRoleService;
import com.framework.utils.pager.Dictionary.OperatorEum;
import com.framework.utils.pager.DynamicSpecifications;
import com.framework.utils.pager.Pager;
import com.framework.utils.pager.Rule;
import com.framework.utils.pager.SearchFilter;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private SysRoleService sysRoleService;

	ObjectMapper mapper = new ObjectMapper();
	private static final String CREATE = "sys/role/create";
	private static final String UPDATE = "sys/role/update";
	private static final String LIST = "sys/role/list";

	@InitBinder
	public void initListBinder(WebDataBinder binder) {
		// 使用SpringMVC提交数组时，如果list大小超过256，就会报错，默认为256
		binder.setAutoGrowCollectionLimit(5000);
	}

	@RequiresPermissions("SysRole:create")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String preCreate() {
		return CREATE;
	}

	@Log(message = "添加了{0}角色。", level = LogLevel.INFO)
	@RequiresPermissions("SysRole:create")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody String create(@Valid SysRole role) throws JsonProcessingException {
		GeneralResponseData<SysRole> ret = new GeneralResponseData<SysRole>();

		SysRole o = sysRoleService.saveOrUpdate(role);
		if (o == null) {
			ret.setStatus(AppConstants.FAILED);
			ret.setErrCode("1001");
			ret.setErrMsg(SysErrorCode.MAP.get("1001"));
		} else {
			ret.setStatus(AppConstants.SUCCESS);
			ret.setData(o);
			setLogObject(new Object[] { role.getName() });
		}

		return mapper.writeValueAsString(ret);
	}

	@Log(message = "删除了{0}角色。", level = LogLevel.INFO)
	@RequiresPermissions("SysRole:delete")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public @ResponseBody String delete(@PathVariable Integer id) throws JsonProcessingException {
		GeneralResponseData<String> ret = new GeneralResponseData<String>();
		sysRoleService.delete(id);
		ret.setStatus(AppConstants.SUCCESS);
		setLogObject(new Object[] { id });
		return mapper.writeValueAsString(ret);
	}

	@ModelAttribute("preload")
	public SysRole preload(@RequestParam(value = "id", required = false) Integer id) {
		if (id != null) {
			SysRole role = sysRoleService.get(id);
			return role;
		}
		return null;
	}

	@RequiresPermissions("SysRole:update")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String preUpdate(@PathVariable Integer id, Map<String, Object> map) {
		SysRole role = sysRoleService.get(id);
		map.put("role", role);
		return UPDATE;
	}

	@Log(message = "修改了{0}角色的信息。", level = LogLevel.INFO)
	@RequiresPermissions("SysRole:update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody String update(@Valid @ModelAttribute("preload") SysRole role)
	        throws JsonProcessingException {
		GeneralResponseData<SysRole> ret = new GeneralResponseData<SysRole>();

		SysRole o = sysRoleService.saveOrUpdate(role);
		if (o == null) {
			ret.setStatus(AppConstants.FAILED);
			ret.setErrCode("1001");
			ret.setErrMsg(SysErrorCode.MAP.get("1001"));
		} else {
			ret.setStatus(AppConstants.SUCCESS);
			ret.setData(o);
			setLogObject(new Object[] { role.getName() });
		}

		return mapper.writeValueAsString(ret);
	}

	@RequiresPermissions(value = { "SysRole:view", "SysRole:create", "SysRole:update", "SysRole:delete" }, logical = Logical.OR)
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(ServletRequest request, Pager pager, Map<String, Object> map) {
		Specification<SysRole> specification = DynamicSpecifications.buildSpecification(request, SysRole.class);
		List<SysRole> roles = sysRoleService.findByPageable(specification, pager);
		map.put("pager", pager);
		map.put("roles", roles);

		return LIST;
	}

	@RequiresPermissions("SysRole:view")
	@RequestMapping(value = "/json", method = RequestMethod.GET)
	public @ResponseBody String json(@RequestParam(value = "filter", required = false) String filter)
	        throws JsonProcessingException {
		GeneralResponseData<List<SysRole>> ret = new GeneralResponseData<List<SysRole>>();

		List<SysRole> list = new ArrayList<SysRole>();
		if (filter != null) {
			SearchFilter sf = new SearchFilter();
			Rule rule = new Rule();
			rule.setField("name");
			rule.setOperator(OperatorEum.LIKE);
			rule.setData(filter);
			sf.addRule(rule);
			Specification<SysRole> specification = DynamicSpecifications.buildSpecification(sf, SysRole.class);

			list = sysRoleService.findAll(specification);
		} else {
			list = sysRoleService.findAll();
		}

		ret.setStatus(AppConstants.SUCCESS);
		ret.setData(list);

		return mapper.writeValueAsString(ret);
	}

}
