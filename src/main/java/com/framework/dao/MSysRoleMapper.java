package com.framework.dao;

import com.framework.entity.MSysRole;

public interface MSysRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MSysRole record);

    int insertSelective(MSysRole record);

    MSysRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MSysRole record);

    int updateByPrimaryKey(MSysRole record);
}