package com.framework.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.framework.entity.SysOrganization;

public interface SysOrganizationDAO extends JpaRepository<SysOrganization, Integer>,
        JpaSpecificationExecutor<SysOrganization> {
    List<SysOrganization> findByIdIn(List<Integer> ids);

    SysOrganization findByName(String name);
}