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
import com.uc.entity.IPFInfo;
import com.uc.service.IPFInfoService;

@Controller
@RequestMapping("/ip")
public class IPFInfoController extends BaseController {

    @Autowired
    private IPFInfoService iPFInfoService;

    ObjectMapper mapper = new ObjectMapper();
    private static final String CREATE = "biz/mgrres/ip/create";
    private static final String UPDATE = "biz/mgrres/ip/update";
    private static final String LIST = "biz/mgrres/ip/list";
    private static final String VIEW = "biz/mgrres/ip/view";

    @RequiresPermissions("IPFInfo:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String preCreate(Map<String, Object> map) {
        return CREATE;
    }

    @Log(message = "添加了网段:{0}", level = LogLevel.INFO)
    @RequiresPermissions("IPFInfo:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody String create(@Valid IPFInfo ipfinfo) throws JsonProcessingException {
        GeneralResponseData<IPFInfo> ret = new GeneralResponseData<IPFInfo>();

        if (StringUtils.isEmpty(ipfinfo.getIpFragment()) || StringUtils.isEmpty(ipfinfo.getIpMask())) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.DATA_INVALID);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.DATA_INVALID));

            return mapper.writeValueAsString(ret);
        }

        // hessian call
        ipfinfo.setCreateTime(new Date());
        Boolean b = iPFInfoService.add(ipfinfo);
        if (!b) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(ipfinfo);
            setLogObject(new Object[] { ipfinfo.getIpFragment() + "-" + ipfinfo.getIpMask() });
        }

        return mapper.writeValueAsString(ret);
    }

    @Log(message = "删除了网段:{0}", level = LogLevel.INFO)
    @RequiresPermissions("IPFInfo:delete")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public @ResponseBody String delete(@PathVariable double id) throws JsonProcessingException {
        GeneralResponseData<String> ret = new GeneralResponseData<String>();

        // hessian call
        iPFInfoService.delete(id);
        ret.setStatus(AppConstants.SUCCESS);
        setLogObject(new Object[] { id });
        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions("IPFInfo:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String preUpdate(@PathVariable Integer id, Map<String, Object> map) {
        IPFInfo ipfinfo = iPFInfoService.findOne(id);
        map.put("ipfinfo", ipfinfo);
        return UPDATE;
    }

    @Log(message = "修改了网段:{0}的信息", level = LogLevel.INFO)
    @RequiresPermissions("IPFInfo:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody String update(@Valid IPFInfo ipfinfo) throws JsonProcessingException {
        GeneralResponseData<IPFInfo> ret = new GeneralResponseData<IPFInfo>();

        if (StringUtils.isEmpty(ipfinfo.getIpFragment()) || StringUtils.isEmpty(ipfinfo.getIpMask())) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.DATA_INVALID);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.DATA_INVALID));

            return mapper.writeValueAsString(ret);
        }

        // hessian call
        IPFInfo tmp = iPFInfoService.findOne(ipfinfo.getCreateTime().getTime());
        ipfinfo.setCreateTime(tmp.getCreateTime());
        ipfinfo.setModifyTime(new Date());

        Boolean b = iPFInfoService.update(ipfinfo);
        if (!b) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(ipfinfo);
            setLogObject(new Object[] { ipfinfo.getIpFragment() + "-" + ipfinfo.getIpMask() });
        }

        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions(value = { "IPFInfo:view", "IPFInfo:create", "IPFInfo:update", "IPFInfo:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
    public String list(ServletRequest request, Pager pager, Map<String, Object> map) {
        List<IPFInfo> ipfinfos = new ArrayList<IPFInfo>();
        SearchFilter filter = DynamicSpecifications.genSearchFilter(request);
        if (filter != null && filter.getRules() != null && filter.getRules().size() > 0) {
            // hessian call
        } else {
            ipfinfos = iPFInfoService.findByPage(pager.getPageSize(), pager.getCurrPage());
        }

        Long count = iPFInfoService.findCount();
        pager.setTotalCount(count);
        map.put("pager", pager);
        map.put("ipfinfos", ipfinfos);

        return LIST;
    }

    @RequiresPermissions(value = { "IPFInfo:view", "IPFInfo:create", "IPFInfo:update", "IPFInfo:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/view/{id}", method = { RequestMethod.GET })
    public String view(@PathVariable Integer id, Map<String, Object> map) {
        IPFInfo ipfinfo = iPFInfoService.findOne(id);
        map.put("ipfinfo", ipfinfo);
        return VIEW;
    }
}
