package com.framework.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.framework.entity.SysUser;

public interface SysUserDAO extends JpaRepository<SysUser, Long>,
		JpaSpecificationExecutor<SysUser> {

	SysUser findByUsername(String username);

	SysUser findByEmail(String email);

	SysUser findByUid(String uid);

	SysUser findByPhone(String phone);

	@Query(value = "select u from SysUser u where u.username = :s or u.email = :s or u.uid = :s or u.phone = :s")
	SysUser findByU(@Param("s") String s);

}