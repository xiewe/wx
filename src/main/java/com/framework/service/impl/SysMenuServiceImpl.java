package com.framework.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.SysMenuDAO;
import com.framework.entity.SysMenu;
import com.framework.service.SysMenuService;
import com.framework.utils.page.Page;
import com.framework.utils.page.PageUtils;

@Service
@Transactional
public class SysMenuServiceImpl implements SysMenuService {

	@Autowired
	private SysMenuDAO oDao;

	@Override
	public SysMenu get(Integer id) {
		return oDao.findOne(id);
	}

	@Override
	public SysMenu saveOrUpdate(SysMenu o) {
		return oDao.save(o);
	}

	@Override
	public void delete(Integer id) {
		oDao.delete(id);
	}

	@Override
	public List<SysMenu> findAll(Page page) {
		org.springframework.data.domain.Page<SysMenu> springDataPage = oDao
				.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<SysMenu> findByPageable(Specification<SysMenu> specification,
			Page page) {
		if (null != page && StringUtils.isEmpty(page.getOrderField())) {
			page.setOrderDirection("desc");
			page.setOrderField("id");
		}
		org.springframework.data.domain.Page<SysMenu> springDataPage = oDao
				.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}
}
