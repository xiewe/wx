package com.uc.entity;

import java.util.Date;

public class IMSIInfo implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String imsi;
    private int opId;
    private String k;
    private int status;
    private Date createTime;
    private Date modifyTime;

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public int getOpId() {
        return opId;
    }

    public void setOpId(int opId) {
        this.opId = opId;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

	@Override
	public String toString() {
		return "IMSIInfo [imsi=" + imsi + ", opId=" + opId + ", k=" + k
				+ ", status=" + status + ", createTime=" + createTime
				+ ", modifyTime=" + modifyTime + "]";
	}
    
    

}
