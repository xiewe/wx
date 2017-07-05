package com.framework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.SysMenuClassDAO;
import com.framework.entity.SysMenuClass;
import com.framework.service.SysMenuClassService;
import com.framework.utils.pager.Pager;

@Service
@Transactional
public class SysMenuClassServiceImpl implements SysMenuClassService {

	@Autowired
	private SysMenuClassDAO oDao;

	@Override
	public SysMenuClass get(Integer id) {
		return oDao.findOne(id);
	}

	@Override
	public SysMenuClass saveOrUpdate(SysMenuClass o) {
		return oDao.save(o);
	}

	@Override
	public void delete(Integer id) {
		oDao.delete(id);
	}

	@Override
	public List<SysMenuClass> findAll(Pager pager) {
		org.springframework.data.domain.Page<SysMenuClass> springDataPage = oDao.findAll(pager.parsePageable());
		pager.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<SysMenuClass> findByPageable(Specification<SysMenuClass> specification, Pager pager) {
		org.springframework.data.domain.Page<SysMenuClass> springDataPage = oDao.findAll(specification,
		        pager.parsePageable());
		pager.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<SysMenuClass> findAll() {
		return oDao.findAll();
	}

	@Override
	public List<SysMenuClass> findAll(Specification<SysMenuClass> specification) {
		return oDao.findAll(specification);
	}
}
