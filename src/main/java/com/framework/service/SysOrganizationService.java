package com.framework.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.framework.entity.SysOrganization;
import com.framework.utils.page.Page;

public interface SysOrganizationService {
	SysOrganization get(Integer id);

	SysOrganization saveOrUpdate(SysOrganization o);

	void delete(Integer id);

	List<SysOrganization> findAll(Page page);

	List<SysOrganization> findByPageable(
			Specification<SysOrganization> specification, Page page);
}
