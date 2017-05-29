package com.framework.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.SysUserDAO;
import com.framework.entity.SysUser;
import com.framework.service.SysUserService;
import com.framework.utils.page.Page;
import com.framework.utils.page.PageUtils;

@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserDAO oDao;

	@Override
	public SysUser get(Long id) {
		return oDao.findOne(id);
	}

	@Override
	public SysUser saveOrUpdate(SysUser o) {
		return oDao.save(o);
	}

	@Override
	public void delete(Long id) {
		oDao.delete(id);
	}

	@Override
	public List<SysUser> findAll(Page page) {
		org.springframework.data.domain.Page<SysUser> springDataPage = oDao
				.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<SysUser> findByPageable(Specification<SysUser> specification,
			Page page) {
		if (null != page && StringUtils.isEmpty(page.getOrderField())) {
			page.setOrderDirection("desc");
			page.setOrderField("createTime");
		}
		org.springframework.data.domain.Page<SysUser> springDataPage = oDao
				.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}
}
