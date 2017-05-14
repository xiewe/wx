package com.framework.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.framework.entity.LogInfo;

public interface LogInfoDAO extends JpaRepository<LogInfo, Long>,
		JpaSpecificationExecutor<LogInfo> {

}