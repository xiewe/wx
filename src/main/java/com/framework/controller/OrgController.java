package com.framework.controller;

import java.util.ArrayList;
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
import com.framework.log4jdbc.Log;
import com.framework.log4jdbc.LogLevel;
import com.framework.service.SysOrganizationService;
import com.framework.utils.pager.Dictionary.OperatorEum;
import com.framework.utils.pager.DynamicSpecifications;
import com.framework.utils.pager.Pager;
import com.framework.utils.pager.Rule;
import com.framework.utils.pager.SearchFilter;

@Controller
@RequestMapping("/org")
public class OrgController extends BaseController {

    @Autowired
    private SysOrganizationService sysOrganizationService;

    ObjectMapper mapper = new ObjectMapper();
    private static final String CREATE = "sys/org/create";
    private static final String UPDATE = "sys/org/update";
    private static final String LIST = "sys/org/list";
    private static final String VIEW = "sys/org/view";

    @RequiresPermissions("SysOrganization:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String preCreate(Map<String, Object> map) {
        List<SysOrganization> orgs = sysOrganizationService.findAll();
        map.put("orgs", orgs);

        return CREATE;
    }

    @Log(message = "添加了{0}组织。", level = LogLevel.INFO)
    @RequiresPermissions("SysOrganization:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody String create(@Valid SysOrganization org) throws JsonProcessingException {
        GeneralResponseData<SysOrganization> ret = new GeneralResponseData<SysOrganization>();

        SysOrganization tmp = sysOrganizationService.findByName(org.getName());
        if (tmp != null) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.ORG_NAME_DUPLICATE);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.ORG_NAME_DUPLICATE));

            return mapper.writeValueAsString(ret);
        }

        org.setCreateTime(new Date());
        SysOrganization o = sysOrganizationService.saveOrUpdate(org);
        if (o == null) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(o);
            setLogObject(new Object[] { org.getName() });
        }

        return mapper.writeValueAsString(ret);
    }

    @Log(message = "删除了{0}组织。", level = LogLevel.INFO)
    @RequiresPermissions("SysOrganization:delete")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public @ResponseBody String delete(@PathVariable Integer id) throws JsonProcessingException {
        GeneralResponseData<String> ret = new GeneralResponseData<String>();
        sysOrganizationService.delete(id);
        ret.setStatus(AppConstants.SUCCESS);
        setLogObject(new Object[] { id });
        return mapper.writeValueAsString(ret);
    }

    // @ModelAttribute("preload")
    public SysOrganization preload(@RequestParam(value = "id", required = false) Integer id) {
        if (id != null) {
            SysOrganization org = sysOrganizationService.get(id);
            return org;
        }
        return null;
    }

    @RequiresPermissions("SysOrganization:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String preUpdate(@PathVariable Integer id, Map<String, Object> map) {
        SysOrganization org = sysOrganizationService.get(id);
        map.put("org", org);
        List<SysOrganization> orgs = sysOrganizationService.findAll();
        orgs.remove(org);
        map.put("orgs", orgs);
        return UPDATE;
    }

    @Log(message = "修改了{0}组织的信息。", level = LogLevel.INFO)
    @RequiresPermissions("SysOrganization:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody String update(@Valid SysOrganization org) // @ModelAttribute("preload")
            throws JsonProcessingException {
        GeneralResponseData<SysOrganization> ret = new GeneralResponseData<SysOrganization>();

        SysOrganization tmp = sysOrganizationService.findByName(org.getName());
        if (tmp != null && tmp.getId() != org.getId()) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.ORG_NAME_DUPLICATE);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.ORG_NAME_DUPLICATE));

            return mapper.writeValueAsString(ret);
        }

        org.setModifyTime(new Date());
        SysOrganization o = sysOrganizationService.saveOrUpdate(org);
        if (o == null) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(o);
            setLogObject(new Object[] { org.getName() });
        }

        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions(value = { "SysOrganization:view", "SysOrganization:create", "SysOrganization:update",
            "SysOrganization:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
    public String list(ServletRequest request, Pager pager, Map<String, Object> map) {
        Specification<SysOrganization> specification = DynamicSpecifications.buildSpecification(request,
                SysOrganization.class);
        List<SysOrganization> orgs = sysOrganizationService.findByPageable(specification, pager);

        List<Integer> ids = new ArrayList<Integer>();
        for (SysOrganization o : orgs) {
            if (o.getParentId() != null) {
                ids.add(o.getParentId());
            }
        }
        List<SysOrganization> pOrgs = sysOrganizationService.findByIdIn(ids);

        for (SysOrganization o : orgs) {
            if (o.getParentId() == null) {
                continue;
            }
            for (SysOrganization n : pOrgs) {
                if (o.getParentId() == n.getId()) {
                    o.setParentName(n.getName());
                    break;
                }
            }
        }

        map.put("pager", pager);
        map.put("orgs", orgs);

        return LIST;
    }

    @RequiresPermissions("SysOrganization:view")
    @RequestMapping(value = "/json", method = RequestMethod.GET)
    public @ResponseBody String json(@RequestParam(value = "filter", required = false) String filter)
            throws JsonProcessingException {
        GeneralResponseData<List<SysOrganization>> ret = new GeneralResponseData<List<SysOrganization>>();

        List<SysOrganization> list = new ArrayList<SysOrganization>();
        if (filter != null) {
            SearchFilter sf = new SearchFilter();
            Rule rule = new Rule();
            rule.setField("name");
            rule.setOperator(OperatorEum.LIKE);
            rule.setData(filter);
            sf.addRule(rule);
            Specification<SysOrganization> specification = DynamicSpecifications.buildSpecification(sf,
                    SysOrganization.class);

            list = sysOrganizationService.findAll(specification);
        } else {
            list = sysOrganizationService.findAll();
        }

        ret.setStatus(AppConstants.SUCCESS);
        ret.setData(list);

        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions(value = { "SysOrganization:view", "SysOrganization:create", "SysOrganization:update",
            "SysOrganization:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/view/{id}", method = { RequestMethod.GET })
    public String view(@PathVariable Integer id, Map<String, Object> map) {
        SysOrganization org = sysOrganizationService.get(id);
        if (org != null && org.getParentId() != null) {
            SysOrganization pOrg = sysOrganizationService.get(org.getParentId());
            org.setParentName(pOrg.getName());
        }
        map.put("org", org);
        return VIEW;
    }
}
