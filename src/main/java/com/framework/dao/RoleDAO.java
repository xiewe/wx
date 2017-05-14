package com.framework.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.framework.entity.Role;

public interface RoleDAO extends JpaRepository<Role, Long>,
		JpaSpecificationExecutor<Role> {
	Role getByName(String name);
}