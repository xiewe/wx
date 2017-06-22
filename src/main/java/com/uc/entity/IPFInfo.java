package com.uc.entity;

public class IPFInfo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String ipFragment;
	private String ipMask;
	private String desc;
	private int usedCount;

	public String getIpFragment() {
		return ipFragment;
	}

	public void setIpFragment(String ipFragment) {
		this.ipFragment = ipFragment;
	}

	public String getIpMask() {
		return ipMask;
	}

	public void setIpMask(String ipMask) {
		this.ipMask = ipMask;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getUsedCount() {
		return usedCount;
	}

	public void setUsedCount(int usedCount) {
		this.usedCount = usedCount;
	}

}
