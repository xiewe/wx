package com.framework.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.framework.entity.DataControl;

public interface DataControlDAO extends JpaRepository<DataControl, Long>,
		JpaSpecificationExecutor<DataControl> {
	List<DataControl> findByOperator(String operator);
}