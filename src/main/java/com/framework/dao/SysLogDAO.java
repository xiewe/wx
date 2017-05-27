package com.framework.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.framework.entity.SysLog;

public interface SysLogDAO extends JpaRepository<SysLog, Long>,
		JpaSpecificationExecutor<SysLog> {

}