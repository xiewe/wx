package com.framework.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "sys_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "com.framework.entity.User")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	@Length(max = 32)
	@Column(length = 32, nullable = false, unique = true, updatable = false)
	private String username;

	@Column(length = 64, nullable = false)
	private String password;

	@Transient
	private String plainPassword;

	@Column(length = 32, nullable = false)
	private String salt;// 简单的密码散列

	/**
	 * 用户类型：0-管理后台用户；1-系统用户不能删除；2-终端注册用户
	 */
	@NotNull
	@Column(length = 3, nullable = false)
	private int category = 0;

	@Length(max = 32)
	@Column(length = 32)
	private String phone;

	@Email
	@Length(max = 300)
	@Column(length = 300)
	private String email;

	/**
	 * 状态 1-disabled，0-enabled
	 */
	@NotNull
	@Range(min = 0, max = 999)
	@Column(length = 3, nullable = false)
	private int status = 0;

	/**
	 * 帐号创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date createTime;


	@Transient
	private String loginIpAddress;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 返回 username 的值
	 *
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置 username 的值
	 *
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 返回 password 的值
	 *
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设置 password 的值
	 *
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 返回 createTime 的值
	 *
	 * @return createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置 createTime 的值
	 *
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 返回 status 的值
	 *
	 * @return status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置 status 的值
	 *
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 返回 plainPassword 的值
	 *
	 * @return plainPassword
	 */
	public String getPlainPassword() {
		return plainPassword;
	}

	/**
	 * 设置 plainPassword 的值
	 *
	 * @param plainPassword
	 */
	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	/**
	 * 返回 email 的值
	 *
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 设置 email 的值
	 *
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 返回 salt 的值
	 *
	 * @return salt
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * 设置 salt 的值
	 *
	 * @param salt
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}



	/**
	 * 返回 phone 的值
	 *
	 * @return phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置 phone 的值
	 *
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getLoginIpAddress() {
		return loginIpAddress;
	}

	public void setLoginIpAddress(String loginIpAddress) {
		this.loginIpAddress = loginIpAddress;
	}

	@Override
	public String toString() {
		// return ToStringBuilder.reflectionToString(this);
		return username;
	}
}
