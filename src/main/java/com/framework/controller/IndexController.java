package com.framework.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.framework.log4jdbc.Log;
import com.framework.log4jdbc.LogLevel;
import com.framework.service.SysRolePermissionService;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController {

	@Autowired
	private SysRolePermissionService sysRolePermissionService;

	private static final String INDEX = "index";
	private static final String UPDATE_PASSWORD = "updatePwd";

	@RequiresUser
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ServletRequest request, Map<String, Object> map) {
		SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
		map.put(AppConstants.LOGIN_USER, user);
		Set<SysRolePermission> setUser = user.getSysRole().getSysRolePermissions();
		List<SysRolePermission> perm = sysRolePermissionService.findBySysRoleOrderBySysMenuClass(user.getSysRole());
		
		List<SysMenu> listMenu = new ArrayList<SysMenu>();
		for (SysRolePermission o : perm) {
			listMenu.add(o.getSysMenu());
		}
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

		return "failed";
	}

}
