package com.uc.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import com.framework.utils.pager.Pager;
import com.uc.service.UserAccountService;
import com.uc.task.BatchOpenAccountThread;
import com.uc.task.UCExcelHandler;

@Controller
@RequestMapping("/importaccount")
public class ImportAccountController extends BaseController {

    @Autowired
    private UserAccountService userAccountService;

    ObjectMapper mapper = new ObjectMapper();
    private static final String LIST = "biz/mgruser/importaccount/list";

    @RequiresPermissions("UserAccount:create")
    @RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
    public String list(ServletRequest request, Pager pager, Map<String, Object> map) {

        return LIST;
    }

    //
    // @RequiresPermissions("UserAccount:create")
    // @RequestMapping(value = "/import", method = { RequestMethod.GET })
    // public String preImportImsi(Map<String, Object> map) {
    //
    // return LIST;
    // }

    @RequiresPermissions("UserAccount:create")
    @RequestMapping(value = "/import/progress", method = { RequestMethod.POST })
    public @ResponseBody String importImsiProgress(@RequestParam("filename") String filename)
            throws JsonProcessingException {
        GeneralResponseData<Integer> ret = new GeneralResponseData<Integer>();

        int progress = UCExcelHandler.getInstance().getParseProgress(filename);

        ret.setStatus(AppConstants.SUCCESS);
        ret.setData(progress);
        return mapper.writeValueAsString(ret);
    }

    @Log(message = "批量开户，文件:{0}，结果:{1}", level = LogLevel.INFO, catrgory = "uc")
    @RequiresPermissions("UserAccount:create")
    @RequestMapping(value = "/import", method = { RequestMethod.POST })
    public @ResponseBody String importImsi(@RequestParam("openaccountfilename") MultipartFile openaccountfile)
            throws JsonProcessingException {
        GeneralResponseData<String> ret = new GeneralResponseData<String>();

        String openaccountfilename = openaccountfile.getOriginalFilename();

        if (!("xls".equals(FileUtils.getFileExt(openaccountfilename)) || "xlsx".equals(FileUtils
                .getFileExt(openaccountfilename)))) {
            ret.setStatus(AppConstants.FAILED);
            ret.setErrCode(SysErrorCode.FILE_FORMAT_INVALID);
            ret.setErrMsg(SysErrorCode.MAP.get(SysErrorCode.FILE_FORMAT_INVALID));

            return mapper.writeValueAsString(ret);
        }

        String folder = DateUtils.getTodayString();
        String path = PropertiesUtil.getInstance().getKeyValue(AppConstants.DOWNLOAD_PATH) + File.separator + folder;
        String newfilename = IDUtil.getSN() + "_" + openaccountfilename;
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
            in = new BufferedInputStream(openaccountfile.getInputStream());
            out = new BufferedOutputStream(new FileOutputStream(newfilenameWithPath));
            byte[] data = new byte[1024];
            int len = 0;
            while (-1 != (len = in.read(data, 0, data.length))) {
                out.write(data, 0, len);
            }
            out.flush();

            // parse excel in thread, notify progress
            ExecutorServiceManage.execute(new BatchOpenAccountThread(newfilenameWithPath));

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

    @RequiresPermissions("UserAccount:create")
    @RequestMapping(value = "/download", method = { RequestMethod.GET })
    public @ResponseBody String downloadImsiTemplate(Map<String, Object> map) {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            response.setContentType("application/x-excel;charset=utf-8");
            response.setHeader("Content-disposition", "attachment;filename=openaccount.xls");
            String path = this.getClass().getClassLoader().getResource("template").getPath();
            in = new BufferedInputStream(new FileInputStream(path + File.separator + "openaccount.xls"));
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
