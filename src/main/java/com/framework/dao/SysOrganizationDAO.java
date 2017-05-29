package com.framework.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.framework.entity.SysOrganization;

public interface SysOrganizationDAO extends
		JpaRepository<SysOrganization, Integer>,
		JpaSpecificationExecutor<SysOrganization> {

}