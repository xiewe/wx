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
import com.uc.entity.PhoneNoFInfo;
import com.uc.service.PhoneNoFService;

@Controller
@RequestMapping("/no")
public class PhoneNoFInfoController extends BaseController {

    @Autowired
    private PhoneNoFService phoneNoFService;

    ObjectMapper mapper = new ObjectMapper();
    private static final String CREATE = "biz/mgrres/no/create";
    private static final String UPDATE = "biz/mgrres/no/update";
    private static final String LIST = "biz/mgrres/no/list";
    private static final String VIEW = "biz/mgrres/no/view";

    @RequiresPermissions("PhoneNoFInfo:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String preCreate(Map<String, Object> map) {
        return CREATE;
    }

    @Log(message = "添加了号段:{0}", level = LogLevel.INFO)
    @RequiresPermissions("PhoneNoFInfo:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody String create(@Valid PhoneNoFInfo phonenofinfo) throws JsonProcessingException {
        GeneralResponseData<PhoneNoFInfo> ret = new GeneralResponseData<PhoneNoFInfo>();

        if (StringUtils.isEmpty(phonenofinfo.getPhoneNoStart()) || phonenofinfo.getNumbers() <= 0) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.DATA_INVALID);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.DATA_INVALID));

            return mapper.writeValueAsString(ret);
        }

        // hessian call
        phonenofinfo.setCreateTime(new Date());
        Boolean b = phoneNoFService.add(phonenofinfo);
        if (!b) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(phonenofinfo);
            setLogObject(new Object[] { phonenofinfo.getPhoneNoStart() });
        }

        return mapper.writeValueAsString(ret);
    }

    @Log(message = "删除了号段:{0}", level = LogLevel.INFO)
    @RequiresPermissions("PhoneNoFInfo:delete")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public @ResponseBody String delete(@PathVariable double id) throws JsonProcessingException {
        GeneralResponseData<String> ret = new GeneralResponseData<String>();

        // hessian call
        phoneNoFService.delete(id);
        ret.setStatus(AppConstants.SUCCESS);
        setLogObject(new Object[] { id });
        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions("PhoneNoFInfo:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String preUpdate(@PathVariable Integer id, Map<String, Object> map) {
        PhoneNoFInfo phonenofinfo = phoneNoFService.findOne(id);
        map.put("phonenofinfo", phonenofinfo);
        return UPDATE;
    }

    @Log(message = "修改了号段:{0}的信息", level = LogLevel.INFO)
    @RequiresPermissions("PhoneNoFInfo:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody String update(@Valid PhoneNoFInfo phonenofinfo) throws JsonProcessingException {
        GeneralResponseData<PhoneNoFInfo> ret = new GeneralResponseData<PhoneNoFInfo>();

        if (StringUtils.isEmpty(phonenofinfo.getPhoneNoStart()) || phonenofinfo.getNumbers() <= 0) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.DATA_INVALID);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.DATA_INVALID));

            return mapper.writeValueAsString(ret);
        }

        // hessian call
        PhoneNoFInfo tmp = phoneNoFService.findOne(phonenofinfo.getCreateTime().getTime());
        phonenofinfo.setCreateTime(tmp.getCreateTime());
        phonenofinfo.setModifyTime(new Date());

        Boolean b = phoneNoFService.update(phonenofinfo);
        if (!b) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(phonenofinfo);
            setLogObject(new Object[] { phonenofinfo.getPhoneNoStart() });
        }

        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions(value = { "PhoneNoFInfo:view", "PhoneNoFInfo:create", "PhoneNoFInfo:update",
            "PhoneNoFInfo:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
    public String list(ServletRequest request, Pager pager, Map<String, Object> map) {
        List<PhoneNoFInfo> phonenofinfos = new ArrayList<PhoneNoFInfo>();
        SearchFilter filter = DynamicSpecifications.genSearchFilter(request);
        if (filter != null && filter.getRules() != null && filter.getRules().size() > 0) {
            // hessian call
        } else {
            phonenofinfos = phoneNoFService.findByPage(pager.getPageSize(), pager.getCurrPage());
        }

        Long count = phoneNoFService.findCount();
        pager.setTotalCount(count);
        map.put("pager", pager);
        map.put("phonenofinfos", phonenofinfos);

        return LIST;
    }

    @RequiresPermissions(value = { "PhoneNoFInfo:view", "PhoneNoFInfo:create", "PhoneNoFInfo:update",
            "PhoneNoFInfo:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/view/{id}", method = { RequestMethod.GET })
    public String view(@PathVariable Integer id, Map<String, Object> map) {
        PhoneNoFInfo phonenofinfo = phoneNoFService.findOne(id);
        map.put("phonenofinfo", phonenofinfo);
        return VIEW;
    }
}
