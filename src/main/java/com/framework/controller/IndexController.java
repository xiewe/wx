package com.framework.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.framework.AppConstants;
import com.framework.entity.SysMenu;
import com.framework.entity.SysRolePermission;
import com.framework.entity.SysUser;
import com.framework.exception.ServiceException;
import com.framework.log4jdbc.Log;
import com.framework.log4jdbc.LogLevel;
import com.framework.service.SysMenuService;
import com.framework.service.SysUserService;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController {

	@Autowired
	private SysMenuService sysMenuService;

	@Autowired
	private SysUserService sysUserService;

	private static final String INDEX = "index";
	private static final String UPDATE_PASSWORD = "updatePwd";
	private static final String UPDATE_BASE = "updateBase";

	@RequiresUser
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ServletRequest request, Map<String, Object> map) {
		SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
		map.put(AppConstants.LOGIN_USER, user);

		List<SysMenu> listMenu = new ArrayList<SysMenu>();
		listMenu = sysMenuService.findByRoleId(user.getSysRole().getId());

		listMenu.sort(new Comparator<SysMenu>() {

			@Override
			public int compare(SysMenu o1, SysMenu o2) {
				return o1.getId() - o2.getId();
			}

		});
		map.put("menu", listMenu);

		return INDEX;
	}

	@RequiresUser
	@RequestMapping(value = "/updatePwd", method = RequestMethod.GET)
	public String preUpdatePassword() {
		return UPDATE_PASSWORD;
	}

	@Log(message = "{0}用户修改密码成功！", level = LogLevel.DEBUG)
	@RequiresUser
	@RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
	public @ResponseBody String updatePassword(ServletRequest request, String plainPassword, String newPassword,
	        String rPassword) {

		SysUser loginUser = (SysUser) org.apache.shiro.SecurityUtils.getSubject().getPrincipal();

		if (newPassword != null && newPassword.equals(rPassword)) {
			loginUser.setPlainPassword(plainPassword);
			try {
				sysUserService.updatePwd(loginUser, newPassword);
			} catch (ServiceException e) {
				return "failed";
			}

			setLogObject(new Object[] { loginUser.getUsername() });
			return "success";
		}

		return "failed";
	}

	@RequiresUser
	@RequestMapping(value = "/updateBase", method = RequestMethod.GET)
	public String preUpdateBase(Map<String, Object> map) {
		map.put(AppConstants.LOGIN_USER, ((SysUser) org.apache.shiro.SecurityUtils.getSubject().getPrincipal()));
		return UPDATE_BASE;
	}

	@Log(message = "{0}修改详细信息成功！", level = LogLevel.DEBUG)
	@RequiresUser
	@RequestMapping(value = "/updateBase", method = RequestMethod.POST)
	public @ResponseBody String updateBase(SysUser user, ServletRequest request) {
		SysUser loginUser = (SysUser) org.apache.shiro.SecurityUtils.getSubject().getPrincipal();

		loginUser.setPhone(user.getPhone());
		loginUser.setEmail(user.getEmail());

		sysUserService.saveOrUpdate(loginUser);
		setLogObject(new Object[] { loginUser.getUsername() });
		return "success";
	}
}
