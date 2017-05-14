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

import com.framework.entity.LogInfo;
import com.framework.service.LogInfoService;
import com.framework.utils.dwz.AjaxObject;
import com.framework.utils.dwz.Page;
import com.framework.utils.persistence.DynamicSpecifications;

@Controller
@RequestMapping("/security/logInfo")
public class LogInfoController {

	@Autowired
	private LogInfoService logInfoService;

	private static final String LIST = "security/logInfo/list";

	@InitBinder
	public void dataBinder(WebDataBinder dataBinder) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(df,
				true));
	}

	@RequiresPermissions("LogInfo:delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	String deleteMany(Long[] ids) {
		for (Long id : ids) {
			logInfoService.delete(id);
		}
		return "success";
	}

	@RequiresPermissions("LogInfo:view")
	@RequestMapping(value = "/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String list(ServletRequest request, Page page,
			Map<String, Object> map) {
		Specification<LogInfo> specification = DynamicSpecifications
				.buildSpecification(request, LogInfo.class);
		List<LogInfo> logInfos = logInfoService.findByPageable(specification,
				page);
		map.put("logLevels", new String[] { "TRACE", "DEBUG", "INFO", "WARN",
				"ERROR" });
		map.put("page", page);
		map.put("logInfos", logInfos);

		return LIST;
	}
}
