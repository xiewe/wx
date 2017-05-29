package com.framework.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.framework.entity.SysRole;

public interface SysRoleDAO extends
		JpaRepository<SysRole, Integer>,
		JpaSpecificationExecutor<SysRole> {

}