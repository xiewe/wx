package com.framework.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.framework.entity.Permission;
import com.framework.entity.Role;
import com.framework.utils.dwz.Page;

public interface PermissionService {

	Permission get(Long id);

	void saveOrUpdate(Permission permission);

	void delete(Long id);

	List<Permission> findAll(Page page);

	List<Permission> findAll();

	List<Permission> findByPageable(Specification<Permission> specification,
			Page page);

	Permission getTree();

	List<Permission> findByCategoryOrderByPriorityAsc(int category);

	void saveAssignRolePermission(Permission[] arrAssignPermissions, Role role);

	Permission getBySn(String sn);
}
