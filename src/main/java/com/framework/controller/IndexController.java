package com.framework.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.AppConstants;
import com.framework.SysErrorCode;
import com.framework.entity.GeneralResponseData;
import com.framework.entity.SysMenu;
import com.framework.entity.SysOrganization;
import com.framework.entity.SysRole;
import com.framework.entity.SysUser;
import com.framework.exception.ServiceException;
import com.framework.log4jdbc.Log;
import com.framework.log4jdbc.LogLevel;
import com.framework.service.SysMenuService;
import com.framework.service.SysOrganizationService;
import com.framework.service.SysRoleService;
import com.framework.service.SysUserService;
import com.framework.utils.PropertiesUtil;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysOrganizationService sysOrganizationService;

    ObjectMapper mapper = new ObjectMapper();
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
        map.put("date", new Date().getTime());
        map.put("inoms", PropertiesUtil.getInstance().getKeyValue("inoms"));

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
            String rPassword) throws JsonProcessingException {
        GeneralResponseData<SysUser> ret = new GeneralResponseData<SysUser>();
        SysUser loginUser = (SysUser) org.apache.shiro.SecurityUtils.getSubject().getPrincipal();

        if (newPassword != null && newPassword.equals(rPassword)) {
            loginUser.setPlainPassword(plainPassword);
            try {
                sysUserService.updatePwd(loginUser, newPassword);
                setLogObject(new Object[] { loginUser.getUsername() });
                ret.setStatus(AppConstants.SUCCESS);
                ret.setData(null);
                setLogObject(new Object[] { loginUser.getUid() + "-" + loginUser.getUsername() });
            } catch (ServiceException e) {
                ret.setStatus(AppConstants.FAILED);
                ret.setErrCode(SysErrorCode.USER_PASSWORD_ERROR);
                ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.USER_PASSWORD_ERROR));
            }
        } else {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        }

        return mapper.writeValueAsString(ret);
    }

    @RequiresUser
    @RequestMapping(value = "/updateBase", method = RequestMethod.GET)
    public String preUpdateBase(Map<String, Object> map) {
        SysUser user = (SysUser) org.apache.shiro.SecurityUtils.getSubject().getPrincipal();
        map.put(AppConstants.LOGIN_USER, user);
        map.put("user", user);
        List<SysRole> roles = sysRoleService.findAll();
        map.put("roles", roles);

        List<SysOrganization> orgs = sysOrganizationService.findAll();
        map.put("orgs", orgs);
        return UPDATE_BASE;
    }

    @Log(message = "{0}修改详细信息成功！", level = LogLevel.DEBUG)
    @RequiresUser
    @RequestMapping(value = "/updateBase", method = RequestMethod.POST)
    public @ResponseBody String updateBase(SysUser user, ServletRequest request) throws JsonProcessingException {
        GeneralResponseData<SysUser> ret = new GeneralResponseData<SysUser>();
        SysUser loginUser = (SysUser) org.apache.shiro.SecurityUtils.getSubject().getPrincipal();

        loginUser.setPhone(user.getPhone());
        loginUser.setEmail(user.getEmail());

        if (user.getSysRole().getId() == null) {
            loginUser.setSysRole(null);
        }
        if (user.getSysOrganization().getId() == null) {
            loginUser.setSysOrganization(null);
        }

        SysUser o = sysUserService.saveOrUpdate(loginUser);
        if (o == null) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(o);
            setLogObject(new Object[] { user.getUid() + "-" + user.getUsername() });
        }

        return mapper.writeValueAsString(ret);
    }
}
