package com.framework.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.framework.entity.SysRole;
import com.framework.utils.pager.Pager;

public interface SysRoleService {
    SysRole get(Integer id);

    SysRole saveOrUpdate(SysRole o);

    void delete(Integer id);

    List<SysRole> findAll();

    List<SysRole> findAll(Specification<SysRole> specification);

    List<SysRole> findAll(Pager page);

    List<SysRole> findByPageable(Specification<SysRole> specification, Pager page);

    SysRole findByName(String name);
}
