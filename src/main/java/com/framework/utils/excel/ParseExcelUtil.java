package com.framework.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.taglibs.standard.lang.jstl.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.framework.utils.DateUtils;

/**
 * 解析excel 工具类
 *
 */
@SuppressWarnings("rawtypes")
public class ParseExcelUtil {
	private final static Logger logger = LoggerFactory
			.getLogger(ParseExcelUtil.class);

	public interface CallbackHandler {
		public void sendbroadcast(String msg);
	}

	/** xml中验证规则的名称name **/
	public static String RULE_NAME_NULLABLE = "nullable";
	public static String RULE_NAME_UNIQUE = "checkUnique";

	/** excel 中的模板数据错误 **/
	public static String ERROR_EXCEL_NULL = "excel中数据为空!<br>";
	public static String ERROR_EXCEL_COLUMN_NOT_EQUAL = "xml列数与excel列数不相符，请检查!<br>";
	public static String ERROR_EXCEL_DATA_TYPE = "数据类型错误";

	public FileInputStream fis;
	public HSSFWorkbook workBook;
	public HSSFSheet sheet;
	public ParseXMLUtil parseXmlUtil;

	/** 当前实体类的code **/
	public String curEntityCode;
	/** 表头map对象：key:entityCode, value:headMap(index,headTitle) **/
	public Map curEntityHeadMap;

	/** 字段的必填：key:entityCode+headTitle, value:true(必填),false(不必填) **/
	public Map curEntityColRequired;

	/** 存放每一行的数据 **/
	public List listDatas;

	public StringBuffer errorString;
	private CallbackHandler callbackHandler;

