package com.framework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.AppConstants;
import com.framework.dao.PermissionDAO;
import com.framework.dao.RolePermissionDAO;
import com.framework.entity.Permission;
import com.framework.entity.Role;
import com.framework.entity.RolePermission;
import com.framework.service.PermissionService;
import com.framework.utils.dwz.Page;
import com.framework.utils.dwz.PageUtils;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionDAO permissionDAO;

	@Autowired
	private RolePermissionDAO rolePermissionDAO;

	@Override
	public Permission get(Long id) {
		return permissionDAO.findOne(id);
	}

	@Override
	public void saveOrUpdate(Permission permission) {
		permissionDAO.save(permission);
	}

	@Override
	public void delete(Long id) {
		permissionDAO.delete(id);
	}

	@Override
	public List<Permission> findAll(Page page) {
		org.springframework.data.domain.Page<Permission> springDataPage = permissionDAO
				.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<Permission> findByPageable(
			Specification<Permission> specification, Page page) {
		org.springframework.data.domain.Page<Permission> springDataPage = permissionDAO
				.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public Permission getTree() {
		Permission root = permissionDAO.findOne(1L);
		makeChildre(root);
		return root;
	}

	private void makeChildre(Permission parent) {
		List<Permission> children = permissionDAO
				.findByParentId(parent.getId());
		for (Permission o : children) {
			if (o.getCategory() == AppConstants.PERMISSION_TYPE_FOLDER) {
				makeChildre(o);
			} else {
				o.setChildren(null);
			}
		}
		parent.setChildren(children);
	}

	@Override
	public List<Permission> findAll() {
		return permissionDAO.findAll();
	}

	@Override
	public void saveAssignRolePermission(Permission[] arrAssignPermissions,
			Role role) {
		// 原来权限
		List<RolePermission> lstOldRolePermissions = rolePermissionDAO
				.findByRoleId(role.getId());

		// 遍历原来权限，如果在新权限中找不到，删除
		for (RolePermission o : lstOldRolePermissions) {
			boolean isContains = false;
			for (Permission permission : arrAssignPermissions) {
				if (o.getPermission().getId().equals(permission.getId())) {
					isContains = true;
					break;
				}
			}
			if (!isContains) {
				rolePermissionDAO.delete(o);
			}
		}

		// 遍历新权限，如果在原来权限中找不到，新增
		for (Permission permission : arrAssignPermissions) {
			boolean isContains = false;
			for (RolePermission o : lstOldRolePermissions) {
				if (o.getPermission().getId().equals(permission.getId())) {
					isContains = true;
					break;
				}
			}
			if (!isContains) {
				RolePermission o = new RolePermission();
				o.setRole(role);
				o.setPermission(permission);
				rolePermissionDAO.save(o);
			}
		}

	}

	@Override
	public Permission getBySn(String sn) {
		return permissionDAO.getBySn(sn);
	}

	@Override
	public List<Permission> findByCategoryOrderByPriorityAsc(int category) {
		return permissionDAO.findByCategoryOrderByPriorityAsc(category);
	}
}
