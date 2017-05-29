package com.framework.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.framework.entity.SysRolePermission;
import com.framework.utils.page.Page;

public interface SysRolePermissionService {
	SysRolePermission get(Integer id);

	SysRolePermission saveOrUpdate(SysRolePermission o);

	void delete(Integer id);

	List<SysRolePermission> findAll(Page page);

	List<SysRolePermission> findByPageable(
			Specification<SysRolePermission> specification, Page page);
}
