package com.uc.entity;

public class IPInfo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String ip;
	private int status;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
