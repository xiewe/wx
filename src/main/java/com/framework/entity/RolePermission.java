package com.framework.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "sys_role_permission")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "com.framework.entity.RolePermission")
public class RolePermission {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roleId")
	private Role role;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "permissionId")
	private Permission permission;

	@OneToMany(mappedBy = "rolePermission", cascade = { CascadeType.PERSIST,
			CascadeType.REMOVE }, orphanRemoval = true)
	private List<RolePermissionDataControl> rolePermissionDataControls = new ArrayList<RolePermissionDataControl>();

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
	 * 返回 permission 的值
	 * 
	 * @return permission
	 */
	public Permission getPermission() {
		return permission;
	}

	/**
	 * 设置 permission 的值
	 * 
	 * @param permission
	 */
	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	public List<RolePermissionDataControl> getRolePermissionDataControls() {
		return rolePermissionDataControls;
	}

	public void setRolePermissionDataControls(
			List<RolePermissionDataControl> rolePermissionDataControls) {
		this.rolePermissionDataControls = rolePermissionDataControls;
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, false);
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	// @Override
	// public int hashCode() {
	// return HashCodeBuilder.reflectionHashCode(this, false);
	// }
}
