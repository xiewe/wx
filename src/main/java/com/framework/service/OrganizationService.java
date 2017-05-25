package com.framework.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.framework.entity.Organization;
import com.framework.entity.User;
import com.framework.utils.dwz.Page;

public interface OrganizationService {
	Organization get(Long id);

	void saveOrUpdate(Organization organization);

	void delete(Long id);

	List<Organization> findAll(Page page);

	List<Organization> findByPageable(
			Specification<Organization> specification, Page page);

	Organization getByName(String name);

	Organization getTree();

	/**
	 * 根据用户查找组织及下属组织
	 * 
	 * @param user
	 * @return
	 */
	List<Organization> findAllOrganizationsByUser(User user);

	Long[] findAllOrganizationIdsByUser(User user);
}
