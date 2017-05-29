package com.framework.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.SysOrganizationDAO;
import com.framework.entity.SysOrganization;
import com.framework.service.SysOrganizationService;
import com.framework.utils.page.Page;
import com.framework.utils.page.PageUtils;

@Service
@Transactional
public class SysOrganizationServiceImpl implements SysOrganizationService {

	@Autowired
	private SysOrganizationDAO oDao;

	@Override
	public SysOrganization get(Integer id) {
		return oDao.findOne(id);
	}

	@Override
	public SysOrganization saveOrUpdate(SysOrganization o) {
		return oDao.save(o);
	}

	@Override
	public void delete(Integer id) {
		oDao.delete(id);
	}

	@Override
	public List<SysOrganization> findAll(Page page) {
		org.springframework.data.domain.Page<SysOrganization> springDataPage = oDao
				.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<SysOrganization> findByPageable(
			Specification<SysOrganization> specification, Page page) {
		if (null != page && StringUtils.isEmpty(page.getOrderField())) {
			page.setOrderDirection("desc");
			page.setOrderField("createTime");
		}
		org.springframework.data.domain.Page<SysOrganization> springDataPage = oDao
				.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}
}
