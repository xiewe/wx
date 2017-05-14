package com.framework.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "sys_user_role")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "com.framework.entity.UserRole")
public class UserRole {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roleId")
	private Role role;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 返回 role 的值
	 * 
	 * @return role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * 设置 role 的值
	 * 
	 * @param role
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * 返回 user 的值
	 * 
	 * @return user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 设置 user 的值
	 * 
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}
}
