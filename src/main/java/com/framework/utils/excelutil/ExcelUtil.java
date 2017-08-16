package com.framework.utils.excelutil;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uc.entity.IMSIInfo;

public class ExcelUtil {

    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    private static final String IMSI = "imsi";

    public static void main(String[] args) {

        String path = "C:/Users/wangxinyu/Downloads/imsiimport.xlsx";
        String path1 = "C:/Users/wangxinyu/Downloads/imsiwrite1.xls";
        String path2 = "C:/Users/wangxinyu/Downloads/imsiwrite2.xlsx";
        try {
            // List<IMSIInfo> list = new ArrayList<IMSIInfo>();
            // IMSIInfo imsi = new IMSIInfo();
            // imsi.setImsi("123123123123");
            // imsi.setK("12312312312312312312312312312312");
            // list.add(imsi);
            // imsi = new IMSIInfo();
            // imsi.setImsi("123123123124");
            // imsi.setK("12312312312312312312312312312314");
            // list.add(imsi);
            // System.out.println(writeExcel(path, IMSI, list));

            List<IMSIInfo> list = new ArrayList<IMSIInfo>();
            list = (List<IMSIInfo>) readExcel(path, IMSI);
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i));
            }

            System.out.println(writeExcel(path1, IMSI, list));
            System.out.println(writeExcel(path2, IMSI, list));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Object readExcel(String path, String type) throws Exception {
        // 根据类型调用
        if (IMSI.equals(type)) {
            // imsi导入
            List<IMSIInfo> result = new ArrayList<IMSIInfo>();
            result = importImsi(path, type);
            return result;
        }
        return null;
    }

    public static boolean writeExcel(String path, String type, Object o) throws Exception {
        if (IMSI.equals(type)) {
            return exportImsi(path, type, o);
        }
        return true;
    }

    public static List<IMSIInfo> importImsi(String path, String type) throws Exception {
        // 根据文件名选择需要调用的函数
        JSONArray jsonArray = readExcelFromFile(path, type);

        return JSONArray.parseArray(jsonArray.toJSONString(), IMSIInfo.class);
    }

    public static boolean exportImsi(String path, String type, Object o) throws Exception {
        List<IMSIInfo> list = (List<IMSIInfo>) o;
        JSONArray jsonArray = JSONArray.parseArray(JSONObject.toJSONString(list));
        return writeExcelToFile(path, type, jsonArray);
    }

    public static JSONArray readExcelFromFile(String path, String type) throws Exception {
        // 判断文件是否符合要求
        if (path == null || "".equals(path)) {
            return null;
        } else if (path.endsWith("xls") || path.endsWith("XLS")) {
            return XlsExcelParse.parseExcel(path, type);
        } else if (path.endsWith("xlsx") || path.endsWith("XLSX")) {
            return XlsxExcelParse.parseExcel(path, type);
        } else {
            // 文件不符合要求
            log.error("文件后缀名不符合要求！");
            return null;
        }
    }

    public static boolean writeExcelToFile(String path, String type, JSONArray jsonArray) {
        // 判断文件是否符合要求
        if (path == null || "".equals(path)) {
            log.error("文件名为空!");
            return false;
        } else if (path.endsWith("xls") || path.endsWith("XLS")) {
            return XlsExcelParse.writeToExcel(path, type, jsonArray);
        } else if (path.endsWith("xlsx") || path.endsWith("XLSX")) {
            return XlsxExcelParse.writeToExcel(path, type, jsonArray);
        } else {
            // 文件不符合要求
            log.error("文件后缀名不符合要求！");
            return false;
        }
    }

}
