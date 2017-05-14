package com.framework.entity;

import java.io.Serializable;

public class GeneralResponseData<T> implements Serializable, Cloneable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int status;

    private String errCode;

    private String errMsg;

    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
