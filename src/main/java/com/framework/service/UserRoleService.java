package com.framework.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.framework.entity.UserRole;
import com.framework.utils.dwz.Page;

public interface UserRoleService {
	UserRole get(Long id);

	void saveOrUpdate(UserRole userRole);

	void delete(Long id);

	List<UserRole> findAll(Page page);

	List<UserRole> findByPageable(Specification<UserRole> specification,
			Page page);

	List<UserRole> findByUserId(Long userId);
}
