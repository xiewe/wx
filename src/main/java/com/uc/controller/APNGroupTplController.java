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
import com.uc.entity.APNGroupTpl;
import com.uc.service.APNGroupTplService;

@Controller
@RequestMapping("/apngroup")
public class APNGroupTplController extends BaseController {

    @Autowired
    private APNGroupTplService aPNGroupTplService;

    ObjectMapper mapper = new ObjectMapper();
    private static final String CREATE = "biz/mgrtpl/apngroup/create";
    private static final String UPDATE = "biz/mgrtpl/apngroup/update";
    private static final String LIST = "biz/mgrtpl/apngroup/list";
    private static final String VIEW = "biz/mgrtpl/apngroup/view";

    @RequiresPermissions("APNGroupTpl:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String preCreate(Map<String, Object> map) {
        return CREATE;
    }

    @Log(message = "添加了OP模板:{0}", level = LogLevel.INFO)
    @RequiresPermissions("APNGroupTpl:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody String create(@Valid APNGroupTpl apngrouptpl) throws JsonProcessingException {
        GeneralResponseData<APNGroupTpl> ret = new GeneralResponseData<APNGroupTpl>();

        if (apngrouptpl.getApnGroupId() <= 0 || apngrouptpl.getApnGroupName() == null
                || StringUtils.isEmpty(apngrouptpl.getApnIdList())) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.DATA_INVALID);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.DATA_INVALID));

            return mapper.writeValueAsString(ret);
        }

        // hessian call
        apngrouptpl.setCreateTime(new Date());
        Boolean b = aPNGroupTplService.add(apngrouptpl);
        if (!b) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(apngrouptpl);
            setLogObject(new Object[] { apngrouptpl.getApnGroupName() });
        }

        return mapper.writeValueAsString(ret);
    }

    @Log(message = "删除了OP模板:{0}", level = LogLevel.INFO)
    @RequiresPermissions("APNGroupTpl:delete")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public @ResponseBody String delete(@PathVariable double id) throws JsonProcessingException {
        GeneralResponseData<String> ret = new GeneralResponseData<String>();

        // hessian call
        aPNGroupTplService.delete(id);
        ret.setStatus(AppConstants.SUCCESS);
        setLogObject(new Object[] { id });
        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions("APNGroupTpl:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String preUpdate(@PathVariable Integer id, Map<String, Object> map) {
        APNGroupTpl apngrouptpl = aPNGroupTplService.findOne(id);
        map.put("apngrouptpl", apngrouptpl);
        return UPDATE;
    }

    @Log(message = "修改了OP模板:{0}的信息", level = LogLevel.INFO)
    @RequiresPermissions("APNGroupTpl:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody String update(@Valid APNGroupTpl apngrouptpl) throws JsonProcessingException {
        GeneralResponseData<APNGroupTpl> ret = new GeneralResponseData<APNGroupTpl>();

        if (apngrouptpl.getApnGroupId() <= 0 || apngrouptpl.getApnGroupName() == null
                || StringUtils.isEmpty(apngrouptpl.getApnIdList())) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.DATA_INVALID);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.DATA_INVALID));

            return mapper.writeValueAsString(ret);
        }

        // hessian call
        APNGroupTpl tmp = aPNGroupTplService.findOne(apngrouptpl.getApnGroupId());
        apngrouptpl.setCreateTime(tmp.getCreateTime());
        apngrouptpl.setModifyTime(new Date());

        Boolean b = aPNGroupTplService.update(apngrouptpl);
        if (!b) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(apngrouptpl);
            setLogObject(new Object[] { apngrouptpl.getApnGroupName() });
        }

        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions(value = { "APNGroupTpl:view", "APNGroupTpl:create", "APNGroupTpl:update", "APNGroupTpl:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
    public String list(ServletRequest request, Pager pager, Map<String, Object> map) {
        List<APNGroupTpl> apngrouptpls = new ArrayList<APNGroupTpl>();
        SearchFilter filter = DynamicSpecifications.genSearchFilter(request);
        if (filter != null && filter.getRules() != null && filter.getRules().size() > 0) {
            // hessian call
        } else {
            apngrouptpls = aPNGroupTplService.findByPage(pager.getPageSize(), pager.getCurrPage());
        }

        Long count = aPNGroupTplService.findCount();
        pager.setTotalCount(count);
        map.put("pager", pager);
        map.put("apngrouptpls", apngrouptpls);

        return LIST;
    }

    @RequiresPermissions(value = { "APNGroupTpl:view", "APNGroupTpl:create", "APNGroupTpl:update", "APNGroupTpl:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/view/{id}", method = { RequestMethod.GET })
    public String view(@PathVariable Integer id, Map<String, Object> map) {
        APNGroupTpl apngrouptpl = aPNGroupTplService.findOne(id);
        map.put("apngrouptpl", apngrouptpl);
        return VIEW;
    }
}
