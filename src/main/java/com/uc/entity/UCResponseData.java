package com.uc.entity;

import java.io.Serializable;

public class UCResponseData<T> implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	private int flag;
	private String errCode;
	private String errMsg;
	private int pageSize;
	private int pageIndex;
	private int totalQueryCount;
	private T data;

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getTotalQueryCount() {
		return totalQueryCount;
	}

	public void setTotalQueryCount(int totalQueryCount) {
		this.totalQueryCount = totalQueryCount;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
