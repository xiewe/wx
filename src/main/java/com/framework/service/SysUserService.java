package com.framework.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.framework.entity.SysUser;
import com.framework.exception.ServiceException;
import com.framework.utils.pager.Pager;

public interface SysUserService {
	SysUser get(Long id);

	SysUser saveOrUpdate(SysUser o);

	void delete(Long id);

	List<SysUser> findAll();

	List<SysUser> findAll(Specification<SysUser> specification);

	List<SysUser> findAll(Pager page);

	List<SysUser> findByPageable(Specification<SysUser> specification, Pager page);

	SysUser findByUsername(String username);

	SysUser findByEmail(String email);

	SysUser findByUid(String uid);

	SysUser findByPhone(String phone);

	SysUser findByU(String s);

	void updatePwd(SysUser user, String newPwd) throws ServiceException;

	void resetPwd(SysUser user, String newPwd);
}
