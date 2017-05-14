package com.framework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.UserRoleDAO;
import com.framework.entity.UserRole;
import com.framework.service.UserRoleService;
import com.framework.utils.dwz.Page;
import com.framework.utils.dwz.PageUtils;

@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

	@Autowired
	private UserRoleDAO userRoleDAO;

	@Override
	public UserRole get(Long id) {
		return userRoleDAO.findOne(id);
	}

	@Override
	public void saveOrUpdate(UserRole userRole) {
		userRoleDAO.save(userRole);
	}

	@Override
	public void delete(Long id) {
		userRoleDAO.delete(id);
	}

	@Override
	public List<UserRole> findAll(Page page) {
		org.springframework.data.domain.Page<UserRole> springDataPage = userRoleDAO
				.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<UserRole> findByPageable(Specification<UserRole> specification,
			Page page) {
		org.springframework.data.domain.Page<UserRole> springDataPage = userRoleDAO
				.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<UserRole> findByUserId(Long userId) {
		return userRoleDAO.findByUserId(userId);
	}

}
