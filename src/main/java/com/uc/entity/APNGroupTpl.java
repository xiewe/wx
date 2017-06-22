package com.uc.entity;

public class APNGroupTpl implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int apnGroupId;
	private String apnGroupName;

	/**
	 * 最大请求上行带宽
	 */
	private int maxRequestedBwUl;
	/**
	 * 最大请求下行带宽
	 */
	private int maxRequestedBwDl;

	private String apnIdList;

	/**
	 * Default Context Identifier
	 */
	private int ci;
	/**
	 * APN模版配置通知类型
	 */
	private int apnNotifiedType;

	public int getApnGroupId() {
		return apnGroupId;
	}

	public void setApnGroupId(int apnGroupId) {
		this.apnGroupId = apnGroupId;
	}

	public String getApnGroupName() {
		return apnGroupName;
	}

	public void setApnGroupName(String apnGroupName) {
		this.apnGroupName = apnGroupName;
	}

	public int getMaxRequestedBwUl() {
		return maxRequestedBwUl;
	}

	public void setMaxRequestedBwUl(int maxRequestedBwUl) {
		this.maxRequestedBwUl = maxRequestedBwUl;
	}

	public int getMaxRequestedBwDl() {
		return maxRequestedBwDl;
	}

	public void setMaxRequestedBwDl(int maxRequestedBwDl) {
		this.maxRequestedBwDl = maxRequestedBwDl;
	}

	public String getApnIdList() {
		return apnIdList;
	}

	public void setApnIdList(String apnIdList) {
		this.apnIdList = apnIdList;
	}

	public int getCi() {
		return ci;
	}

	public void setCi(int ci) {
		this.ci = ci;
	}

	public int getApnNotifiedType() {
		return apnNotifiedType;
	}

	public void setApnNotifiedType(int apnNotifiedType) {
		this.apnNotifiedType = apnNotifiedType;
	}

}
