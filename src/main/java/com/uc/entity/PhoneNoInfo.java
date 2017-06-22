package com.uc.entity;

public class PhoneNoInfo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String phoneNo;
	private int status;

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
