package com.framework.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.framework.entity.SysRolePermission;
import com.framework.utils.pager.Pager;

public interface SysRolePermissionService {
	SysRolePermission get(Integer id);

	SysRolePermission saveOrUpdate(SysRolePermission o);

	void delete(Integer id);

	List<SysRolePermission> findAll();

	List<SysRolePermission> findAll(Specification<SysRolePermission> specification);

	List<SysRolePermission> findAll(Pager page);

	List<SysRolePermission> findByPageable(Specification<SysRolePermission> specification, Pager page);
}
