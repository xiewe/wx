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
import com.uc.entity.APNTpl;
import com.uc.service.APNTplService;

@Controller
@RequestMapping("/apn")
public class APNTplController extends BaseController {

    @Autowired
    private APNTplService aPNTplService;

    ObjectMapper mapper = new ObjectMapper();
    private static final String CREATE = "biz/mgrtpl/apn/create";
    private static final String UPDATE = "biz/mgrtpl/apn/update";
    private static final String LIST = "biz/mgrtpl/apn/list";
    private static final String VIEW = "biz/mgrtpl/apn/view";

    @RequiresPermissions("APNTpl:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String preCreate(Map<String, Object> map) {
        return CREATE;
    }

    @Log(message = "添加了APN模板:{0}", level = LogLevel.INFO)
    @RequiresPermissions("APNTpl:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody String create(@Valid APNTpl apntpl) throws JsonProcessingException {
        GeneralResponseData<APNTpl> ret = new GeneralResponseData<APNTpl>();

        if (apntpl.getApnId() <= 0 || apntpl.getOi() == null || apntpl.getNi() == null) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.DATA_INVALID);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.DATA_INVALID));

            return mapper.writeValueAsString(ret);
        }

        // hessian call
        apntpl.setCreateTime(new Date());
        Boolean b = aPNTplService.add(apntpl);
        if (!b) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(apntpl);
            setLogObject(new Object[] { apntpl.getApnId() + "-" + apntpl.getOi() + "-" + apntpl.getNi() });
        }

        return mapper.writeValueAsString(ret);
    }

    @Log(message = "删除了APN模板:{0}", level = LogLevel.INFO)
    @RequiresPermissions("APNTpl:delete")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public @ResponseBody String delete(@PathVariable double id) throws JsonProcessingException {
        GeneralResponseData<String> ret = new GeneralResponseData<String>();

        // hessian call
        aPNTplService.delete(id);
        ret.setStatus(AppConstants.SUCCESS);
        setLogObject(new Object[] { id });
        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions("APNTpl:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String preUpdate(@PathVariable Integer id, Map<String, Object> map) {
        APNTpl apntpl = aPNTplService.findOne(id);
        map.put("apntpl", apntpl);
        return UPDATE;
    }

    @Log(message = "修改了APN模板:{0}的信息", level = LogLevel.INFO)
    @RequiresPermissions("APNTpl:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody String update(@Valid APNTpl apntpl) throws JsonProcessingException {
        GeneralResponseData<APNTpl> ret = new GeneralResponseData<APNTpl>();

        if (apntpl.getApnId() <= 0 || apntpl.getOi() == null || apntpl.getNi() == null) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.DATA_INVALID);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.DATA_INVALID));

            return mapper.writeValueAsString(ret);
        }

        // hessian call
        APNTpl tmp = aPNTplService.findOne(apntpl.getApnId());
        apntpl.setCreateTime(tmp.getCreateTime());
        apntpl.setModifyTime(new Date());

        Boolean b = aPNTplService.update(apntpl);
        if (!b) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(apntpl);
            setLogObject(new Object[] { apntpl.getApnId() + "-" + apntpl.getOi() + "-" + apntpl.getNi() });
        }

        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions(value = { "APNTpl:view", "APNTpl:create", "APNTpl:update", "APNTpl:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
    public String list(ServletRequest request, Pager pager, Map<String, Object> map) {
        List<APNTpl> apntpls = new ArrayList<APNTpl>();
        SearchFilter filter = DynamicSpecifications.genSearchFilter(request);
        if (filter != null && filter.getRules() != null && filter.getRules().size() > 0) {
            // hessian call
        } else {
            apntpls = aPNTplService.findByPage(pager.getPageSize(), pager.getCurrPage());
        }

        Long count = aPNTplService.findCount();
        pager.setTotalCount(count);
        map.put("pager", pager);
        map.put("apntpls", apntpls);

        return LIST;
    }

    @RequiresPermissions(value = { "APNTpl:view", "APNTpl:create", "APNTpl:update", "APNTpl:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/view/{id}", method = { RequestMethod.GET })
    public String view(@PathVariable Integer id, Map<String, Object> map) {
        APNTpl apntpl = aPNTplService.findOne(id);
        map.put("apntpl", apntpl);
        return VIEW;
    }
}
