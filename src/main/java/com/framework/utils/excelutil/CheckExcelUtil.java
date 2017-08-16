package com.framework.utils.excelutil;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.framework.utils.PropertiesUtils;

public class CheckExcelUtil {

	private static final Logger log = LoggerFactory.getLogger(CheckExcelUtil.class);
	
	public static PropertiesUtils propertiesUtil = new PropertiesUtils("excel.properties");
	
	public static boolean checkColNum(String type, int colNum) throws Exception{
		return propertiesUtil.getKeyValue(type + ".col").equals(colNum + "");
	}
	
	public static int getColNum(String type){
		return Integer.parseInt(propertiesUtil.getKeyValue(type + ".col"));
	}
	
	public static boolean checkColName(String type, int colNum, String colName) throws Exception{
		return propertiesUtil.getKeyValue(type + ".col" + colNum + ".name").equals(colName);
	}
	
	public static int findExcelHead(String type){
		try{
			return Integer.parseInt(propertiesUtil.getKeyValue(type + ".start.num"));
		}catch(Exception e){
			//如果没有定义该规则，默认从第一行开始
			return 0;
		}
		
	}
	
	public static boolean checkColType(String type, int colNum, String s, int rowNum) throws Exception{
		
		if(s == null || "".equals(s)){
			try{
				if(propertiesUtil.getKeyValue(type + ".col" + colNum + ".allowNull").equals("true"));
				else{
					log.error((rowNum+1) + "行 " + (colNum+1) + "列：不允许为空！");
					return false;
				}
			}catch(Exception e){
				//如果没有定义该规则，默认为不允许空
				log.error((rowNum+1) + "行 " + (colNum+1) + "列：不允许为空！");
				return false;
			}
			
		}
		
		
		
		String format = propertiesUtil.getKeyValue(type + ".col" + colNum + ".type");
		if(format.equals("hex")){
			for(int i = 0; i < s.length(); i++){
				if(s.charAt(i) >= '0' && s.charAt(i) <='9');
				else if(s.charAt(i) >= 'a' && s.charAt(i) <='f');
				else if(s.charAt(i) >= 'A' && s.charAt(i) <='F');
				else {
					log.error((rowNum+1) + "行" + (colNum+1) + "列:" + s + "单元格数据格式不符合要求！");
					return false;
				}
			}
			return true;
		}else if(format.equals("int")){
			try{
				Integer.parseInt(s);
			}catch(Exception e){
				log.error((rowNum+1) + "行" + (colNum+1) + "列:" + s + "单元格数据格式不符合要求！");
				return false;
			}
			return true;
		}
		return true;
	}
	
	public static boolean checkColLength(String type, int colNum, int length) throws Exception{
		try{
			return (Integer.parseInt(propertiesUtil.getKeyValue(type + ".col" + colNum + ".maxLength")) >= length) &&
					(Integer.parseInt(propertiesUtil.getKeyValue(type + ".col" + colNum + ".minLength")) <= length);
		}catch(Exception e){
			//如果没有定义该规则，默认所有长度都可以
			return true;
		}
		
	}
	
	public static String getElementName(String type, int colNum){
		return propertiesUtil.getKeyValue(type + ".col" + colNum + ".elementName");
	}
	
	public static boolean isBlankRow(String type, HSSFRow row){
		for(int i = 0; i < Integer.parseInt(propertiesUtil.getKeyValue(type + ".col")); i++){
			HSSFCell cell = row.getCell(i);
			if (cell == null) {
			    break;
			}
			String data = cell.getStringCellValue();
			if(data != null && !"".equals(data)) return false;
		}
		return true;
	}
	
	public static String getColName(String type, int colNum){
		return propertiesUtil.getKeyValue(type + ".col" + colNum + ".name");
	}
	
}
