package com.framework.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.framework.entity.User;

public interface UserDAO extends JpaRepository<User, Long>,
		JpaSpecificationExecutor<User> {

	User getByUsername(String username);

	List<User> findByOrganizationId(Long id);

	List<User> findByOrganizationIdIn(Long[] ids);
}