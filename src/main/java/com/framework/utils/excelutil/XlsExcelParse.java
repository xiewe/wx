package com.framework.utils.excelutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class XlsExcelParse {
	
	private static final Logger log = LoggerFactory.getLogger(XlsExcelParse.class);
	
	public static JSONArray parseExcel(String path, String type) throws Exception{
		File file = new File(path);
		InputStream inputStream = new FileInputStream(file);
		HSSFWorkbook hwb = new HSSFWorkbook(inputStream);
		HSSFSheet sheet = hwb.getSheetAt(0);// 暂定只取首页签
		
		//取出表头位置
		int begin = CheckExcelUtil.findExcelHead(type);
		int end = sheet.getLastRowNum();
		
		HSSFRow row = sheet.getRow(begin);
		
		//检查列数是否符合要求
		if(!CheckExcelUtil.checkColNum(type, row.getLastCellNum())){
			log.error("表格列数不正确!");
			return null;
		}
		
		//检查列名是否正确
		for(int i = 0; i < CheckExcelUtil.getColNum(type); i++){
			HSSFCell cell = row.getCell(i);
			if(!CheckExcelUtil.checkColName(type, i, getStringXLSCellValue(cell))){
				log.error("表头信息不符合!");
				return null;
			}
		}
		
		JSONArray jsonArray = new JSONArray();
		
		boolean error = false;
		
		for(int i = begin + 1; i <= end; i++){
			row = sheet.getRow(i);
			JSONObject jsonObject = new JSONObject();
			error = false;
			
			//如果是空行直接结束
			if(CheckExcelUtil.isBlankRow(type, row)) break;
			
			for(int j = 0; j < CheckExcelUtil.getColNum(type); j++){
				HSSFCell cell = row.getCell(j);
				String data = getStringXLSCellValue(cell);
				
				//检查单元格内数据格式
				if(!CheckExcelUtil.checkColType(type, j, data, i)){
					error = true;
					break;
				}
				
				//检查数据长度是否符合
				if(!CheckExcelUtil.checkColLength(type, j, data.length())){
					log.error((i+1) + "行" + (j+1) + "列：" + data + ":单元格数据长度不符合要求！");
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
	private static String getStringXLSCellValue(HSSFCell cell) {
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
		case HSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
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
		case HSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
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
			HSSFWorkbook hwb = new HSSFWorkbook();
			HSSFSheet sheet = hwb.createSheet("imsi");
			
			HSSFRow row = sheet.createRow(0);
			
			int colNum = CheckExcelUtil.getColNum(type);
			HSSFCell[] cells = new HSSFCell[colNum];
			
			for(int i = 0; i < colNum; i++){
				cells[i] = row.createCell(i);
				cells[i].setCellValue(CheckExcelUtil.getColName(type, i));
			}
			
			for(int i = 0; i < jsonArray.size(); i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				row = sheet.createRow(i + 1);
				cells = new HSSFCell[colNum];
				for(int j = 0; j < colNum; j++){
					cells[j] = row.createCell(j);
					cells[j].setCellValue((String)jsonObject.get(CheckExcelUtil.getElementName(type, j)));
				}
			}
			
			hwb.write(outputStream);
			outputStream.close();
			return true;
			
			
			
			
		} catch (Exception e) {
			log.error("执行失败");
			e.printStackTrace();
			return false;
		}
		
	}
}
