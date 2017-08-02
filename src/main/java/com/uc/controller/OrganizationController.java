package com.uc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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
import com.uc.service.OrganizationService;

@Controller
@RequestMapping("/resorg")
public class OrganizationController extends BaseController {

    @Autowired
    private OrganizationService organizationService;

    ObjectMapper mapper = new ObjectMapper();
    private static final String CREATE = "biz/mgrres/resorg/create";
    private static final String UPDATE = "biz/mgrres/resorg/update";
    private static final String LIST = "biz/mgrres/resorg/list";
    private static final String VIEW = "biz/mgrres/resorg/view";

    @RequiresPermissions("Organization:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String preCreate(Map<String, Object> map) {
        return CREATE;
    }

    @Log(message = "添加了组织:{0}", level = LogLevel.INFO, catrgory = "uc")
    @RequiresPermissions("Organization:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody String create(@Valid Organization organization) throws JsonProcessingException {
        GeneralResponseData<Organization> ret = new GeneralResponseData<Organization>();

        if (StringUtils.isEmpty(organization.getOrgName())) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.DATA_INVALID);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.DATA_INVALID));

            return mapper.writeValueAsString(ret);
        }

        // hessian call
        organization.setCreateTime(new Date());
        Boolean b = organizationService.add(organization);
        if (!b) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(organization);
            setLogObject(new Object[] { organization.getOrgName() });
        }

        return mapper.writeValueAsString(ret);
    }

    @Log(message = "删除了组织:{0}", level = LogLevel.INFO, catrgory = "uc")
    @RequiresPermissions("Organization:delete")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public @ResponseBody String delete(@PathVariable double id) throws JsonProcessingException {
        GeneralResponseData<String> ret = new GeneralResponseData<String>();

        // hessian call
        organizationService.delete(id);
        ret.setStatus(AppConstants.SUCCESS);
        setLogObject(new Object[] { id });
        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions("Organization:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String preUpdate(@PathVariable Integer id, Map<String, Object> map) {
        Organization organization = organizationService.findOne(id);
        map.put("organization", organization);
        return UPDATE;
    }

    @Log(message = "修改了组织:{0}的信息", level = LogLevel.INFO, catrgory = "uc")
    @RequiresPermissions("Organization:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody String update(@Valid Organization organization) throws JsonProcessingException {
        GeneralResponseData<Organization> ret = new GeneralResponseData<Organization>();

        if (StringUtils.isEmpty(organization.getOrgName())) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.DATA_INVALID);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.DATA_INVALID));

            return mapper.writeValueAsString(ret);
        }

        // hessian call
        Organization tmp = organizationService.findOne(organization.getOrgId());
        organization.setCreateTime(tmp.getCreateTime());
        organization.setModifyTime(new Date());

        Boolean b = organizationService.update(organization);
        if (!b) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(organization);
            setLogObject(new Object[] { organization.getOrgName() });
        }

        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions(value = { "Organization:view", "Organization:create", "Organization:update",
            "Organization:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
    public String list(ServletRequest request, Pager pager, Map<String, Object> map) {
        List<Organization> organizations = new ArrayList<Organization>();
        SearchFilter filter = DynamicSpecifications.genSearchFilter(request);
        if (filter != null && filter.getRules() != null && filter.getRules().size() > 0) {
            // hessian call
        } else {
            organizations = organizationService.findByPage(pager.getPageSize(), pager.getCurrPage());
        }

        Long count = organizationService.findCount();
        pager.setTotalCount(count);
        map.put("pager", pager);
        map.put("organizations", organizations);

        return LIST;
    }

    @RequiresPermissions(value = { "Organization:view", "Organization:create", "Organization:update",
            "Organization:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/view/{id}", method = { RequestMethod.GET })
    public String view(@PathVariable Integer id, Map<String, Object> map) {
        Organization organization = organizationService.findOne(id);
        map.put("organization", organization);
        return VIEW;
    }
}
