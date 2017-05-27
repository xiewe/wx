package com.framework.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.framework.entity.LogInfo;
import com.framework.utils.page.Page;

public interface LogInfoService {
	LogInfo get(Long id);

	void saveOrUpdate(LogInfo logInfo);

	void delete(Long id);

	List<LogInfo> findAll(Page page);

	List<LogInfo> findByPageable(Specification<LogInfo> specification, Page page);
}
