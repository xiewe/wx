package com.uc.entity;

import java.util.Date;

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
    private Date createTime;
    private Date modifyTime;

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
