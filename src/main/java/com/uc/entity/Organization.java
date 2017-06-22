package com.uc.entity;

public class Organization implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int orgId;
	private String orgName;
	private int maxUserNo;
	private int maxGroupNo;
	private String admin;
	private String password;
	private String emgySingleCallNo;
	private String DCSId;
	private String comments;
	private int currUserCount;

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public int getMaxUserNo() {
		return maxUserNo;
	}

	public void setMaxUserNo(int maxUserNo) {
		this.maxUserNo = maxUserNo;
	}

	public int getMaxGroupNo() {
		return maxGroupNo;
	}

	public void setMaxGroupNo(int maxGroupNo) {
		this.maxGroupNo = maxGroupNo;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmgySingleCallNo() {
		return emgySingleCallNo;
	}

	public void setEmgySingleCallNo(String emgySingleCallNo) {
		this.emgySingleCallNo = emgySingleCallNo;
	}

	public String getDCSId() {
		return DCSId;
	}

	public void setDCSId(String dCSId) {
		DCSId = dCSId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getCurrUserCount() {
		return currUserCount;
	}

	public void setCurrUserCount(int currUserCount) {
		this.currUserCount = currUserCount;
	}

}
