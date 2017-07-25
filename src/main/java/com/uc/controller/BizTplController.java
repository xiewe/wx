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
import com.uc.entity.BizTpl;
import com.uc.service.BizTplService;

@Controller
@RequestMapping("/biz")
public class BizTplController extends BaseController {

    @Autowired
    private BizTplService bizTplService;

    ObjectMapper mapper = new ObjectMapper();
    private static final String CREATE = "biz/mgrtpl/biz/create";
    private static final String UPDATE = "biz/mgrtpl/biz/update";
    private static final String LIST = "biz/mgrtpl/biz/list";
    private static final String VIEW = "biz/mgrtpl/biz/view";

    @RequiresPermissions("BizTpl:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String preCreate(Map<String, Object> map) {
        return CREATE;
    }

    @Log(message = "添加了业务模板:{0}", level = LogLevel.INFO)
    @RequiresPermissions("BizTpl:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody String create(@Valid BizTpl biztpl) throws JsonProcessingException {
        GeneralResponseData<BizTpl> ret = new GeneralResponseData<BizTpl>();

        if (biztpl.getBizTplId() <= 0 || StringUtils.isEmpty(biztpl.getBizTplName())
                || StringUtils.isEmpty(biztpl.getBizIdList())) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.DATA_INVALID);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.DATA_INVALID));

            return mapper.writeValueAsString(ret);
        }

        // hessian call
        biztpl.setCreateTime(new Date());
        Boolean b = bizTplService.add(biztpl);
        if (!b) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(biztpl);
            setLogObject(new Object[] { biztpl.getBizTplName() });
        }

        return mapper.writeValueAsString(ret);
    }

    @Log(message = "删除了业务模板:{0}", level = LogLevel.INFO)
    @RequiresPermissions("BizTpl:delete")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public @ResponseBody String delete(@PathVariable double id) throws JsonProcessingException {
        GeneralResponseData<String> ret = new GeneralResponseData<String>();

        // hessian call
        bizTplService.delete(id);
        ret.setStatus(AppConstants.SUCCESS);
        setLogObject(new Object[] { id });
        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions("BizTpl:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String preUpdate(@PathVariable Integer id, Map<String, Object> map) {
        BizTpl biztpl = bizTplService.findOne(id);
        map.put("biztpl", biztpl);
        return UPDATE;
    }

    @Log(message = "修改了业务模板:{0}的信息", level = LogLevel.INFO)
    @RequiresPermissions("BizTpl:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody String update(@Valid BizTpl biztpl) throws JsonProcessingException {
        GeneralResponseData<BizTpl> ret = new GeneralResponseData<BizTpl>();

        if (biztpl.getBizTplId() <= 0 || StringUtils.isEmpty(biztpl.getBizTplName())
                || StringUtils.isEmpty(biztpl.getBizIdList())) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.DATA_INVALID);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.DATA_INVALID));

            return mapper.writeValueAsString(ret);
        }

        // hessian call
        BizTpl tmp = bizTplService.findOne(biztpl.getBizTplId());
        biztpl.setCreateTime(tmp.getCreateTime());
        biztpl.setModifyTime(new Date());

        Boolean b = bizTplService.update(biztpl);
        if (!b) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(biztpl);
            setLogObject(new Object[] { biztpl.getBizTplName() });
        }

        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions(value = { "BizTpl:view", "BizTpl:create", "BizTpl:update", "BizTpl:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
    public String list(ServletRequest request, Pager pager, Map<String, Object> map) {
        List<BizTpl> biztpls = new ArrayList<BizTpl>();
        SearchFilter filter = DynamicSpecifications.genSearchFilter(request);
        if (filter != null && filter.getRules() != null && filter.getRules().size() > 0) {
            // hessian call
        } else {
            biztpls = bizTplService.findByPage(pager.getPageSize(), pager.getCurrPage());
        }

        Long count = bizTplService.findCount();
        pager.setTotalCount(count);
        map.put("pager", pager);
        map.put("biztpls", biztpls);

        return LIST;
    }

    @RequiresPermissions(value = { "BizTpl:view", "BizTpl:create", "BizTpl:update", "BizTpl:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/view/{id}", method = { RequestMethod.GET })
    public String view(@PathVariable Integer id, Map<String, Object> map) {
        BizTpl biztpl = bizTplService.findOne(id);
        map.put("biztpl", biztpl);
        return VIEW;
    }
}
