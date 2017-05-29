package com.framework.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.framework.entity.SysMenu;

public interface SysMenuDAO extends JpaRepository<SysMenu, Integer>,
		JpaSpecificationExecutor<SysMenu> {

}