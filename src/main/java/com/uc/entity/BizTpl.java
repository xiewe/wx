package com.uc.entity;

public class BizTpl implements java.io.Serializable {

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

	public static final String DefaultVoLTETpl = "100100,100200,100301,100302,100303,100400,100500,100600,100700,100800,100900,101000";
	public static final String DefaultLTETpl = "";
	public static final String DefaultSIPTpl = "100100,100200,100301,100302,100303,100400,100500,100600,100700,100800,100900,101000";
	public static final String DefaultDCTpl = "100100,100200,100301,100302,100303,100400,100500,100600,100700,100800,100900,101000";
	public static final String DefaultPDTTpl = "100100,100200,100301,100302,100303,100400,100500,100600,100700,100800,100900,101000";
	public static final String DefaultPNASTpl = "100100,100200,100301,100302,100303,100400,100500,100600,100700,100800,100900,101000";
	public static final String DefaultCameraTpl = "100100,100200,100301,100302,100303,100400,100500,100600,100900,101000";
	public static final String DefaultPOCTpl = "100100,100200,100301,100302,100303,100400,100500,100600,100700,100800,100900,101000";

	private int bizTplId;
	private String bizTplName;
	private int userType;
	private int bizSwitcher;
	private String bizIdList;
	private int userPriority;

	public int getBizTplId() {
		return bizTplId;
	}

	public void setBizTplId(int bizTplId) {
		this.bizTplId = bizTplId;
	}

	public String getBizTplName() {
		return bizTplName;
	}

	public void setBizTplName(String bizTplName) {
		this.bizTplName = bizTplName;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public int getBizSwitcher() {
		return bizSwitcher;
	}

	public void setBizSwitcher(int bizSwitcher) {
		this.bizSwitcher = bizSwitcher;
	}

	public String getBizIdList() {
		return bizIdList;
	}

	public void setBizIdList(String bizIdList) {
		this.bizIdList = bizIdList;
	}

	public int getUserPriority() {
		return userPriority;
	}

	public void setUserPriority(int userPriority) {
		this.userPriority = userPriority;
	}

}