	public ParseExcelUtil(File excelFile, File xmlFile,
			CallbackHandler callbackHandler) {
		this.callbackHandler = callbackHandler;
		try {
			if (excelFile == null) {
				callbackHandler.sendbroadcast("File not found exception. ");
				throw new FileNotFoundException();
			}
			errorString = new StringBuffer();
			fis = new FileInputStream(excelFile);
			workBook = new HSSFWorkbook(fis);
			parseXmlUtil = new ParseXMLUtil(xmlFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 开始从excel读取数据
	 *
	 * @throws ParseException
	 **/
	public void readExcelData() throws ParseException {
		int sheetSize = workBook.getNumberOfSheets();
		for (int i = 0; i < sheetSize; i++) {
			callbackHandler.sendbroadcast(String.format(
					"开始从excel读取数据，正在读取第 %d个工作薄。", i + 1));
			sheet = workBook.getSheetAt(i);
			String entityName = workBook.getSheetName(i);
			readSheetData(sheet, entityName);
		}
	}

	/**
	 * 读每个sheet页的数据
	 *
	 * @throws ParseException
	 **/
	public void readSheetData(HSSFSheet sheet, String entityName)
			throws ParseException {
		callbackHandler.sendbroadcast("开始读每个sheet页的数据...");
		int rowNumbers = sheet.getPhysicalNumberOfRows();
		Map ent = (Map) parseXmlUtil.getEntityMap().get(entityName);
		this.setCurEntityCode((String) ent.get("code"));
		if (rowNumbers == 0) {
			logger.debug("================excel中数据为空！");
			callbackHandler.sendbroadcast(ERROR_EXCEL_NULL);
			throw new ParseException(ERROR_EXCEL_NULL);
		}
		List colList = (List) parseXmlUtil.getColumnListMap().get(entityName);
		int xmlRowNum = colList.size();
		HSSFRow excelRow = sheet.getRow(0);
		int excelFirstRow = excelRow.getFirstCellNum();
		int excelLastRow = excelRow.getLastCellNum();
		if (xmlRowNum != (excelLastRow - excelFirstRow)) {
			logger.debug("==================xml列数与excel列数不相符，请检查");
			callbackHandler.sendbroadcast(ERROR_EXCEL_COLUMN_NOT_EQUAL);
			throw new ParseException(ERROR_EXCEL_COLUMN_NOT_EQUAL);
		}
		readSheetHeadData(sheet);
		readSheetColumnData(sheet, entityName);

	}

	/** 读取sheet页中的表头信息 **/
	@SuppressWarnings({ "unchecked", "static-access" })
	public void readSheetHeadData(HSSFSheet sheet) {
		callbackHandler.sendbroadcast("正在读取sheet页中的表头信息... ");
		Map headMap = new HashMap();
		curEntityHeadMap = new HashMap();
		curEntityColRequired = new HashMap();
		HSSFRow excelheadRow = sheet.getRow(0);
		int excelLastRow = excelheadRow.getLastCellNum();
		String headTitle = "";
		for (int i = 0; i < excelLastRow; i++) {
			HSSFCell cell = excelheadRow.getCell(i);
			headTitle = this.getStringCellValue(cell).trim();
			if (headTitle.endsWith("*")) {
				curEntityColRequired.put(this.getCurEntityCode() + "_"
						+ headTitle, true);
			} else {
				curEntityColRequired.put(this.getCurEntityCode() + "_"
						+ headTitle, false);
			}
			headMap.put(i, headTitle);
		}
		curEntityHeadMap.put(this.getCurEntityCode(), headMap);
	}

	/**
	 * 读取sheet页里面的数据
	 *
	 * @throws ParseException
	 **/
	@SuppressWarnings({ "unchecked", "static-access" })
	public void readSheetColumnData(HSSFSheet sheet, String entityName)
			throws ParseException {
		callbackHandler.sendbroadcast("正在读取sheet页里面的数据... ");
		HSSFRow excelheadRow = sheet.getRow(0);
		int excelLastcell = excelheadRow.getLastCellNum(); // excel总列数
		int excelRowNum = sheet.getLastRowNum(); // excel总行数
		Map headMap = (Map) this.getCurEntityHeadMap().get(
				this.getCurEntityCode());
		Map colMap = parseXmlUtil.getColumnMap();
		listDatas = new ArrayList();

		for (int i = 1; i < excelRowNum + 1; i++) {// 行循环
			HSSFRow columnRow = sheet.getRow(i);
			if (columnRow != null) {
				Map curRowCellMap = new HashMap();
				for (int j = 0; j < excelLastcell; j++) { // 列循环
					int cout = headMap.get(j).toString().indexOf("*");
					String headTitle = "";
					if (cout == -1) {
						headTitle = headMap.get(j).toString();
					} else {
						headTitle = headMap.get(j).toString()
								.substring(0, cout);
					}
					Map curColMap = (Map) colMap.get(entityName + "_"
							+ headTitle);
					String curColCode = (String) curColMap.get("code");
					String curColType = (String) curColMap.get("type");
					HSSFCell colCell = columnRow.getCell(j);
					String value = this.getStringCellValue(colCell);
					if (value != null) {
						value = value.trim();
					}
					String xmlColType = (String) curColMap.get("type");
					if (xmlColType.equals("int")) {
						int intVal = Integer.valueOf(value);
						curRowCellMap.put(curColCode, intVal); // 将这一行的数据以code-value的形式存入map
					} else if (xmlColType.equals("Date")) {
						Date date = null;
						try {
							date = DateUtils.formatDateStr(value, "yyyy-MM-dd");
						} catch (Exception e) {
							date = new Date();
							callbackHandler.sendbroadcast(String.format(
									"第%d行日期格式转换失败，采用当前时间。", i + i));
						}
						curRowCellMap.put(curColCode, date);
					} else {
						curRowCellMap.put(curColCode, value);
					}
					/** 验证cell数据 **/
					validateCellData(i + 1, j + 1, colCell, entityName,
							headTitle, curColType);
				}

				callbackHandler.sendbroadcast(String.format("第 %d行数据的有效性验证通过",
						i + 1));
				listDatas.add(curRowCellMap);
			}
		}

		if (this.getErrorString().length() > 0) {
			// 清理所有的缓存clearMap();现在暂时未清理
			String[] strArr = errorString.toString().split("<br>");
			for (String s : strArr) {
				logger.debug(s);
				callbackHandler.sendbroadcast(s);
			}

			throw new ParseException("有错误的数据，请改正后再导入。");
		}

	}

	/** 验证单元格数据 **/
	@SuppressWarnings("static-access")
	public void validateCellData(int curRow, int curCol, HSSFCell colCell,
			String entityName, String headName, String curColType) {
		// callbackHandler.sendbroadcast(String.format("验证第 %d 行，第 %d 列 数据的有效性 %n",
		// curRow, curCol));
		List rulList = (List) parseXmlUtil.getColumnRulesMap().get(
				entityName + "_" + headName);
		if (rulList != null && rulList.size() > 0) {
			for (int i = 0; i < rulList.size(); i++) {
				Map rulM = (Map) rulList.get(i);
				String rulName = (String) rulM.get("name");
				String rulMsg = (String) rulM.get("message");
				String cellValue = this.getStringCellValue(colCell).trim();
				if (rulName.equals(RULE_NAME_NULLABLE)) {
					if (cellValue.equals("") || cellValue == null) {
						errorString.append("第" + curRow + "行,第" + curCol + "列:"
								+ rulMsg + "<br>");
					}
				} else {
					// ////这里写其他的验证规则。。。
				}
			}
		}
	}

	public List<Object> parseExcelAsObjects(Class c) throws Exception {
		readExcelData();
		callbackHandler.sendbroadcast("正在转换数据...");
		List<Object> objs = new ArrayList();
		for (int i = 0; i < this.getListDatas().size(); i++) {
			Map excelCol = (Map) this.getListDatas().get(i); // 得到第 i 行的数据
			objs.add(BeanToMapUtil.convertMap(c, excelCol));
		}
		callbackHandler.sendbroadcast("数据转换成功！");
		return objs;
	}

	/**
	 * 获得单元格字符串
	 *
	 * @throws UnSupportedCellTypeException
	 */
	public static String getStringCellValue(HSSFCell cell) {
		if (cell == null) {
			return null;
		}

		String result = "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_BOOLEAN:
			result = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				java.text.SimpleDateFormat TIME_FORMATTER = new java.text.SimpleDateFormat(
						"yyyy-MM-dd");
				result = TIME_FORMATTER.format(cell.getDateCellValue());
			} else {
				double doubleValue = cell.getNumericCellValue();
				result = "" + doubleValue;
			}
			break;
		case HSSFCell.CELL_TYPE_STRING:
			if (cell.getRichStringCellValue() == null) {
				result = null;
			} else {
				result = cell.getRichStringCellValue().getString();
			}
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			result = null;
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			try {
				result = String.valueOf(cell.getNumericCellValue());
			} catch (Exception e) {
				result = cell.getRichStringCellValue().getString();
			}
			break;
		default:
			result = "";
		}

		return result;
	}

	public String getCurEntityCode() {
		return curEntityCode;
	}

	public void setCurEntityCode(String curEntityCode) {
		this.curEntityCode = curEntityCode;
	}

	public Map getCurEntityHeadMap() {
		return curEntityHeadMap;
	}

	public void setCurEntityHeadMap(Map curEntityHeadMap) {
		this.curEntityHeadMap = curEntityHeadMap;
	}

	public ParseXMLUtil getParseXmlUtil() {
		return parseXmlUtil;
	}

	public void setParseXmlUtil(ParseXMLUtil parseXmlUtil) {
		this.parseXmlUtil = parseXmlUtil;
	}

	public Map getCurEntityColRequired() {
		return curEntityColRequired;
	}

	public void setCurEntityColRequired(Map curEntityColRequired) {
		this.curEntityColRequired = curEntityColRequired;
	}

	public List getListDatas() {
		return listDatas;
	}

	public void setListDatas(List listDatas) {
		this.listDatas = listDatas;
	}

	public StringBuffer getErrorString() {
		return errorString;
	}

	public void setErrorString(StringBuffer errorString) {
		this.errorString = errorString;
	}

}
