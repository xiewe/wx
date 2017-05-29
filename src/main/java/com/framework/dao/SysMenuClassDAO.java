package com.framework.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.framework.entity.SysMenuClass;

public interface SysMenuClassDAO extends JpaRepository<SysMenuClass, Integer>,
		JpaSpecificationExecutor<SysMenuClass> {

}