package com.framework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.SysRoleDAO;
import com.framework.entity.SysRole;
import com.framework.service.SysRoleService;
import com.framework.shiro.ShiroRealm;
import com.framework.utils.pager.Pager;

@Service
@Transactional
public class SysRoleServiceImpl implements SysRoleService {

	@Autowired
	private SysRoleDAO oDao;

	@Autowired
	private ShiroRealm shiroRealm;

	@Override
	public SysRole get(Integer id) {
		return oDao.findOne(id);
	}

	@Override
	public SysRole saveOrUpdate(SysRole o) {
		shiroRealm.clearAllCachedAuthorizationInfo();
		return oDao.save(o);
	}

	@Override
	public void delete(Integer id) {
		oDao.delete(id);
		shiroRealm.clearAllCachedAuthorizationInfo();
	}

	@Override
	public List<SysRole> findAll(Pager pager) {
		org.springframework.data.domain.Page<SysRole> springDataPage = oDao.findAll(pager.parsePageable());
		pager.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<SysRole> findByPageable(Specification<SysRole> specification, Pager pager) {
		org.springframework.data.domain.Page<SysRole> springDataPage = oDao.findAll(specification,
		        pager.parsePageable());
		pager.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<SysRole> findAll() {
		return oDao.findAll();
	}

	@Override
	public List<SysRole> findAll(Specification<SysRole> specification) {
		return oDao.findAll(specification);
	}
}
