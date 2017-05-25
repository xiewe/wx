package com.framework.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.framework.entity.Role;
import com.framework.utils.dwz.Page;

public interface RoleService {
	Role get(Long id);

	Role getByName(String name);

	void saveOrUpdate(Role role);

	void delete(Long id);

	List<Role> findAll(Page page);

	List<Role> findByPageable(Specification<Role> specification, Page page);

}
