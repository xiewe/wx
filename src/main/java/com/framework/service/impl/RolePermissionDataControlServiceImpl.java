package com.framework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.RolePermissionDataControlDAO;
import com.framework.entity.RolePermissionDataControl;
import com.framework.service.RolePermissionDataControlService;
import com.framework.utils.dwz.Page;
import com.framework.utils.dwz.PageUtils;

@Service
@Transactional
public class RolePermissionDataControlServiceImpl implements
		RolePermissionDataControlService {

	@Autowired
	private RolePermissionDataControlDAO rolePermissionDataControlDAO;

	@Override
	public RolePermissionDataControl get(Long id) {
		return rolePermissionDataControlDAO.findOne(id);
	}

	@Override
	public void saveOrUpdate(RolePermissionDataControl rolePermissionDataControl) {
		rolePermissionDataControlDAO.save(rolePermissionDataControl);
	}

	@Override
	public void delete(Long id) {
		rolePermissionDataControlDAO.delete(id);
	}

	@Override
	public List<RolePermissionDataControl> findAll(Page page) {
		org.springframework.data.domain.Page<RolePermissionDataControl> springDataPage = rolePermissionDataControlDAO
				.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<RolePermissionDataControl> findByPageable(
			Specification<RolePermissionDataControl> specification, Page page) {
		org.springframework.data.domain.Page<RolePermissionDataControl> springDataPage = rolePermissionDataControlDAO
				.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public void save(List<RolePermissionDataControl> newRList) {
		rolePermissionDataControlDAO.save(newRList);
	}

	@Override
	public void delete(List<RolePermissionDataControl> delRList) {
		rolePermissionDataControlDAO.delete(delRList);
	}

	@Override
	public List<RolePermissionDataControl> findByRolePermissionRoleId(Long id) {
		return rolePermissionDataControlDAO.findByRolePermissionRoleId(id);
	}

}
