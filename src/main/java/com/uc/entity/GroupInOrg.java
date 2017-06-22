package com.uc.entity;

public class GroupInOrg implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String orgName;
	private String groupName;
	private String groupNo;

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

}
