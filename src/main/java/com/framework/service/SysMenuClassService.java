package com.framework.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.framework.entity.SysMenuClass;
import com.framework.utils.page.Page;

public interface SysMenuClassService {
	SysMenuClass get(Integer id);

	SysMenuClass saveOrUpdate(SysMenuClass o);

	void delete(Integer id);

	List<SysMenuClass> findAll(Page page);

	List<SysMenuClass> findByPageable(
			Specification<SysMenuClass> specification, Page page);
}
