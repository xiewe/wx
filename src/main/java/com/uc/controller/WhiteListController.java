package com.uc.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.AppConstants;
import com.framework.SysErrorCode;
import com.framework.concurrency.ExecutorServiceManage;
import com.framework.controller.BaseController;
import com.framework.entity.GeneralResponseData;
import com.framework.log4jdbc.Log;
import com.framework.log4jdbc.LogLevel;
import com.framework.utils.DateUtils;
import com.framework.utils.FileUtils;
import com.framework.utils.IDUtil;
import com.framework.utils.PropertiesUtil;
import com.framework.utils.pager.DynamicSpecifications;
import com.framework.utils.pager.Pager;
import com.framework.utils.pager.SearchFilter;
import com.uc.entity.BlackWhiteList;
import com.uc.entity.UCResponseData;
import com.uc.service.BlackWhiteListService;
import com.uc.task.BatchBlackWhiteListhread;
import com.uc.task.UCExcelHandler;

@Controller
@RequestMapping("/whitelist")
public class WhiteListController extends BaseController {

    @Autowired
    private BlackWhiteListService blackWhiteListService;

    ObjectMapper mapper = new ObjectMapper();
    private static final String CREATE = "biz/mgrres/whitelist/create";
    private static final String UPDATE = "biz/mgrres/whitelist/update";
    private static final String LIST = "biz/mgrres/whitelist/list";
    private static final String VIEW = "biz/mgrres/whitelist/view";
    private static final String IMPORT = "biz/mgrres/imsi/import";

