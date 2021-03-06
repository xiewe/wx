package com.framework.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.framework.entity.SysLog;
import com.framework.service.SysLogService;
import com.framework.utils.pager.DynamicSpecifications;
import com.framework.utils.pager.Pager;

@Controller
@RequestMapping("/log")
public class SysLogController {

	@Autowired
	private SysLogService sysLogService;

	private static final String LIST = "sys/logInfo/list";

	@InitBinder
	public void dataBinder(WebDataBinder dataBinder) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(df, true));
	}

	@RequiresPermissions("SysLog:delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody String deleteMany(Long[] ids) {
		for (Long id : ids) {
			sysLogService.delete(id);
		}
		return "success";
	}

	@RequiresPermissions("SysLog:view")
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(ServletRequest request, Pager pager, Map<String, Object> map) {
		Specification<SysLog> specification = DynamicSpecifications.buildSpecification(request, SysLog.class);
		pager.setOrderField("createTime");
		pager.setOrderDirection("desc");
		List<SysLog> logs = sysLogService.findByPageable(specification, pager);
		map.put("pager", pager);
		map.put("logs", logs);
		return LIST;
	}
}
