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
import com.uc.entity.BlackWhiteList;
import com.uc.service.BlackWhiteListService;

@Controller
@RequestMapping("/blacklist")
public class BlackListController extends BaseController {

    @Autowired
    private BlackWhiteListService blackWhiteListService;

    ObjectMapper mapper = new ObjectMapper();
    private static final String CREATE = "biz/mgrres/blacklist/create";
    private static final String UPDATE = "biz/mgrres/blacklist/update";
    private static final String LIST = "biz/mgrres/blacklist/list";
    private static final String VIEW = "biz/mgrres/blacklist/view";

    @RequiresPermissions("BlackWhiteList:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String preCreate(Map<String, Object> map) {
        return CREATE;
    }

    @Log(message = "添加了白名单:{0}", level = LogLevel.INFO)
    @RequiresPermissions("BlackWhiteList:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody String create(@Valid BlackWhiteList blackwhitelist) throws JsonProcessingException {
        GeneralResponseData<BlackWhiteList> ret = new GeneralResponseData<BlackWhiteList>();

        // hessian call
        blackwhitelist.setCreateTime(new Date());
        Boolean b = blackWhiteListService.add(blackwhitelist);
        if (!b) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(blackwhitelist);
            setLogObject(new Object[] { blackwhitelist.getImeisv() + "-" + blackwhitelist.getImsi() });
        }

        return mapper.writeValueAsString(ret);
    }

    @Log(message = "删除了白名单:{0}", level = LogLevel.INFO)
    @RequiresPermissions("BlackWhiteList:delete")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public @ResponseBody String delete(@PathVariable double id) throws JsonProcessingException {
        GeneralResponseData<String> ret = new GeneralResponseData<String>();

        // hessian call
        blackWhiteListService.delete(id);
        ret.setStatus(AppConstants.SUCCESS);
        setLogObject(new Object[] { id });
        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions("BlackWhiteList:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String preUpdate(@PathVariable Integer id, Map<String, Object> map) {
        BlackWhiteList blackwhitelist = blackWhiteListService.findOne(id);
        map.put("blackwhitelist", blackwhitelist);
        return UPDATE;
    }

    @Log(message = "修改了白名单:{0}的信息", level = LogLevel.INFO)
    @RequiresPermissions("BlackWhiteList:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody String update(@Valid BlackWhiteList blackwhitelist) throws JsonProcessingException {
        GeneralResponseData<BlackWhiteList> ret = new GeneralResponseData<BlackWhiteList>();

        // hessian call
        BlackWhiteList tmp = blackWhiteListService.findOne(blackwhitelist.getCreateTime().getTime());
        blackwhitelist.setCreateTime(tmp.getCreateTime());
        blackwhitelist.setModifyTime(new Date());

        Boolean b = blackWhiteListService.update(blackwhitelist);
        if (!b) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
        } else {
            ret.setStatus(AppConstants.SUCCESS);
            ret.setData(blackwhitelist);
            setLogObject(new Object[] { blackwhitelist.getImeisv() + "-" + blackwhitelist.getImsi() });
        }

        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions(value = { "BlackWhiteList:view", "BlackWhiteList:create", "BlackWhiteList:update",
            "BlackWhiteList:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
    public String list(ServletRequest request, Pager pager, Map<String, Object> map) {
        List<BlackWhiteList> blackwhitelists = new ArrayList<BlackWhiteList>();
        SearchFilter filter = DynamicSpecifications.genSearchFilter(request);
        if (filter != null && filter.getRules() != null && filter.getRules().size() > 0) {
            // hessian call
        } else {
            blackwhitelists = blackWhiteListService.findByPage(pager.getPageSize(), pager.getCurrPage());
        }

        Long count = blackWhiteListService.findCount();
        pager.setTotalCount(count);
        map.put("pager", pager);
        map.put("blackwhitelists", blackwhitelists);

        return LIST;
    }

    @RequiresPermissions(value = { "BlackWhiteList:view", "BlackWhiteList:create", "BlackWhiteList:update",
            "BlackWhiteList:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/view/{id}", method = { RequestMethod.GET })
    public String view(@PathVariable Integer id, Map<String, Object> map) {
        BlackWhiteList blackwhitelist = blackWhiteListService.findOne(id);
        map.put("blackwhitelist", blackwhitelist);
        return VIEW;
    }
}
