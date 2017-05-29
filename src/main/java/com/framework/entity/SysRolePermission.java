package com.framework.entity;
// Generated 2017-5-27 22:17:08 by Hibernate Tools 4.3.1.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * SysRolePermission generated by hbm2java
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "sys_role_permission", catalog = "wx")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "com.framework.entity.SysRolePermission")
public class SysRolePermission implements java.io.Serializable {

	private Integer id;
	private SysMenu sysMenu;
	private SysMenuClass sysMenuClass;
	private SysRole sysRole;

	public SysRolePermission() {
	}

	public SysRolePermission(SysMenu sysMenu, SysMenuClass sysMenuClass, SysRole sysRole) {
		this.sysMenu = sysMenu;
		this.sysMenuClass = sysMenuClass;
		this.sysRole = sysRole;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", nullable = false)
	public SysMenu getSysMenu() {
		return this.sysMenu;
	}

	public void setSysMenu(SysMenu sysMenu) {
		this.sysMenu = sysMenu;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_class_id", nullable = false)
	public SysMenuClass getSysMenuClass() {
		return this.sysMenuClass;
	}

	public void setSysMenuClass(SysMenuClass sysMenuClass) {
		this.sysMenuClass = sysMenuClass;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	public SysRole getSysRole() {
		return this.sysRole;
	}

	public void setSysRole(SysRole sysRole) {
		this.sysRole = sysRole;
	}

}
