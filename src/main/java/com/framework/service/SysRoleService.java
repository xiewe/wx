package com.framework.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.framework.entity.SysRole;
import com.framework.utils.page.Page;

public interface SysRoleService {
	SysRole get(Integer id);

	SysRole saveOrUpdate(SysRole o);

	void delete(Integer id);

	List<SysRole> findAll(Page page);

	List<SysRole> findByPageable(Specification<SysRole> specification, Page page);
}
