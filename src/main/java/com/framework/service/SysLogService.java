package com.framework.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.framework.entity.SysLog;
import com.framework.utils.pager.Pager;

public interface SysLogService {
	SysLog get(Long id);

	void saveOrUpdate(SysLog o);

	void delete(Long id);

	List<SysLog> findAll();
	
	List<SysLog> findAll(Specification<SysLog> specification);
	
	List<SysLog> findAll(Pager page);

	List<SysLog> findByPageable(Specification<SysLog> specification, Pager page);
}
