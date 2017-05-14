package com.framework.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.framework.entity.Organization;

public interface OrganizationDAO extends JpaRepository<Organization, Long>,
		JpaSpecificationExecutor<Organization> {
	@QueryHints(value = {
			@QueryHint(name = "org.hibernate.cacheable", value = "true"),
			@QueryHint(name = "org.hibernate.cacheRegion", value = "com.framework.entity.Organization") })
	@Query("from Organization o order by o.priority ASC")
	List<Organization> findAllWithCache();

	Organization getByName(String name);

	List<Organization> findByParentId(Long parentId);
}