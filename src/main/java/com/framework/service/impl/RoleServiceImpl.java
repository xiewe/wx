package com.framework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.RoleDAO;
import com.framework.entity.Role;
import com.framework.service.RoleService;
import com.framework.utils.dwz.Page;
import com.framework.utils.dwz.PageUtils;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDAO roleDAO;

	@Override
	public Role get(Long id) {
		return roleDAO.findOne(id);
	}

	@Override
	public void saveOrUpdate(Role role) {
		roleDAO.save(role);
	}

	@Override
	public void delete(Long id) {
		roleDAO.delete(id);
	}

	@Override
	public List<Role> findAll(Page page) {
		org.springframework.data.domain.Page<Role> springDataPage = roleDAO
				.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<Role> findByPageable(Specification<Role> specification,
			Page page) {
		org.springframework.data.domain.Page<Role> springDataPage = roleDAO
				.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public Role getByName(String name) {
		return roleDAO.getByName(name);
	}

}
