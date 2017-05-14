package com.framework.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "sys_data_control")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "com.framework.entity.DataControl")
public class DataControl {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	@Length(max = 45)
	@Column(length = 45)
	private String name;

	@Length(max = 256)
	@Column(length = 256)
	private String description;

	// 数据极权限类型：OWNS-用户关联的数据；CUSTOM-自定义；ORGANIZATION-部门关联的数据；
	@Length(max = 45)
	@Column(length = 45)
	private String category;

	@Length(max = 45)
	@Column(length = 45)
	private String fieldName;

	@Length(max = 45)
	@Column(length = 45)
	private String fieldValue;

	// 取值说明：EQ-等于；LIKE-模糊查询；GT-大于；LT-小于；GTE-大于或等于；LTE-小于或等于；IN-在区间范围内；
	@Length(max = 45)
	@Column(length = 45)
	private String operator;

	@OneToMany(mappedBy = "dataControl", cascade = { CascadeType.PERSIST,
			CascadeType.REMOVE }, orphanRemoval = true)
	private List<RolePermissionDataControl> rolePermissionDataControls = new ArrayList<RolePermissionDataControl>();

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public List<RolePermissionDataControl> getRolePermissionDataControls() {
		return rolePermissionDataControls;
	}

	public void setRolePermissionDataControls(
			List<RolePermissionDataControl> rolePermissionDataControls) {
		this.rolePermissionDataControls = rolePermissionDataControls;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
