package com.uc.entity;

import java.util.Date;

public class UserStatusInfo implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int orgId;
    private String orgName;
    private int userType;
    private String subNo;
    private String imsi;
    private int attachStatus;
    private int registerStatus;
    private int guti;
    private int tai;
    private int accessWay;
    private String currIp;
    private String currAPN;
    private Date createTime;
    private Date modifyTime;

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

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getSubNo() {
        return subNo;
    }

    public void setSubNo(String subNo) {
        this.subNo = subNo;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public int getAttachStatus() {
        return attachStatus;
    }

    public void setAttachStatus(int attachStatus) {
        this.attachStatus = attachStatus;
    }

    public int getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(int registerStatus) {
        this.registerStatus = registerStatus;
    }

    public int getGuti() {
        return guti;
    }

    public void setGuti(int guti) {
        this.guti = guti;
    }

    public int getTai() {
        return tai;
    }

    public void setTai(int tai) {
        this.tai = tai;
    }

    public int getAccessWay() {
        return accessWay;
    }

    public void setAccessWay(int accessWay) {
        this.accessWay = accessWay;
    }

    public String getCurrIp() {
        return currIp;
    }

    public void setCurrIp(String currIp) {
        this.currIp = currIp;
    }

    public String getCurrAPN() {
        return currAPN;
    }

    public void setCurrAPN(String currAPN) {
        this.currAPN = currAPN;
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

}
