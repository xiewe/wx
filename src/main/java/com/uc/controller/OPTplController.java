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
import com.uc.entity.OPTpl;
import com.uc.service.OPTplService;

@Controller
@RequestMapping("/op")
public class OPTplController extends BaseController {

    @Autowired
    private OPTplService oPTplService;

    ObjectMapper mapper = new ObjectMapper();
    private static final String CREATE = "biz/mgrtpl/op/create";
    private static final String UPDATE = "biz/mgrtpl/op/update";
    private static final String LIST = "biz/mgrtpl/op/list";
    private static final String VIEW = "biz/mgrtpl/op/view";

    @RequiresPermissions("OPTpl:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String preCreate(Map<String, Object> map) {
        return CREATE;
    }

    @Log(message = "添加了OP模板:{0}", level = LogLevel.INFO, catrgory = "uc")
    @RequiresPermissions("OPTpl:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody String create(@Valid OPTpl optpl) throws JsonProcessingException {
        GeneralResponseData<OPTpl> ret = new GeneralResponseData<OPTpl>();

        if (optpl.getOpId() <= 0 || optpl.getOpName() == null || optpl.getOpValue() == null) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.DATA_INVALID);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.DATA_INVALID));

            return mapper.writeValueAsString(ret);
        }

        // hessian call
        optpl.setCreateTime(new Date());
        Boolean b = oPTplService.add(optpl);
        if (!b) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(optpl);
            setLogObject(new Object[] { optpl.getOpName() });
        }

        return mapper.writeValueAsString(ret);
    }

    @Log(message = "删除了OP模板:{0}", level = LogLevel.INFO, catrgory = "uc")
    @RequiresPermissions("OPTpl:delete")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public @ResponseBody String delete(@PathVariable double id) throws JsonProcessingException {
        GeneralResponseData<String> ret = new GeneralResponseData<String>();

        // hessian call
        oPTplService.delete(id);
        ret.setStatus(AppConstants.SUCCESS);
        setLogObject(new Object[] { id });
        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions("OPTpl:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String preUpdate(@PathVariable Integer id, Map<String, Object> map) {
        OPTpl optpl = oPTplService.findOne(id);
        map.put("optpl", optpl);
        return UPDATE;
    }

    @Log(message = "修改了OP模板:{0}的信息", level = LogLevel.INFO, catrgory = "uc")
    @RequiresPermissions("OPTpl:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody String update(@Valid OPTpl optpl) throws JsonProcessingException {
        GeneralResponseData<OPTpl> ret = new GeneralResponseData<OPTpl>();

        if (optpl.getOpId() <= 0 || optpl.getOpName() == null || optpl.getOpValue() == null) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.DATA_INVALID);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.DATA_INVALID));

            return mapper.writeValueAsString(ret);
        }

        // hessian call
        OPTpl tmp = oPTplService.findOne(optpl.getOpId());
        optpl.setCreateTime(tmp.getCreateTime());
        optpl.setModifyTime(new Date());

        Boolean b = oPTplService.update(optpl);
        if (!b) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(optpl);
            setLogObject(new Object[] { optpl.getOpName() });
        }

        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions(value = { "OPTpl:view", "OPTpl:create", "OPTpl:update", "OPTpl:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
    public String list(ServletRequest request, Pager pager, Map<String, Object> map) {
        List<OPTpl> optpls = new ArrayList<OPTpl>();
        SearchFilter filter = DynamicSpecifications.genSearchFilter(request);
        if (filter != null && filter.getRules() != null && filter.getRules().size() > 0) {
            // hessian call
        } else {
            optpls = oPTplService.findByPage(pager.getPageSize(), pager.getCurrPage());
        }

        Long count = oPTplService.findCount();
        pager.setTotalCount(count);
        map.put("pager", pager);
        map.put("optpls", optpls);

        return LIST;
    }

    @RequiresPermissions(value = { "OPTpl:view", "OPTpl:create", "OPTpl:update", "OPTpl:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/view/{id}", method = { RequestMethod.GET })
    public String view(@PathVariable Integer id, Map<String, Object> map) {
        OPTpl optpl = oPTplService.findOne(id);
        map.put("optpl", optpl);
        return VIEW;
    }
}
