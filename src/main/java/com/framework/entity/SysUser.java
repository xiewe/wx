package com.framework.entity;
// Generated 2017-5-25 22:34:35 by Hibernate Tools 4.3.1.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SysUser generated by hbm2java
 */
@Entity
@Table(name = "sys_user", catalog = "wx")
public class SysUser implements java.io.Serializable {

	private Long id;
	private SysOrganization sysOrganization;
	private SysRole sysRole;
	private String uid;
	private String username;
	private String password;
	private String salt;
	private String realname;
	private Integer gender;
	private String photo;
	private String phone;
	private String countryCode;
	private String nationality;
	private String individualId;
	private String email;
	private String address;
	private String selfIntro;
	private Integer isVip;
	private Integer status;
	private Integer utype;
	private String gps;
	private Double longitude;
	private Double latitude;
	private String userAgent;
	private Date lastLogin;
	private Date createTime;
	private Date modifyTime;

	public SysUser() {
	}

	public SysUser(String username) {
		this.username = username;
	}

	public SysUser(SysOrganization sysOrganization, SysRole sysRole, String uid, String username, String password,
			String salt, String realname, Integer gender, String photo, String phone, String countryCode,
			String nationality, String individualId, String email, String address, String selfIntro, Integer isVip,
			Integer status, Integer utype, String gps, Double longitude, Double latitude, String userAgent,
			Date lastLogin, Date createTime, Date modifyTime) {
		this.sysOrganization = sysOrganization;
		this.sysRole = sysRole;
		this.uid = uid;
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.realname = realname;
		this.gender = gender;
		this.photo = photo;
		this.phone = phone;
		this.countryCode = countryCode;
		this.nationality = nationality;
		this.individualId = individualId;
		this.email = email;
		this.address = address;
		this.selfIntro = selfIntro;
		this.isVip = isVip;
		this.status = status;
		this.utype = utype;
		this.gps = gps;
		this.longitude = longitude;
		this.latitude = latitude;
		this.userAgent = userAgent;
		this.lastLogin = lastLogin;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_id")
	public SysOrganization getSysOrganization() {
		return this.sysOrganization;
	}

	public void setSysOrganization(SysOrganization sysOrganization) {
		this.sysOrganization = sysOrganization;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	public SysRole getSysRole() {
		return this.sysRole;
	}

	public void setSysRole(SysRole sysRole) {
		this.sysRole = sysRole;
	}

	@Column(name = "uid", length = 30)
	public String getUid() {
		return this.uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Column(name = "username", nullable = false, length = 32)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", length = 64)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "salt", length = 45)
	public String getSalt() {
		return this.salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@Column(name = "realname", length = 600)
	public String getRealname() {
		return this.realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	@Column(name = "gender")
	public Integer getGender() {
		return this.gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	@Column(name = "photo", length = 300)
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Column(name = "phone", length = 32)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "country_code", length = 8)
	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Column(name = "nationality", length = 30)
	public String getNationality() {
		return this.nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	@Column(name = "individual_id", length = 32)
	public String getIndividualId() {
		return this.individualId;
	}

	public void setIndividualId(String individualId) {
		this.individualId = individualId;
	}

	@Column(name = "email", length = 100)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "address", length = 300)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "self_intro", length = 1000)
	public String getSelfIntro() {
		return this.selfIntro;
	}

	public void setSelfIntro(String selfIntro) {
		this.selfIntro = selfIntro;
	}

	@Column(name = "is_vip")
	public Integer getIsVip() {
		return this.isVip;
	}

	public void setIsVip(Integer isVip) {
		this.isVip = isVip;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "utype")
	public Integer getUtype() {
		return this.utype;
	}

	public void setUtype(Integer utype) {
		this.utype = utype;
	}

	@Column(name = "gps", length = 60)
	public String getGps() {
		return this.gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}

	@Column(name = "longitude", precision = 22, scale = 0)
	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Column(name = "latitude", precision = 22, scale = 0)
	public Double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@Column(name = "user_agent", length = 200)
	public String getUserAgent() {
		return this.userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_login", length = 19)
	public Date getLastLogin() {
		return this.lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_time", length = 19)
	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

}