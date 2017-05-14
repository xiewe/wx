package com.framework.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.framework.entity.RolePermission;

public interface RolePermissionDAO extends JpaRepository<RolePermission, Long>,
		JpaSpecificationExecutor<RolePermission> {

	/**
	 * @param id
	 * @return
	 */
	List<RolePermission> findByRoleId(Long roleId);

	RolePermission findByRoleIdAndPermissionId(Long roleId, Long permissionId);

}