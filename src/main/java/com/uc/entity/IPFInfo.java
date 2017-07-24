package com.uc.entity;

import java.util.Date;

public class IPFInfo implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String ipFragment;
    private String ipMask;
    private String desc;
    private int usedCount;
    private Date createTime;
    private Date modifyTime;

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
