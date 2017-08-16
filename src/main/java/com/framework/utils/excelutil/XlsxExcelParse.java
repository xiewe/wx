package com.framework.utils.excelutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class XlsxExcelParse {

	private static final Logger log = LoggerFactory.getLogger(XlsxExcelParse.class);
	public static JSONArray parseExcel(String path, String type) throws Exception{
		File file = new File(path);
		InputStream inputStream = new FileInputStream(file);
		XSSFWorkbook hwb = new XSSFWorkbook(inputStream);
		XSSFSheet sheet = hwb.getSheetAt(0);
		
		//取出表头位置
		int begin = CheckExcelUtil.findExcelHead(type);
		int end = sheet.getLastRowNum();
		
		XSSFRow row = sheet.getRow(begin);
		
		//检查列数是否符合要求
		if(!CheckExcelUtil.checkColNum(type, row.getLastCellNum())){
			log.error("表格列数不正确!");
			return null;
		}
		
		//检查列名是否正确
		for(int i = 0; i < CheckExcelUtil.getColNum(type); i++){
			XSSFCell cell = row.getCell(i);
			if(!CheckExcelUtil.checkColName(type, i, getStringXLSXCellValue(cell))){
				log.error("表头信息不符合!");
				return null;
			}
		}
		
		JSONArray jsonArray = new JSONArray();
		
		boolean error = false, blankRow = true;
		
		for(int i = begin + 1; i <= end; i++){
			row = sheet.getRow(i);
			JSONObject jsonObject = new JSONObject();
			error = false;
			if(row == null) break;
			for(int j = 0; j < CheckExcelUtil.getColNum(type); j++){
				XSSFCell cell = row.getCell(j);
				String data = getStringXLSXCellValue(cell);
				if(data != null && !"".equals(data)){
					blankRow = false;
				}
				//检查单元格内数据格式
				if(!CheckExcelUtil.checkColType(type, j, data, i)){
					error = true;
					break;
				}
				
				//检查数据长度是否符合
				if(!CheckExcelUtil.checkColLength(type, j, data.length())){
					log.error((i+1) + "行" + (j+1) + "列:" + data + "单元格数据长度不符合要求！");
					error = true;
					break;
				}
				
				jsonObject.put(CheckExcelUtil.getElementName(type, j), data);
			}
			
			
			if(!error){
				//没有出错加入链表中
				jsonArray.add(jsonObject);
			}
			error = false;
			
		}
		inputStream.close();
		return jsonArray;
		
	}
	
	/**
	 * 获取单元格数据内容为字符串类型的数据
	 * 
	 * @param cell
	 *            Excel单元格
	 * @return String 单元格数据内容
	 */
	private static String getStringXLSXCellValue(XSSFCell cell) {
		String strCell = "";
		if (cell == null) {
			return "";
		}
		// 将数值型参数转成文本格式，该算法不能保证1.00这种类型数值的精确度
		DecimalFormat df = (DecimalFormat) NumberFormat.getPercentInstance();
		StringBuffer sb = new StringBuffer();
		sb.append("0");
		df.applyPattern(sb.toString());

		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case XSSFCell.CELL_TYPE_NUMERIC:
			double value = cell.getNumericCellValue();
			while (Double.parseDouble(df.format(value)) != value) {
				if ("0".equals(sb.toString())) {
					sb.append(".0");
				} else {
					sb.append("0");
				}
				df.applyPattern(sb.toString());
			}
			strCell = df.format(value);
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case XSSFCell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		if (strCell == null || "".equals(strCell)) {
			return "";
		}
		return strCell;
	}
	
	public static boolean writeToExcel(String path, String type, JSONArray jsonArray){
		File file = new File(path);
		if(file.exists()){
			log.error("文件已经存在!");
			return false;
		}else{
			try{
				file.createNewFile();
			}catch(Exception e){
				log.error(e.toString());
				return false;
			}
		}
		try {
			
			OutputStream outputStream = new FileOutputStream(file);
			XSSFWorkbook hwb = new XSSFWorkbook();
			XSSFSheet sheet = hwb.createSheet("imsi");
			XSSFRow row = sheet.createRow(0);
			
			int colNum = CheckExcelUtil.getColNum(type);
			XSSFCell[] cells = new XSSFCell[colNum];
			
			for(int i = 0; i < colNum; i++){
				cells[i] = row.createCell(i);
				cells[i].setCellValue(CheckExcelUtil.getColName(type, i));
			}
			
			for(int i = 0; i < jsonArray.size(); i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				row = sheet.createRow(i + 1);
				cells = new XSSFCell[colNum];
				for(int j = 0; j < colNum; j++){
					cells[j] = row.createCell(j);
					cells[j].setCellValue((String)jsonObject.get(CheckExcelUtil.getElementName(type, j)));
				}
			}
			
			hwb.write(outputStream);
			outputStream.close();
			return true;
			
		}catch(Exception e){
			log.error("执行失败");
			e.printStackTrace();
			return false;
		}
	}
}
