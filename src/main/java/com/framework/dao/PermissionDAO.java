package com.framework.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.framework.entity.Permission;

public interface PermissionDAO extends JpaRepository<Permission, Long>,
		JpaSpecificationExecutor<Permission> {

	Page<Permission> findByParentId(Long parentId, Pageable pageable);

	List<Permission> findByParentId(Long parentId);

	@QueryHints(value = {
			@QueryHint(name = "org.hibernate.cacheable", value = "true"),
			@QueryHint(name = "org.hibernate.cacheRegion", value = "com.framework.entity.Permission") })
	@Query("from Permission m where m.category=0 order by m.priority ASC")
	List<Permission> findAllWithCache();

	List<Permission> findByCategoryOrderByPriorityAsc(int category);

	Permission getBySn(String sn);
}