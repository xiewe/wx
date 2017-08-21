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
import com.uc.entity.UserAccount;
import com.uc.service.OrganizationService;
import com.uc.service.UserAccountService;

@Controller
@RequestMapping("/openaccount")
public class OpenAccountController extends BaseController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private OrganizationService organizationService;

    ObjectMapper mapper = new ObjectMapper();
    private static final String CREATE = "biz/mgruser/openaccount/create";
    private static final String UPDATE = "biz/mgruser/openaccount/update";
    private static final String LIST = "biz/mgruser/openaccount/list";
    private static final String VIEW = "biz/mgruser/openaccount/view";

    @RequiresPermissions("UserAccount:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String preCreate(Map<String, Object> map) {
        return CREATE;
    }

    @Log(message = "添加了用户:{0}", level = LogLevel.INFO, catrgory = "uc")
    @RequiresPermissions("UserAccount:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody String create(@Valid UserAccount useraccount) throws JsonProcessingException {
        GeneralResponseData<UserAccount> ret = new GeneralResponseData<UserAccount>();

        // hessian call
        useraccount.setCreateTime(new Date());
        Boolean b = userAccountService.add(useraccount);
        if (!b) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(useraccount);
            setLogObject(new Object[] { useraccount.getSubName() });
        }

        return mapper.writeValueAsString(ret);
    }

    @Log(message = "删除了用户:{0}", level = LogLevel.INFO, catrgory = "uc")
    @RequiresPermissions("UserAccount:delete")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public @ResponseBody String delete(@PathVariable double id) throws JsonProcessingException {
        GeneralResponseData<String> ret = new GeneralResponseData<String>();

        // hessian call
        userAccountService.delete(id);
        ret.setStatus(AppConstants.SUCCESS);
        setLogObject(new Object[] { id });
        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions("UserAccount:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String preUpdate(@PathVariable Integer id, Map<String, Object> map) {
        UserAccount useraccount = userAccountService.findOne(id);
        map.put("useraccount", useraccount);
        return UPDATE;
    }

    @Log(message = "修改了用户:{0}的信息", level = LogLevel.INFO, catrgory = "uc")
    @RequiresPermissions("UserAccount:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody String update(@Valid UserAccount useraccount) throws JsonProcessingException {
        GeneralResponseData<UserAccount> ret = new GeneralResponseData<UserAccount>();

        // hessian call
        UserAccount tmp = userAccountService.findOne(useraccount.getOpId());
        useraccount.setCreateTime(tmp.getCreateTime());
        useraccount.setModifyTime(new Date());

        Boolean b = userAccountService.update(useraccount);
        if (!b) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(useraccount);
            setLogObject(new Object[] { useraccount.getSubName() });
        }

        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions("UserAccount:create")
    @RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
    public String list(ServletRequest request, Pager pager, Map<String, Object> map) {
        List<Organization> resorgs = organizationService.findAll();
        map.put("resorgs", resorgs);
        
        map.put("pager", pager);

        return LIST;
    }

    @RequiresPermissions(value = { "UserAccount:view", "UserAccount:create", "UserAccount:update", "UserAccount:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/view/{id}", method = { RequestMethod.GET })
    public String view(@PathVariable Integer id, Map<String, Object> map) {
        UserAccount useraccount = userAccountService.findOne(id);
        map.put("useraccount", useraccount);
        return VIEW;
    }
}
