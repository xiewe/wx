package com.uc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.AppConstants;
import com.framework.SysErrorCode;
import com.framework.controller.BaseController;
import com.framework.entity.GeneralResponseData;
import com.framework.log4jdbc.Log;
import com.framework.log4jdbc.LogLevel;
import com.framework.utils.pager.DynamicSpecifications;
import com.framework.utils.pager.Pager;
import com.framework.utils.pager.SearchFilter;
import com.uc.entity.Organization;
import com.uc.entity.UserStatusInfo;
import com.uc.service.OrganizationService;
import com.uc.service.UserStatusInfoService;

@Controller
@RequestMapping("/userstatus")
public class UserStatusController extends BaseController {

    @Autowired
    private UserStatusInfoService userStatusInfoService;

    @Autowired
    private OrganizationService organizationService;

    ObjectMapper mapper = new ObjectMapper();
    private static final String CREATE = "biz/mgruser/userstatus/create";
    private static final String UPDATE = "biz/mgruser/userstatus/update";
    private static final String LIST = "biz/mgruser/userstatus/list";
    private static final String VIEW = "biz/mgruser/userstatus/view";

    @RequiresPermissions("UserStatusInfo:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String preCreate(Map<String, Object> map) {
        List<Organization> resorgs = organizationService.findAll();
        map.put("resorgs", resorgs);

        return CREATE;
    }

    @Log(message = "添加了用户状态:{0}", level = LogLevel.INFO, catrgory = "uc")
    @RequiresPermissions("UserStatusInfo:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody String create(@Valid UserStatusInfo userstatusinfo) throws JsonProcessingException {
        GeneralResponseData<UserStatusInfo> ret = new GeneralResponseData<UserStatusInfo>();

        // hessian call
        userstatusinfo.setCreateTime(new Date());
        Boolean b = userStatusInfoService.add(userstatusinfo);
        if (!b) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(userstatusinfo);
            setLogObject(new Object[] { userstatusinfo.getSubNo() });
        }

        return mapper.writeValueAsString(ret);
    }

    @Log(message = "删除了用户状态:{0}", level = LogLevel.INFO, catrgory = "uc")
    @RequiresPermissions("UserStatusInfo:delete")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public @ResponseBody String delete(@PathVariable double id) throws JsonProcessingException {
        GeneralResponseData<String> ret = new GeneralResponseData<String>();

        // hessian call
        userStatusInfoService.delete(id);
        ret.setStatus(AppConstants.SUCCESS);
        setLogObject(new Object[] { id });
        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions("UserStatusInfo:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String preUpdate(@PathVariable Integer id, Map<String, Object> map) {
        UserStatusInfo userstatusinfo = userStatusInfoService.findOne(id);
        map.put("userstatusinfo", userstatusinfo);
        return UPDATE;
    }

    @Log(message = "修改了用户状态:{0}的信息", level = LogLevel.INFO, catrgory = "uc")
    @RequiresPermissions("UserStatusInfo:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody String update(@Valid UserStatusInfo userstatusinfo) throws JsonProcessingException {
        GeneralResponseData<UserStatusInfo> ret = new GeneralResponseData<UserStatusInfo>();

        // hessian call
        UserStatusInfo tmp = userStatusInfoService.findOne(Double.valueOf(userstatusinfo.getSubNo()));
        userstatusinfo.setCreateTime(tmp.getCreateTime());
        userstatusinfo.setModifyTime(new Date());

        Boolean b = userStatusInfoService.update(userstatusinfo);
        if (!b) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(userstatusinfo);
            setLogObject(new Object[] { userstatusinfo.getSubNo() });
        }

        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions(value = { "UserStatusInfo:view", "UserStatusInfo:create", "UserStatusInfo:update",
            "UserStatusInfo:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
    public String list(ServletRequest request, Pager pager, Map<String, Object> map) {
        List<UserStatusInfo> userstatusinfos = new ArrayList<UserStatusInfo>();
        long count = 0L;
        SearchFilter filter = DynamicSpecifications.genSearchFilter(request);
        if (filter != null && filter.getRules() != null && filter.getRules().size() > 0) {
            // hessian call

        } else {
            userstatusinfos = userStatusInfoService.findByPage(pager.getPageSize(), pager.getCurrPage());
            count = userStatusInfoService.findCount();
        }

        pager.setTotalCount(count);
        map.put("pager", pager);
        map.put("userstatusinfos", userstatusinfos);
        List<Organization> resorgs = organizationService.findAll();
        map.put("resorgs", resorgs);

        return LIST;
    }

    @RequiresPermissions(value = { "UserStatusInfo:view", "UserStatusInfo:create", "UserStatusInfo:update",
            "UserStatusInfo:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/view/{id}", method = { RequestMethod.GET })
    public String view(@PathVariable Integer id, Map<String, Object> map) {
        UserStatusInfo userstatusinfo = userStatusInfoService.findOne(id);
        map.put("userstatusinfo", userstatusinfo);
        return VIEW;
    }
}
