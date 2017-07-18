package com.framework.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.framework.entity.SysRolePermission;

public interface SysRolePermissionDAO extends JpaRepository<SysRolePermission, Integer>,
        JpaSpecificationExecutor<SysRolePermission> {
    @Query(value = "select distinct p from SysRolePermission p where p.sysRole.id = :rid order by p.id")
    List<SysRolePermission> findByRoleId(@Param("rid") Integer rid);
}