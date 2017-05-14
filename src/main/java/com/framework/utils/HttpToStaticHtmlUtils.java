package com.framework.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.framework.utils.RandomUtil;

public class HttpToStaticHtmlUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpToStaticHtmlUtils.class);

    private static long star = 0;
    private static long end = 0;
    private static long ttime = 0;

    // 用户请求时存储
    public static String storeHtmlCodeToFile(HttpServletRequest request, String filepath) {
        String httpUrl = "";
        if (request.getQueryString() != null) {
            httpUrl = request.getRequestURL() + "?" + request.getQueryString();
        } else {
            httpUrl = request.getRequestURL().toString();
        }
        
        String uid = "";
        String lang = "";
        String token = "";

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {

                if ("uid".equals(cookies[i].getName())) {
                    uid = cookies[i].getValue();
                } else if ("token".equals(cookies[i].getName())) {
                    token = cookies[i].getValue();
                } else if ("lang".equals(cookies[i].getName())) {
                    lang = cookies[i].getValue();
                }
            }
        }

        return storeHtmlCodeToFile(httpUrl, uid, lang, token, "UTF-8", filepath);
    }

    // 程序构造请求时存储
    public static String storeHtmlCodeToFileWithoutClientRequest(HttpServletRequest request,
            String servlet_querystring, String filepath) {

        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();

        String httpUrl = basePath + servlet_querystring;
        String uid = "";
        String lang = "";
        String token = "";

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {

                if ("uid".equals(cookies[i].getName())) {
                    uid = cookies[i].getValue();
                } else if ("token".equals(cookies[i].getName())) {
                    token = cookies[i].getValue();
                } else if ("lang".equals(cookies[i].getName())) {
                    lang = cookies[i].getValue();
                }
            }
        }

        return storeHtmlCodeToFile(httpUrl, uid, lang, token, "UTF-8", filepath);
    }

    // 静态化http 请求，将http保存在磁盘；filepath 后面需要带分隔符
    private static String storeHtmlCodeToFile(String httpUrl, String uid, String lang, String token, String encoding,
            String filepath) {
        Date before = new Date();
        star = before.getTime();
        StringBuffer htmlCode = new StringBuffer();
        try {
            File fp = new File(filepath);
            if (!fp.exists()) {
                fp.mkdirs();
            }

            URL url = new java.net.URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/4.0");
            connection.setRequestProperty("Cookie", "uid=" + uid + ";lang=" + lang + ";token=" + token);
            connection.connect();

            InputStream in;
            in = connection.getInputStream();
            java.io.BufferedReader breader = new BufferedReader(new InputStreamReader(in, encoding));
            String currentLine;
            while ((currentLine = breader.readLine()) != null) {
                htmlCode.append(currentLine + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Date after = new Date();
            end = after.getTime();
            ttime = end - star;
            logger.debug("执行时间:" + ttime + "毫秒");
        }

        String fileName = RandomUtil.generateUUID() + ".html";
        String fileFullname = filepath + fileName;
        writeHtml(fileFullname, htmlCode.toString(), "YES");

        return fileName;
    }

    // 存储文件 ; flag - 是否覆盖
    private static synchronized void writeHtml(String fileFullname, String info, String flag) {
        PrintWriter pw = null;
        try {
            File writeFile = new File(fileFullname);
            boolean isExit = writeFile.exists();
            if (isExit != true) {
                writeFile.createNewFile();
            } else {
                if (!flag.equals("NO")) {
                    writeFile.delete();
                    writeFile.createNewFile();
                }
            }
            pw = new PrintWriter(new FileOutputStream(fileFullname, true));
            pw.println(info);
            pw.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            pw.close();
        }
    }

    public static void main(String[] args) {

        String url = "http://localhost:8080/cwbizchat/t";
        storeHtmlCodeToFile(url, "1", "zh", "1", "UTF-8", "D:/");
    }

}
