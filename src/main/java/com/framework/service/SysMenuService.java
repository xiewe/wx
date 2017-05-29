package com.framework.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.framework.entity.SysMenu;
import com.framework.utils.page.Page;

public interface SysMenuService {
	SysMenu get(Integer id);

	SysMenu saveOrUpdate(SysMenu o);

	void delete(Integer id);

	List<SysMenu> findAll(Page page);

	List<SysMenu> findByPageable(Specification<SysMenu> specification, Page page);
}
