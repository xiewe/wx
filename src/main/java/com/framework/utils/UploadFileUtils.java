package com.framework.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.framework.utils.RandomUtil;

public class UploadFileUtils {

    // filepath - 本地磁盘路径，最后需要带分隔符
    public static List<String> upload(HttpServletRequest request, String filepath) throws IllegalStateException,
            IOException {
        List<String> listStr = new ArrayList<String>();

        File fp = new File(filepath);
        if (!fp.exists()) {
            fp.mkdirs();
        }

        // 解析器解析request的上下文
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession()
                .getServletContext());
        // 先判断request中是否包涵multipart类型的数据，
        if (multipartResolver.isMultipart(request)) {
            // 再将request中的数据转化成multipart类型的数据
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                MultipartFile file = multiRequest.getFile((String) iter.next());
                if (file != null) {
                    String oldFileName = file.getOriginalFilename();
                    String fileName = "";
                    int index = oldFileName.lastIndexOf(".");
                    if (index != -1) {
                        fileName = RandomUtil.generateUUID() + oldFileName.substring(index);
                    } else {
                        fileName = RandomUtil.generateUUID();
                    }

                    String path = filepath + fileName;
                    File localFile = new File(path);
                    // 写文件到本地
                    file.transferTo(localFile);

                    listStr.add(fileName);
                }
            }
        }

        return listStr;
    }
}
