package com.framework.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.framework.entity.SysRole;
import com.framework.entity.SysRolePermission;

public interface SysRolePermissionDAO extends
		JpaRepository<SysRolePermission, Integer>,
		JpaSpecificationExecutor<SysRolePermission> {
	List<SysRolePermission> findBySysRoleOrderBySysMenuClass(SysRole sysRole);
}