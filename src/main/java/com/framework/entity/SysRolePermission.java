package com.framework.entity;
// Generated 2017-5-25 22:34:35 by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * SysRolePermission generated by hbm2java
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "sys_role_permission", catalog = "wx")
public class SysRolePermission implements java.io.Serializable {

	private SysRolePermissionId id;
	private SysMenu sysMenu;
	private SysMenuClass sysMenuClass;
	private SysRole sysRole;

	public SysRolePermission() {
	}

	public SysRolePermission(SysRolePermissionId id, SysMenu sysMenu, SysMenuClass sysMenuClass, SysRole sysRole) {
		this.id = id;
		this.sysMenu = sysMenu;
		this.sysMenuClass = sysMenuClass;
		this.sysRole = sysRole;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "roleId", column = @Column(name = "role_id", nullable = false)),
			@AttributeOverride(name = "menuId", column = @Column(name = "menu_id", nullable = false)),
			@AttributeOverride(name = "menuClassId", column = @Column(name = "menu_class_id", nullable = false)) })
	public SysRolePermissionId getId() {
		return this.id;
	}

	public void setId(SysRolePermissionId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", nullable = false, insertable = false, updatable = false)
	public SysMenu getSysMenu() {
		return this.sysMenu;
	}

	public void setSysMenu(SysMenu sysMenu) {
		this.sysMenu = sysMenu;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_class_id", nullable = false, insertable = false, updatable = false)
	public SysMenuClass getSysMenuClass() {
		return this.sysMenuClass;
	}

	public void setSysMenuClass(SysMenuClass sysMenuClass) {
		this.sysMenuClass = sysMenuClass;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false, insertable = false, updatable = false)
	public SysRole getSysRole() {
		return this.sysRole;
	}

	public void setSysRole(SysRole sysRole) {
		this.sysRole = sysRole;
	}

}
