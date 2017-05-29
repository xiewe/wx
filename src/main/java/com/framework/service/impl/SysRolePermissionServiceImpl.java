package com.framework.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.SysRolePermissionDAO;
import com.framework.entity.SysRolePermission;
import com.framework.service.SysRolePermissionService;
import com.framework.utils.page.Page;
import com.framework.utils.page.PageUtils;

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
	public List<SysRolePermission> findAll(Page page) {
		org.springframework.data.domain.Page<SysRolePermission> springDataPage = oDao
				.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<SysRolePermission> findByPageable(
			Specification<SysRolePermission> specification, Page page) {
		if (null != page && StringUtils.isEmpty(page.getOrderField())) {
			page.setOrderDirection("desc");
			page.setOrderField("createTime");
		}
		org.springframework.data.domain.Page<SysRolePermission> springDataPage = oDao
				.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}
}
