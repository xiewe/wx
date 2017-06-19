package com.framework.entity;
// Generated 2017-5-27 22:17:08 by Hibernate Tools 4.3.1.Final

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * SysRole generated by hbm2java
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "sys_role", catalog = "uc")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "com.framework.entity.SysRole")
public class SysRole implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6919886877780059392L;
	private Integer id;
	private String name;
	private String description;
	private Set<SysUser> sysUsers = new HashSet<SysUser>(0);
	private Set<SysRolePermission> sysRolePermissions = new HashSet<SysRolePermission>(0);

	public SysRole() {
	}

	public SysRole(String name) {
		this.name = name;
	}

	public SysRole(String name, String description, Set<SysUser> sysUsers, Set<SysRolePermission> sysRolePermissions) {
		this.name = name;
		this.description = description;
		this.sysUsers = sysUsers;
		this.sysRolePermissions = sysRolePermissions;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false, length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", length = 256)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sysRole")
	public Set<SysUser> getSysUsers() {
		return this.sysUsers;
	}

	public void setSysUsers(Set<SysUser> sysUsers) {
		this.sysUsers = sysUsers;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sysRole")
	public Set<SysRolePermission> getSysRolePermissions() {
		return this.sysRolePermissions;
	}

	public void setSysRolePermissions(Set<SysRolePermission> sysRolePermissions) {
		this.sysRolePermissions = sysRolePermissions;
	}

}
