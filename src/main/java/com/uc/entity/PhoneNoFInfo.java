package com.uc.entity;

public class PhoneNoFInfo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int orgId;
	/**
	 * 1 - user; 2 - group
	 */
	private int phoneNoType;
	private String phoneNoStart;
	private int numbers;
	private int usedCount;

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public int getPhoneNoType() {
		return phoneNoType;
	}

	public void setPhoneNoType(int phoneNoType) {
		this.phoneNoType = phoneNoType;
	}

	public String getPhoneNoStart() {
		return phoneNoStart;
	}

	public void setPhoneNoStart(String phoneNoStart) {
		this.phoneNoStart = phoneNoStart;
	}

	public int getNumbers() {
		return numbers;
	}

	public void setNumbers(int numbers) {
		this.numbers = numbers;
	}

	public int getUsedCount() {
		return usedCount;
	}

	public void setUsedCount(int usedCount) {
		this.usedCount = usedCount;
	}

}
