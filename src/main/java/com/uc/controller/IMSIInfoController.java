package com.uc.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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
import com.uc.entity.IMSIInfo;
import com.uc.entity.OPTpl;
import com.uc.service.IMSIInfoService;
import com.uc.service.OPTplService;
import com.uc.task.BatchIMSIThread;
import com.uc.task.UCExcelHandler;

@Controller
@RequestMapping("/imsi")
public class IMSIInfoController extends BaseController {

    @Autowired
    private IMSIInfoService iMSIInfoService;

    @Autowired
    private OPTplService oPTplService;

    ObjectMapper mapper = new ObjectMapper();
    private static final String CREATE = "biz/mgrres/imsi/create";
    private static final String UPDATE = "biz/mgrres/imsi/update";
    private static final String LIST = "biz/mgrres/imsi/list";
    private static final String VIEW = "biz/mgrres/imsi/view";
    private static final String IMPORT = "biz/mgrres/imsi/import";

    @RequiresPermissions("IMSIInfo:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String preCreate(Map<String, Object> map) {
        List<OPTpl> optpls = oPTplService.findAll();
        map.put("optpls", optpls);

        return CREATE;
    }

    @Log(message = "添加了IMSI:{0}", level = LogLevel.INFO, catrgory = "uc")
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
        imsiinfo.setStatus(0);
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

    @Log(message = "删除了IMSI:{0}", level = LogLevel.INFO, catrgory = "uc")
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

    @Log(message = "修改了IMSI:{0}的信息", level = LogLevel.INFO, catrgory = "uc")
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

        List<OPTpl> optpls = oPTplService.findAll();
        map.put("optpls", optpls);

        return LIST;
    }

    @RequiresPermissions(value = { "IMSIInfo:view", "IMSIInfo:create", "IMSIInfo:update", "IMSIInfo:delete" }, logical = Logical.OR)
    @RequestMapping(value = "/view/{id}", method = { RequestMethod.GET })
    public String view(@PathVariable Integer id, Map<String, Object> map) {
        IMSIInfo imsiinfo = iMSIInfoService.findOne(id);
        map.put("imsiinfo", imsiinfo);
        return VIEW;
    }

    @RequiresPermissions("IMSIInfo:create")
    @RequestMapping(value = "/import", method = { RequestMethod.GET })
    public String preImportImsi(Map<String, Object> map) {
        List<OPTpl> optpls = oPTplService.findAll();
        map.put("optpls", optpls);

        return IMPORT;
    }

    @RequiresPermissions("IMSIInfo:create")
    @RequestMapping(value = "/import/progress", method = { RequestMethod.POST })
    public @ResponseBody String importImsiProgress(@RequestParam("filename") String filename)
            throws JsonProcessingException {
        GeneralResponseData<Integer> ret = new GeneralResponseData<Integer>();

        int progress = UCExcelHandler.getInstance().getParseProgress(filename);

        ret.setStatus(AppConstants.SUCCESS);
        ret.setData(progress);
        return mapper.writeValueAsString(ret);
    }

    @Log(message = "导入IMSI，文件:{0}，结果:{1}", level = LogLevel.INFO, catrgory = "uc")
    @RequiresPermissions("IMSIInfo:create")
    @RequestMapping(value = "/import", method = { RequestMethod.POST })
    public @ResponseBody String importImsi(@RequestParam("imsifile") MultipartFile imsifile)
            throws JsonProcessingException {
        GeneralResponseData<String> ret = new GeneralResponseData<String>();

        String imsifilename = imsifile.getOriginalFilename();

        if (!("xls".equals(FileUtils.getFileExt(imsifilename)) || "xlsx".equals(FileUtils.getFileExt(imsifilename)))) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.FILE_FORMAT_INVALID);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.FILE_FORMAT_INVALID));

            return mapper.writeValueAsString(ret);
        }

        String folder = DateUtils.getTodayString();
        String path = PropertiesUtil.getInstance().getKeyValue(AppConstants.DOWNLOAD_PATH) + File.separator + folder;
        String newfilename = IDUtil.getSN() + "_" + imsifilename;
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
            in = new BufferedInputStream(imsifile.getInputStream());
            out = new BufferedOutputStream(new FileOutputStream(newfilenameWithPath));
            byte[] data = new byte[1024];
            int len = 0;
            while (-1 != (len = in.read(data, 0, data.length))) {
                out.write(data, 0, len);
            }
            out.flush();

            // parse excel in thread, notify progress
            ExecutorServiceManage.execute(new BatchIMSIThread(newfilenameWithPath));

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

    @RequiresPermissions("IMSIInfo:create")
    @RequestMapping(value = "/download", method = { RequestMethod.GET })
    public @ResponseBody String downloadImsiTemplate(Map<String, Object> map) {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            response.setContentType("application/x-excel;charset=utf-8");
            response.setHeader("Content-disposition", "attachment;filename=imsiimport.xls");
            String path = this.getClass().getClassLoader().getResource("template").getPath();
            in = new BufferedInputStream(new FileInputStream(path + File.separator + "imsiimport.xls"));
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
