package com.framework.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.framework.entity.RolePermissionDataControl;

public interface RolePermissionDataControlDAO extends
		JpaRepository<RolePermissionDataControl, Long>,
		JpaSpecificationExecutor<RolePermissionDataControl> {

	List<RolePermissionDataControl> findByRolePermissionRoleId(Long id);

}