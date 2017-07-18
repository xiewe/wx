package com.framework.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.AppConstants;
import com.framework.SysErrorCode;
import com.framework.entity.GeneralResponseData;
import com.framework.entity.SysOrganization;
import com.framework.entity.SysRole;
import com.framework.entity.SysUser;
import com.framework.log4jdbc.Log;
import com.framework.log4jdbc.LogLevel;
import com.framework.service.SysOrganizationService;
import com.framework.service.SysRoleService;
import com.framework.service.SysUserService;
import com.framework.shiro.ShiroRealm;
import com.framework.utils.pager.DynamicSpecifications;
import com.framework.utils.pager.Pager;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysOrganizationService sysOrganizationService;

    @Autowired
    private ShiroRealm shiroRealm;

    ObjectMapper mapper = new ObjectMapper();
    private static final String CREATE = "sys/user/create";
    private static final String UPDATE = "sys/user/update";
    private static final String LIST = "sys/user/list";
    private static final String VIEW = "sys/user/view";

    @RequiresPermissions("SysUser:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String preCreate(Map<String, Object> map) {
        List<SysRole> roles = sysRoleService.findAll();
        map.put("roles", roles);

        List<SysOrganization> orgs = sysOrganizationService.findAll();
        map.put("orgs", orgs);

        return CREATE;
    }

    @Log(message = "添加了{0}用户。", level = LogLevel.INFO)
    @RequiresPermissions("SysUser:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody String create(@Valid SysUser user) throws JsonProcessingException {
        GeneralResponseData<SysUser> ret = new GeneralResponseData<SysUser>();

        SysUser tmp = sysUserService.findByUsername(user.getUsername());
        if (tmp != null) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.USER_NAME_DUPLICATE);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.USER_NAME_DUPLICATE));

            return mapper.writeValueAsString(ret);
        }

        if (user.getSysOrganization().getId() == null) {
            user.setSysOrganization(null);
        }
        if (user.getSysRole().getId() == null) {
            user.setSysRole(null);
        }
        user.setStatus(0);
        user.setCreateTime(new Date());
        user.setPlainPassword(user.getPassword());
        SysUser o = sysUserService.saveOrUpdate(user);
        if (o == null) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(o);
            setLogObject(new Object[] { user.getUsername() });
        }

        return mapper.writeValueAsString(ret);
    }

    @Log(message = "删除了{0}用户。", level = LogLevel.INFO)
    @RequiresPermissions("SysUser:delete")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public @ResponseBody String delete(@PathVariable Long id) throws JsonProcessingException {
        GeneralResponseData<String> ret = new GeneralResponseData<String>();
        if (id == 1) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.ADMIN_CANNOT_DELETE);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.ADMIN_CANNOT_DELETE));
            return mapper.writeValueAsString(ret);
        }

        sysUserService.delete(id);
        shiroRealm.clearAllCachedAuthorizationInfo();
        ret.setStatus(AppConstants.SUCCESS);
        setLogObject(new Object[] { id });
        return mapper.writeValueAsString(ret);
    }

    @ModelAttribute("preload")
    public SysUser preload(@RequestParam(value = "id", required = false) Long id) {
        if (id != null) {
            SysUser user = sysUserService.get(id);
            return user;
        }
        return null;
    }

    @RequiresPermissions("SysUser:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String preUpdate(@PathVariable Long id, Map<String, Object> map) {
        SysUser user = sysUserService.get(id);
        map.put("user", user);
        List<SysRole> roles = sysRoleService.findAll();
        map.put("roles", roles);

        List<SysOrganization> orgs = sysOrganizationService.findAll();
        map.put("orgs", orgs);
        return UPDATE;
    }

    @Log(message = "修改了{0}用户的信息。", level = LogLevel.INFO)
    @RequiresPermissions("SysUser:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody String update(@Valid @ModelAttribute("preload") SysUser user) throws JsonProcessingException {
        GeneralResponseData<SysUser> ret = new GeneralResponseData<SysUser>();
        if (user.getId() == 1) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.ADMIN_CANNOT_UPDATE);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.ADMIN_CANNOT_UPDATE));
            return mapper.writeValueAsString(ret);
        }

        SysUser tmp = sysUserService.findByUsername(user.getUsername());
        if (tmp != null && tmp.getId() != user.getId()) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.USER_NAME_DUPLICATE);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.USER_NAME_DUPLICATE));

            return mapper.writeValueAsString(ret);
        }

        if (user.getSysOrganization().getId() == null) {
            user.setSysOrganization(null);
        }
        if (user.getSysRole().getId() == null) {
            user.setSysRole(null);
        }
        user.setModifyTime(new Date());
        SysUser o = sysUserService.saveOrUpdate(user);
        if (o == null) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(o);
            setLogObject(new Object[] { user.getUsername() });
        }

        shiroRealm.clearAllCachedAuthorizationInfo();

        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions(value = { "SysUser:view", "SysUser:create", "SysUser:update", "SysUser:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
    public String list(ServletRequest request, Pager pager, Map<String, Object> map) {
        Specification<SysUser> specification = DynamicSpecifications.buildSpecification(request, SysUser.class);
        List<SysUser> users = sysUserService.findByPageable(specification, pager);
        map.put("pager", pager);
        map.put("users", users);

        List<SysRole> roles = sysRoleService.findAll();
        map.put("roles", roles);

        List<SysOrganization> orgs = sysOrganizationService.findAll();
        map.put("orgs", orgs);

        return LIST;
    }

    @RequiresPermissions(value = { "SysUser:view", "SysUser:create", "SysUser:update", "SysUser:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/view/{id}", method = { RequestMethod.GET })
    public String view(@PathVariable Long id, Map<String, Object> map) {
        SysUser user = sysUserService.get(id);

        map.put("user", user);
        return VIEW;
    }

    @Log(message = "{0}用户{1}", level = LogLevel.INFO)
    @RequiresPermissions(value = { "SysUser:update" }, logical = Logical.OR)
    @RequestMapping(value = "/reset/{type}/{userId}", method = RequestMethod.POST)
    public @ResponseBody String reset(@PathVariable String type, @PathVariable Long userId) {
        SysUser user = sysUserService.get(userId);

        String msg = "";
        if (type.equals("password")) {
            sysUserService.resetPwd(user, "123456");
            msg = "重置密码成功";
        } else if (type.equals("status")) {
            if (user.getStatus() == AppConstants.USER_STATUS_ENABLED) {
                user.setStatus(AppConstants.USER_STATUS_DISABLED);
                msg = "disabled";
            } else {
                user.setStatus(AppConstants.USER_STATUS_ENABLED);
                msg = "enabled";
            }

            sysUserService.saveOrUpdate(user);
            // reload permission
            shiroRealm.clearAllCachedAuthenticationCacheInfo();
            setLogObject(new Object[] { user.getUid() + "-" + user.getUsername(), msg });
        }
        return "success";
    }

}
