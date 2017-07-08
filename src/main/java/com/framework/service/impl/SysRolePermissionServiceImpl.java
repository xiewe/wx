package com.framework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.SysRolePermissionDAO;
import com.framework.entity.SysRolePermission;
import com.framework.service.SysRolePermissionService;
import com.framework.utils.pager.Pager;

@Service
@Transactional
public class SysRolePermissionServiceImpl implements SysRolePermissionService {

	@Autowired
	private SysRolePermissionDAO oDao;

	@Override
	public SysRolePermission get(Integer id) {
		return oDao.findOne(id);
	}

	@Override
	public SysRolePermission saveOrUpdate(SysRolePermission o) {
		return oDao.save(o);
	}

	@Override
	public void delete(Integer id) {
		oDao.delete(id);
	}

	@Override
	public List<SysRolePermission> findAll(Pager pager) {
		org.springframework.data.domain.Page<SysRolePermission> springDataPage = oDao.findAll(pager.parsePageable());
		pager.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<SysRolePermission> findByPageable(Specification<SysRolePermission> specification, Pager pager) {
		org.springframework.data.domain.Page<SysRolePermission> springDataPage = oDao.findAll(specification,
		        pager.parsePageable());
		pager.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<SysRolePermission> findAll() {
		return oDao.findAll();
	}

	@Override
	public List<SysRolePermission> findAll(Specification<SysRolePermission> specification) {
		return oDao.findAll(specification);
	}

}
