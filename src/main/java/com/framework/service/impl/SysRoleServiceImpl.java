package com.framework.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.SysRoleDAO;
import com.framework.entity.SysRole;
import com.framework.service.SysRoleService;
import com.framework.utils.page.Page;
import com.framework.utils.page.PageUtils;

@Service
@Transactional
public class SysRoleServiceImpl implements SysRoleService {

	@Autowired
	private SysRoleDAO oDao;

	@Override
	public SysRole get(Integer id) {
		return oDao.findOne(id);
	}

	@Override
	public SysRole saveOrUpdate(SysRole o) {
		return oDao.save(o);
	}

	@Override
	public void delete(Integer id) {
		oDao.delete(id);
	}

	@Override
	public List<SysRole> findAll(Page page) {
		org.springframework.data.domain.Page<SysRole> springDataPage = oDao
				.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<SysRole> findByPageable(Specification<SysRole> specification,
			Page page) {
		if (null != page && StringUtils.isEmpty(page.getOrderField())) {
			page.setOrderDirection("desc");
			page.setOrderField("createTime");
		}
		org.springframework.data.domain.Page<SysRole> springDataPage = oDao
				.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}
}