    @InitBinder
    public void dataBinder(WebDataBinder dataBinder) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(df, true));
    }

    @RequiresPermissions("BlackWhiteList:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String preCreate(Map<String, Object> map) {
        return CREATE;
    }

    @Log(message = "添加了白名单:{0}", level = LogLevel.INFO, catrgory = "uc")
    @RequiresPermissions("BlackWhiteList:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody String create(@Valid BlackWhiteList blackwhitelist) throws JsonProcessingException {
        GeneralResponseData<BlackWhiteList> ret = new GeneralResponseData<BlackWhiteList>();

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

    @Log(message = "删除了白名单:{0}", level = LogLevel.INFO, catrgory = "uc")
    @RequiresPermissions("BlackWhiteList:delete")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody String delete(@RequestParam("ids") String ids, @RequestParam("imeis") String imeis,
            @RequestParam("imsis") String imsis, @RequestParam("listtypes") String listtypes)
            throws JsonProcessingException {
        GeneralResponseData<String> ret = new GeneralResponseData<String>();

        // hessian call
        UCResponseData<String> response = new UCResponseData<String>();

        BlackWhiteList blackwhitelist = new BlackWhiteList();

        String[] idlist, imeilist, imsilist, listtypelist;

        idlist = ids.substring(0, ids.length() - 1).split("-");
        imeilist = imeis.substring(0, imeis.length() - 1).split("-");
        imsilist = imsis.substring(0, imsis.length() - 1).split("-");
        listtypelist = listtypes.substring(0, listtypes.length() - 1).split("-");

        if (idlist.length == imeilist.length && imeilist.length == imsilist.length
                && imsilist.length == listtypelist.length) {
            for (int i = 0; i < idlist.length; i++) {
                blackwhitelist.setImsi(imeilist[i]);
                blackwhitelist.setImeisv(imeilist[i]);
                blackwhitelist.setListType(Integer.parseInt(listtypelist[i]));

                blackWhiteListService.delete(Double.parseDouble(idlist[i]));
                setLogObject(new Object[] { Double.parseDouble(idlist[i]) });
            }
        } else {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.SAVE_FAILED);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.SAVE_FAILED));
            return mapper.writeValueAsString(ret);
        }

        ret.setStatus(AppConstants.SUCCESS);
        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions("BlackWhiteList:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String preUpdate(@PathVariable double id, Map<String, Object> map) {
        BlackWhiteList blackwhitelist = blackWhiteListService.findOne(id);
        map.put("blackwhitelist", blackwhitelist);
        return UPDATE;
    }

    @Log(message = "修改了白名单:{0}的信息", level = LogLevel.INFO, catrgory = "uc")
    @RequiresPermissions("BlackWhiteList:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody String update(@Valid BlackWhiteList blackwhitelist) throws JsonProcessingException {
        GeneralResponseData<BlackWhiteList> ret = new GeneralResponseData<BlackWhiteList>();

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
        long count = 0L;
        SearchFilter filter = DynamicSpecifications.genSearchFilter(request);
        if (filter != null && filter.getRules() != null && filter.getRules().size() > 0) {
            // hessian call

        } else {
            blackwhitelists = blackWhiteListService.findByPage(pager.getPageSize(), pager.getCurrPage());
            count = blackWhiteListService.findCount();
        }

        pager.setTotalCount(count);
        map.put("pager", pager);
        map.put("blackwhitelists", blackwhitelists);

        return LIST;
    }

    @RequiresPermissions(value = { "BlackWhiteList:view", "BlackWhiteList:create", "BlackWhiteList:update",
            "BlackWhiteList:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/view/{id}", method = { RequestMethod.GET })
    public String view(@PathVariable double id, Map<String, Object> map) {
        BlackWhiteList blackwhitelist = blackWhiteListService.findOne(id);
        map.put("blackwhitelist", blackwhitelist);
        return VIEW;
    }

    @RequiresPermissions("BlackWhiteList:create")
    @RequestMapping(value = "/import", method = { RequestMethod.GET })
    public String preImportImsi(Map<String, Object> map) {

        return IMPORT;
    }

    @RequiresPermissions("BlackWhiteList:create")
    @RequestMapping(value = "/import/progress", method = { RequestMethod.POST })
    public @ResponseBody String importImsiProgress(@RequestParam("filename") String filename)
            throws JsonProcessingException {
        GeneralResponseData<Integer> ret = new GeneralResponseData<Integer>();

        int progress = UCExcelHandler.getInstance().getParseProgress(filename);

        ret.setStatus(AppConstants.SUCCESS);
        ret.setData(progress);
        return mapper.writeValueAsString(ret);
    }

    @Log(message = "导入黑白名单，文件:{0}，结果:{1}", level = LogLevel.INFO, catrgory = "uc")
    @RequiresPermissions("BlackWhiteList:create")
    @RequestMapping(value = "/import", method = { RequestMethod.POST })
    public @ResponseBody String importImsi(@RequestParam("blackwhitelist") MultipartFile blackwhitelist)
            throws JsonProcessingException {
        GeneralResponseData<String> ret = new GeneralResponseData<String>();

        String blackwhitelistname = blackwhitelist.getOriginalFilename();

        if (!("xls".equals(FileUtils.getFileExt(blackwhitelistname)) || "xlsx".equals(FileUtils
                .getFileExt(blackwhitelistname)))) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.FILE_FORMAT_INVALID);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.FILE_FORMAT_INVALID));

            return mapper.writeValueAsString(ret);
        }

        String folder = DateUtils.getTodayString();
        String path = PropertiesUtil.getInstance().getKeyValue(AppConstants.DOWNLOAD_PATH) + File.separator + folder;
        String newfilename = IDUtil.getSN() + "_" + blackwhitelistname;
        String newfilenameWithPath = path + File.separator + newfilename;
        String logfilename = newfilename + ".log";
        String logfilenameWithPath = path + File.separator + logfilename;

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }

        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(blackwhitelist.getInputStream());
            out = new BufferedOutputStream(new FileOutputStream(newfilenameWithPath));
            byte[] data = new byte[1024];
            int len = 0;
            while (-1 != (len = in.read(data, 0, data.length))) {
                out.write(data, 0, len);
            }
            out.flush();

            // parse excel in thread, notify progress
            ExecutorServiceManage.execute(new BatchBlackWhiteListhread(newfilenameWithPath));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ret.setStatus(AppConstants.SUCCESS);
        ret.setData(newfilenameWithPath);
        setLogObject(new Object[] {
                "<a target=\"_blank\" href=\"/file/" + folder + "/" + newfilename + "\">" + newfilename + "</a>",
                "<a target=\"_blank\" href=\"/file/" + folder + "/" + logfilename + "\">" + logfilename + "</a>" });
        return mapper.writeValueAsString(ret);
    }

    @RequiresPermissions("BlackWhiteList:create")
    @RequestMapping(value = "/download", method = { RequestMethod.GET })
    public @ResponseBody String downloadImsiTemplate(Map<String, Object> map) {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            response.setContentType("application/x-excel;charset=utf-8");
            response.setHeader("Content-disposition", "attachment;filename=blackwhitelist.xls");
            String path = this.getClass().getClassLoader().getResource("template").getPath();
            in = new BufferedInputStream(new FileInputStream(path + File.separator + "blackwhitelist.xls"));
            out = new BufferedOutputStream(response.getOutputStream());
            byte[] data = new byte[1024];
            int len = 0;
            while (-1 != (len = in.read(data, 0, data.length))) {
                out.write(data, 0, len);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "";
    }
}
