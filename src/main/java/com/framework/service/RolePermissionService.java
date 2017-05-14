package com.framework.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.framework.entity.RolePermission;
import com.framework.utils.dwz.Page;

public interface RolePermissionService {
	RolePermission get(Long id);

	void saveOrUpdate(RolePermission rolePermission);

	void delete(Long id);

	List<RolePermission> findAll(Page page);

	List<RolePermission> findByPageable(
			Specification<RolePermission> specification, Page page);

	/**
	 * @param id
	 * @return
	 */
	List<RolePermission> findByRoleId(Long id);

	RolePermission findByRoleIdAndPermissionId(Long roleId, Long permissionId);

	/**
	 * @param newRList
	 */
	void save(List<RolePermission> newRList);

	void delete(List<RolePermission> delRList);
}
