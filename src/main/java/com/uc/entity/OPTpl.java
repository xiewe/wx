package com.uc.entity;

import java.util.Date;

public class OPTpl implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int opId;
    private String opName;
    private String opValue;
    private Date createTime;
    private Date modifyTime;

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
