package com.framework.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.framework.entity.SysLog;
import com.framework.utils.page.Page;

public interface SysLogService {
	SysLog get(Long id);

	void saveOrUpdate(SysLog logInfo);

	void delete(Long id);

	List<SysLog> findAll(Page page);

	List<SysLog> findByPageable(Specification<SysLog> specification, Page page);
}
