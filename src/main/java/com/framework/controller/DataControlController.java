package com.framework.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.framework.AppConstants;
import com.framework.entity.DataControl;
import com.framework.exception.ServiceException;
import com.framework.log4jdbc.Log;
import com.framework.log4jdbc.LogMessageObject;
import com.framework.service.DataControlService;
import com.framework.utils.persistence.DynamicSpecifications;

@Controller
@RequestMapping("/security/dataControl")
public class DataControlController {

	@Autowired
	private DataControlService dataControlService;

	@Autowired
	private HttpServletRequest request;

	private static final String CREATE = "security/dataControl/create";
	private static final String UPDATE = "security/dataControl/update";
	private static final String LIST = "security/dataControl/list";
	private static final String VIEW = "security/dataControl/view";

	@RequiresPermissions("DataControl:save")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String preCreate(Map<String, Object> map) {
		return CREATE;
	}

	@Log(message = "添加了id={0}数据权限。")
	@RequiresPermissions("DataControl:save")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody
	String create(@Valid DataControl dataControl) {
		dataControlService.saveOrUpdate(dataControl);
		request.setAttribute(AppConstants.LOG_ARGUMENTS, LogMessageObject
				.newWrite().setObjects(new Object[] { dataControl.getId() }));
		return "";
	}

	@ModelAttribute("preloadDataControl")
	public DataControl preload(
			@RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			DataControl dataControl = dataControlService.get(id);
			return dataControl;
		}
		return null;
	}

	@RequiresPermissions("DataControl:edit")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String preUpdate(@PathVariable Long id, Map<String, Object> map) {
		DataControl dataControl = dataControlService.get(id);
		map.put("dataControl", dataControl);
		return UPDATE;
	}

	@Log(message = "修改了id={0}数据权限的信息。")
	@RequiresPermissions("DataControl:edit")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody
	String update(
			@Valid @ModelAttribute("preloadDataControl") DataControl dataControl) {
		dataControlService.saveOrUpdate(dataControl);
		request.setAttribute(AppConstants.LOG_ARGUMENTS, LogMessageObject
				.newWrite().setObjects(new Object[] { dataControl.getId() }));
		return "";
	}

	@Log(message = "删除了id={0}数据权限。")
	@RequiresPermissions("DataControl:delete")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public @ResponseBody
	String delete(@PathVariable Long id) {
		DataControl dataControl = dataControlService.get(id);
		if (!dataControl.getCategory().equals(
				AppConstants.DATACONTROL_CATEGORY_CUSTOM)) {
			throw new ServiceException("不能删除通用数据权限！");
		}
		dataControlService.delete(id);
		request.setAttribute(AppConstants.LOG_ARGUMENTS, LogMessageObject
				.newWrite().setObjects(new Object[] { id }));
		return "";
	}

	@Log(message = "批量删除了id={0}数据权限。")
	@RequiresPermissions("DataControl:delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	String deleteMany(Long[] ids) {
		for (int i = 0; i < ids.length; i++) {
			DataControl dataControl = dataControlService.get(ids[i]);
			if (!dataControl.getCategory().equals(
					AppConstants.DATACONTROL_CATEGORY_CUSTOM)) {
				throw new ServiceException("不能删除通用数据权限！");
			}
			dataControlService.delete(dataControl.getId());
		}
		request.setAttribute(AppConstants.LOG_ARGUMENTS, LogMessageObject
				.newWrite().setObjects(new Object[] { Arrays.toString(ids) }));
		return "";
	}

	@RequiresPermissions("DataControl:view")
	@RequestMapping(value = "/view/{id}", method = { RequestMethod.GET })
	public String view(@PathVariable Long id, Map<String, Object> map) {
		DataControl dataControl = dataControlService.get(id);
		map.put("dataControl", dataControl);
		return VIEW;
	}
}
