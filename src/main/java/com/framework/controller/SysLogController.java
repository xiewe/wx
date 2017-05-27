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
import com.framework.utils.page.Page;
import com.framework.utils.persistence.DynamicSpecifications;

@Controller
@RequestMapping("/security/sysLog")
public class SysLogController {

	@Autowired
	private SysLogService sysLogService;

	private static final String LIST = "security/sysLog/list";

	@InitBinder
	public void dataBinder(WebDataBinder dataBinder) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(df,
				true));
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
	@RequestMapping(value = "/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String list(ServletRequest request, Page page,
			Map<String, Object> map) {
		Specification<SysLog> specification = DynamicSpecifications
				.buildSpecification(request, SysLog.class);
		List<SysLog> sysLogs = sysLogService
				.findByPageable(specification, page);
		map.put("logLevels", new String[] { "TRACE", "DEBUG", "INFO", "WARN",
				"ERROR" });
		map.put("page", page);
		map.put("sysLogs", sysLogs);

		return LIST;
	}
}
