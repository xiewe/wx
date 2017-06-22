package com.uc.entity;

public class OPTpl implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int opId;
	private String opName;
	private String opValue;

	public int getOpId() {
		return opId;
	}

	public void setOpId(int opId) {
		this.opId = opId;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public String getOpValue() {
		return opValue;
	}

	public void setOpValue(String opValue) {
		this.opValue = opValue;
	}

}
