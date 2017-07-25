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
import com.uc.entity.IMSIInfo;
import com.uc.service.IMSIInfoService;

@Controller
@RequestMapping("/imsi")
public class IMSIInfoController extends BaseController {

    @Autowired
    private IMSIInfoService iMSIInfoService;

    ObjectMapper mapper = new ObjectMapper();
    private static final String CREATE = "biz/mgrres/imsi/create";
    private static final String UPDATE = "biz/mgrres/imsi/update";
    private static final String LIST = "biz/mgrres/imsi/list";
    private static final String VIEW = "biz/mgrres/imsi/view";

    @RequiresPermissions("IMSIInfo:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String preCreate(Map<String, Object> map) {
        return CREATE;
    }

    @Log(message = "添加了IMSI:{0}", level = LogLevel.INFO)
    @RequiresPermissions("IMSIInfo:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody String create(@Valid IMSIInfo imsiinfo) throws JsonProcessingException {
        GeneralResponseData<IMSIInfo> ret = new GeneralResponseData<IMSIInfo>();

        if (StringUtils.isEmpty(imsiinfo.getImsi()) || StringUtils.isEmpty(imsiinfo.getK())) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.DATA_INVALID);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.DATA_INVALID));

            return mapper.writeValueAsString(ret);
        }

        // hessian call
        imsiinfo.setCreateTime(new Date());
        Boolean b = iMSIInfoService.add(imsiinfo);
        if (!b) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(imsiinfo);
            setLogObject(new Object[] { imsiinfo.getImsi() });
        }

        return mapper.writeValueAsString(ret);
    }

    @Log(message = "删除了IMSI:{0}", level = LogLevel.INFO)
    @RequiresPermissions("IMSIInfo:delete")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public @ResponseBody String delete(@PathVariable double id) throws JsonProcessingException {
        GeneralResponseData<String> ret = new GeneralResponseData<String>();

        // hessian call
        iMSIInfoService.delete(id);
        ret.setStatus(AppConstants.SUCCESS);
        setLogObject(new Object[] { id });
        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions("IMSIInfo:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String preUpdate(@PathVariable Integer id, Map<String, Object> map) {
        IMSIInfo imsiinfo = iMSIInfoService.findOne(id);
        map.put("imsiinfo", imsiinfo);
        return UPDATE;
    }

    @Log(message = "修改了IMSI:{0}的信息", level = LogLevel.INFO)
    @RequiresPermissions("IMSIInfo:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody String update(@Valid IMSIInfo imsiinfo) throws JsonProcessingException {
        GeneralResponseData<IMSIInfo> ret = new GeneralResponseData<IMSIInfo>();

        if (StringUtils.isEmpty(imsiinfo.getImsi()) || StringUtils.isEmpty(imsiinfo.getK())) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.DATA_INVALID);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.DATA_INVALID));

            return mapper.writeValueAsString(ret);
        }

        // hessian call
        IMSIInfo tmp = iMSIInfoService.findOne(imsiinfo.getCreateTime().getTime());
        imsiinfo.setCreateTime(tmp.getCreateTime());
        imsiinfo.setModifyTime(new Date());

        Boolean b = iMSIInfoService.update(imsiinfo);
        if (!b) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(imsiinfo);
            setLogObject(new Object[] { imsiinfo.getImsi() });
        }

        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions(value = { "IMSIInfo:view", "IMSIInfo:create", "IMSIInfo:update", "IMSIInfo:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
    public String list(ServletRequest request, Pager pager, Map<String, Object> map) {
        List<IMSIInfo> imsiinfos = new ArrayList<IMSIInfo>();
        SearchFilter filter = DynamicSpecifications.genSearchFilter(request);
        if (filter != null && filter.getRules() != null && filter.getRules().size() > 0) {
            // hessian call
        } else {
            imsiinfos = iMSIInfoService.findByPage(pager.getPageSize(), pager.getCurrPage());
        }

        Long count = iMSIInfoService.findCount();
        pager.setTotalCount(count);
        map.put("pager", pager);
        map.put("imsiinfos", imsiinfos);

        return LIST;
    }

    @RequiresPermissions(value = { "IMSIInfo:view", "IMSIInfo:create", "IMSIInfo:update", "IMSIInfo:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/view/{id}", method = { RequestMethod.GET })
    public String view(@PathVariable Integer id, Map<String, Object> map) {
        IMSIInfo imsiinfo = iMSIInfoService.findOne(id);
        map.put("imsiinfo", imsiinfo);
        return VIEW;
    }
}
