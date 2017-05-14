package com.framework.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.framework.entity.UserRole;

public interface UserRoleDAO extends JpaRepository<UserRole, Long>,
		JpaSpecificationExecutor<UserRole> {
	List<UserRole> findByUserId(Long userId);
}