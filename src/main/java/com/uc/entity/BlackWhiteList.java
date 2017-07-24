package com.uc.entity;

import java.util.Date;

public class BlackWhiteList implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int listType;
    private int imeisv;
    private String imsi;
    private Date createTime;
    private Date modifyTime;

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
