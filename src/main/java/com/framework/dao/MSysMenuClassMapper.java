package com.framework.dao;

import com.framework.entity.MSysMenuClass;

public interface MSysMenuClassMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MSysMenuClass record);

    int insertSelective(MSysMenuClass record);

    MSysMenuClass selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MSysMenuClass record);

    int updateByPrimaryKey(MSysMenuClass record);
}