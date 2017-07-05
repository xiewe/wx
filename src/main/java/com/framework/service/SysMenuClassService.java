package com.framework.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.framework.entity.SysMenuClass;
import com.framework.utils.pager.Pager;

public interface SysMenuClassService {
	SysMenuClass get(Integer id);

	SysMenuClass saveOrUpdate(SysMenuClass o);

	void delete(Integer id);

	List<SysMenuClass> findAll();

	List<SysMenuClass> findAll(Specification<SysMenuClass> specification);

	List<SysMenuClass> findAll(Pager page);

	List<SysMenuClass> findByPageable(Specification<SysMenuClass> specification, Pager page);
}
