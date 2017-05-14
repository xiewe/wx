package com.framework.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.framework.entity.RolePermissionDataControl;
import com.framework.utils.dwz.Page;

public interface RolePermissionDataControlService {

	RolePermissionDataControl get(Long id);

	void saveOrUpdate(RolePermissionDataControl rolePermissionDataControl);

	void delete(Long id);

	List<RolePermissionDataControl> findAll(Page page);

	List<RolePermissionDataControl> findByPageable(
			Specification<RolePermissionDataControl> specification, Page page);

	/**
	 * @param list
	 */
	void save(List<RolePermissionDataControl> list);

	/**
	 * @param list
	 */
	void delete(List<RolePermissionDataControl> list);

	/**
	 * @param id
	 * @return
	 */
	List<RolePermissionDataControl> findByRolePermissionRoleId(Long id);
}
