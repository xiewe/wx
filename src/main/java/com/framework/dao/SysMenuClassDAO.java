package com.framework.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.framework.entity.SysMenuClass;

public interface SysMenuClassDAO extends JpaRepository<SysMenuClass, Integer>, JpaSpecificationExecutor<SysMenuClass> {
	@Query(value = "select distinct c from SysMenuClass c, SysRolePermission p where c.id = p.sysMenuClass.id and p.sysRole.id = :rid")
	List<SysMenuClass> findByRoleId(@Param("rid") Integer rid);
}