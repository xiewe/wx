package com.uc.entity;

public class BizEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public static final String VoiceSingleCall = "100100";
    public static final String VideoSingleCall = "100200";
    public static final String LocalCallOut = "100301";
    public static final String CountryCallOut = "100302";
    public static final String InternationalCallOut = "100303";
    public static final String ShortMessage = "100400";
    public static final String ShortData = "100500";
    public static final String InstantMessage = "100600";
    public static final String VoiceRecording = "100700";
    public static final String VideoRecording = "100800";
    public static final String VoiceGroupCall = "100900";
    public static final String VideoGroupCall = "101000";

    public BizEntity(String bizId, String bizName) {
        super();
        this.bizId = bizId;
        this.bizName = bizName;
    }

    private String bizId;
    private String bizName;

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getBizName() {
        return bizName;
    }

    public void setBizName(String bizName) {
        this.bizName = bizName;
    }

}
