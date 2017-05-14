package com.framework.utils.persistence;

/**
 * 关键字类
 * 
 * @author liangjian
 *
 */
public class KeywordFilter {

	public KeywordFilter(String fieldName) {
		super();
		this.fieldName = fieldName;
	}

	public KeywordFilter(String fieldName, DataType dataType) {
		super();
		this.fieldName = fieldName;
		this.dataType = dataType;
	}

	public enum DataType {
		INT, VARCHAR, DATE
	}

	private String fieldName;
	private DataType dataType = DataType.VARCHAR;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

}
