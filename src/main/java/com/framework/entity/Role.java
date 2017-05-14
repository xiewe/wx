package com.framework.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "sys_role")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "com.framework.entity.Role")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	@Length(max = 64)
	@Column(length = 64, nullable = false, unique = true)
	private String name;

	@Length(max = 256)
	@Column(length = 256)
	private String description;

	@Column(length = 3)
	private int category = 0;

	@OneToMany(mappedBy = "role", cascade = { CascadeType.PERSIST,
			CascadeType.REMOVE }, orphanRemoval = true)
	private Set<UserRole> userRoles = new HashSet<UserRole>();

	@OneToMany(mappedBy = "role", cascade = { CascadeType.PERSIST,
			CascadeType.REMOVE }, orphanRemoval = true)
	private List<RolePermission> rolePermissions = new ArrayList<RolePermission>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 返回 name 的值
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置 name 的值
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回 description 的值
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置 description 的值
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 返回 category 的值
	 * 
	 * @return category
	 */
	public int getCategory() {
		return category;
	}

	/**
	 * 设置 category 的值
	 * 
	 * @param category
	 */
	public void setCategory(int category) {
		this.category = category;
	}

	/**
	 * 返回 userRoles 的值
	 * 
	 * @return userRoles
	 */
	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	/**
	 * 设置 userRoles 的值
	 * 
	 * @param userRoles
	 */
	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	/**
	 * 返回 rolePermissions 的值
	 * 
	 * @return rolePermissions
	 */
	public List<RolePermission> getRolePermissions() {
		return rolePermissions;
	}

	/**
	 * 设置 rolePermissions 的值
	 * 
	 * @param rolePermissions
	 */
	public void setRolePermissions(List<RolePermission> rolePermissions) {
		this.rolePermissions = rolePermissions;
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
