package com.framework.dao;

import com.framework.entity.MSysMenu;

public interface MSysMenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MSysMenu record);

    int insertSelective(MSysMenu record);

    MSysMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MSysMenu record);

    int updateByPrimaryKey(MSysMenu record);
}