package com.framework.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.framework.entity.SysUser;
import com.framework.utils.page.Page;

public interface SysUserService {
	SysUser get(Long id);

	SysUser saveOrUpdate(SysUser o);

	void delete(Long id);

	List<SysUser> findAll(Page page);

	List<SysUser> findByPageable(Specification<SysUser> specification, Page page);
}
