package com.uc.entity;

public class BlackWhiteList implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int listType;
	private int imeisv;
	private String imsi;

	public int getListType() {
		return listType;
	}

	public void setListType(int listType) {
		this.listType = listType;
	}

	public int getImeisv() {
		return imeisv;
	}

	public void setImeisv(int imeisv) {
		this.imeisv = imeisv;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

}
