package com.framework.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "sys_permission")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "com.framework.entity.Permission")
public class Permission implements Comparable<Permission> {

	public final static String PERMISSION_CREATE = "save";

	public final static String PERMISSION_READ = "view";

	public final static String PERMISSION_UPDATE = "edit";

	public final static String PERMISSION_DELETE = "delete";

	/**
	 * 安全级别：SYSTEM-系统级不能删除；GENERAL-一般普通级别可删除
	 */
	public static enum SecurityLevel {
		SYSTEM, GENERAL
	}

	@Transient
	private boolean checked; // ztree json 属性，是否选中

	@Transient
	private boolean open;// ztree json 属性，是否展开

	@Transient
	private Long pId;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	@Length(max = 64)
	@Column(length = 64, nullable = false)
	private String name;

	/**
	 * 对应的模块全类名
	 */
	@Column(length = 100)
	private String className;

	/**
	 * 模块的入口地址
	 */
	@JsonIgnore
	@NotBlank
	@Length(max = 256)
	@Column(length = 256, nullable = false)
	private String url = "#";

	@Length(max = 256)
	@Column(length = 256)
	private String description;

	/**
	 * 标志符，用于授权名称（类似module:save or module:save:123）
	 */
	@NotBlank
	@Length(max = 32)
	@Column(length = 32, nullable = false, unique = true)
	private String sn;

	/**
	 * 类型：0-文件夹菜单; 1-菜单模块可拥有子级; 2-CUDR等操作，最底层不能再有子级;
	 */
	@Column(length = 3)
	private int category = 0;

	@Enumerated(EnumType.STRING)
	@Column(name = "security_level")
	private SecurityLevel securityLevel = SecurityLevel.GENERAL;

	/**
	 * 模块的排序号,越小优先级越高
	 */
	@NotNull
	@Range(min = 1, max = 999)
	@Column(length = 3, nullable = false)
	private Integer priority = 999;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentId")
	private Permission parent;

	@JsonIgnore
	@OneToMany(mappedBy = "parent", cascade = { CascadeType.PERSIST,
			CascadeType.REMOVE })
	@OrderBy("priority ASC")
	private List<Permission> children = new ArrayList<Permission>();

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
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * 返回 url 的值
	 * 
	 * @return url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置 url 的值
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
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
	 * 返回 priority 的值
	 * 
	 * @return priority
	 */
	public Integer getPriority() {
		return priority;
	}

	/**
	 * 设置 priority 的值
	 * 
	 * @param priority
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	/**
	 * 返回 parent 的值
	 * 
	 * @return parent
	 */
	public Permission getParent() {
		return parent;
	}

	/**
	 * 设置 parent 的值
	 * 
	 * @param parent
	 */
	public void setParent(Permission parent) {
		this.parent = parent;
	}

	/**
	 * 返回 children 的值
	 * 
	 * @return children
	 */
	public List<Permission> getChildren() {
		return children;
	}

	/**
	 * 设置 children 的值
	 * 
	 * @param children
	 */
	public void setChildren(List<Permission> children) {
		this.children = children;
	}

	/**
	 * 返回 sn 的值
	 * 
	 * @return sn
	 */
	public String getSn() {
		return sn;
	}

	/**
	 * 设置 sn 的值
	 * 
	 * @param sn
	 */
	public void setSn(String sn) {
		this.sn = sn;
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

	public SecurityLevel getSecurityLevel() {
		return securityLevel;
	}

	public void setSecurityLevel(SecurityLevel securityLevel) {
		this.securityLevel = securityLevel;
	}

	@Override
	public int compareTo(Permission m) {
		if (m == null) {
			return -1;
		} else if (m == this) {
			return 0;
		} else if (this.priority < m.getPriority()) {
			return -1;
		} else if (this.priority > m.getPriority()) {
			return 1;
		}

		return 0;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
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

	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean getOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public Long getpId() {
		return parent == null ? null : parent.getId();
	}

	public void setpId(Long pId) {
		this.pId = pId;
	}

}
