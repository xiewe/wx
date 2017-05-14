package com.framework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.RolePermissionDAO;
import com.framework.entity.RolePermission;
import com.framework.service.RolePermissionService;
import com.framework.utils.dwz.Page;
import com.framework.utils.dwz.PageUtils;

@Service
@Transactional
public class RolePermissionServiceImpl implements RolePermissionService {

	@Autowired
	private RolePermissionDAO rolePermissionDAO;

	@Override
	public RolePermission get(Long id) {
		return rolePermissionDAO.findOne(id);
	}

	@Override
	public void saveOrUpdate(RolePermission rolePermission) {
		rolePermissionDAO.save(rolePermission);
	}

	@Override
	public void delete(Long id) {
		rolePermissionDAO.delete(id);
	}

	@Override
	public List<RolePermission> findAll(Page page) {
		org.springframework.data.domain.Page<RolePermission> springDataPage = rolePermissionDAO
				.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<RolePermission> findByPageable(
			Specification<RolePermission> specification, Page page) {
		org.springframework.data.domain.Page<RolePermission> springDataPage = rolePermissionDAO
				.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<RolePermission> findByRoleId(Long id) {
		return rolePermissionDAO.findByRoleId(id);
	}

	@Override
	public void save(List<RolePermission> newRList) {
		rolePermissionDAO.save(newRList);
	}

	@Override
	public void delete(List<RolePermission> delRList) {
		rolePermissionDAO.delete(delRList);
	}

	@Override
	public RolePermission findByRoleIdAndPermissionId(Long roleId,
			Long permissionId) {
		return rolePermissionDAO.findByRoleIdAndPermissionId(roleId,
				permissionId);
	}
}
