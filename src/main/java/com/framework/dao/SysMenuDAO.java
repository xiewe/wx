package com.framework.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.framework.entity.SysMenu;

public interface SysMenuDAO extends JpaRepository<SysMenu, Integer>, JpaSpecificationExecutor<SysMenu> {
	@Query(value = "select distinct m from SysMenu m, SysRolePermission p where m.id = p.sysMenu.id and p.sysRole.id = :rid")
	List<SysMenu> findByRoleId(@Param("rid") Integer rid);
}