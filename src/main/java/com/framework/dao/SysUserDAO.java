package com.framework.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.framework.entity.SysUser;

public interface SysUserDAO extends JpaRepository<SysUser, Long>,
		JpaSpecificationExecutor<SysUser> {

}